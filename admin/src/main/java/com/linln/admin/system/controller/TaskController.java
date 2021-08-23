package com.linln.admin.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.linln.admin.system.config.YamlPropertySourceFactory;
import com.linln.admin.system.domain.RobTaskRequest;
import com.linln.admin.system.validator.TaskValid;
import com.linln.common.enums.ResultEnum;
import com.linln.common.exception.ResultException;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.SaveAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.User;
import com.linln.modules.task.domain.IntegralLogger;
import com.linln.modules.task.domain.Price;
import com.linln.modules.task.domain.RobTask;
import com.linln.modules.task.domain.Task;
import com.linln.modules.task.service.IntegralLoggerService;
import com.linln.modules.task.service.IntegralService;
import com.linln.modules.task.service.PriceService;
import com.linln.modules.task.service.TaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/task")
@PropertySource(value = {"classpath:application.yml"},factory = YamlPropertySourceFactory.class)
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private IntegralService integralService;

    @Autowired
    private IntegralLoggerService integralLoggerService;

    @GetMapping("/index")
    @RequiresPermissions("task:index")
    public String index(Model model, Task task){
        User user = ShiroUtil.getSubject();
        if (!Objects.equals(user.getUsername(),"admin")) {
            task.setMerchantName(user.getUsername());
        }
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("merchantId", match -> match.contains());

        Example<Task> example = Example.of(task, matcher);
        Page<Task> list = taskService.getPageList(example);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/task/index";
    }


    /**
     * 商户查看已抢单列表
     * @param model
     * @return
     */
    @GetMapping("/viewRobTask")
    @RequiresPermissions("view:robTask")
    public String viewRobTask(Model model, RobTask robTask){
        User user = ShiroUtil.getSubject();
        if (!Objects.equals(user.getUsername(),"admin")) {
            robTask.setMerchantName(user.getUsername());
        }
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("merchantName", match -> match.contains());

        Example<RobTask> example = Example.of(robTask, matcher);
        Page<RobTask> list = taskService.merchantGetRobTaskList(example);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/robTask/viewRobTask";
    }


    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("task:add")
    public String toAdd(){
        return "/task/add";
    }


    /**
     * C端任务中心列表
     */
    @PostMapping("/2cTaskList")
    @ResponseBody
    public Page<Task> task2c(@RequestBody String  cUser) {
        // 获取用户列表
        Page<Task> list = taskService.getPageList2C(cUser);
        return list;
    }

    /**
     * C端已抢单任务
     */
    @PostMapping("/myRobTask")
    @ResponseBody
    public Page<RobTask> myRobTask(@RequestBody String  robTask) {
        RobTaskRequest robTaskRequest=JSONObject.parseObject(robTask,RobTaskRequest.class);
        // 获取用户列表
        Page<RobTask> list = taskService.getRobTaskByUserName(robTaskRequest.getUserName(),robTaskRequest.page,robTaskRequest.getLimit());
        return list;
    }

    @PostMapping("/robOrder")
    @ResponseBody
    public ResultVo robOrder(@RequestBody String  task) {
        // 获取抢单列表
        ResultVo resultVo = taskService.robOrder(task);
        return resultVo;
    }



    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add"})
    @RequiresPermissions({"task:add"})
    @ResponseBody
    @ActionLog(name = "任务管理", message = "任务：${userName}", action = SaveAction.class)
    public ResultVo save(@Validated TaskValid valid, @EntityParam Task task, HttpServletRequest request){
        User user = ShiroUtil.getSubject();
        //1、任务名称是否重复
       if ( taskService.repeateTaskName(user.getUsername(),task.getTaskName())) {
            throw new ResultException(ResultEnum.TASK_NAME_REPEATER_ERROR);
        }
        //2、保存任务
        task.setCreateDate(new Date());
        task.setUpdateDate(new Date());
        task.setDeleteFlg((byte)0);
        //用户编号
        task.setMerchantName(user.getUsername());
        task.setMerchantId(user.getId());
        task.setTaskStatus((byte)1);
        task.setEffective((byte)0);
        //计算佣金
        List<Price> customerPriceByPrice = priceService.getCustomerPriceByPrice(task.getBabyPrice(), task.getTaskType());
        if (!CollectionUtils.isEmpty(customerPriceByPrice)){
            task.setYj(customerPriceByPrice.get(0).getPrice());
        }
        Task newTask = taskService.save(task);
        ResultVo resultVo=new ResultVo();
        if(newTask!=null){
            resultVo.setCode(ResultEnum.SUCCESS.getCode());
            resultVo.setMsg(ResultEnum.SUCCESS.getMessage());
            resultVo.setData(newTask);
        }else{
            resultVo.setCode(ResultEnum.ERROR.getCode());
            resultVo.setMsg(ResultEnum.ERROR.getMessage());
            resultVo.setData(null);
        }
        return resultVo;
    }

    /**
     * 确认发布
     * @param valid 验证对象
     */
    @PostMapping({"/update"})
    @RequiresPermissions({"task:update"})
    @ResponseBody
    public ResultVo update(@Validated TaskValid valid, @EntityParam Task task, HttpServletRequest request){
        User user = ShiroUtil.getSubject();
        if(task.getId()==null){
            task.setCreateDate(new Date());
            task.setUpdateDate(new Date());
            task.setDeleteFlg((byte)0);
            //用户编号
            task.setMerchantName(user.getUsername());
            task.setMerchantId(user.getId());
            task.setTaskStatus((byte)1);
            task.setEffective((byte)0);
        }
        //2、保存任务
        task.setCreateDate(new Date());
        task.setUpdateDate(new Date());
        task.setDeleteFlg((byte)0);
        //用户编号
        task.setTaskStatus((byte)1);
        task.setEffective((byte)1);
        //计算佣金
        List<Price> customerPriceByPrice = priceService.getCustomerPriceByPrice(task.getBabyPrice(), task.getTaskType());
        if (!CollectionUtils.isEmpty(customerPriceByPrice)){
            task.setYj(customerPriceByPrice.get(0).getPrice());
        }
        taskService.save(task);
        return ResultVoUtil.SAVE_SUCCESS;
    }



    @GetMapping("/delete/{id}")
    @RequiresPermissions("task:delete")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") Long id, Model model){
        Task oldTask = taskService.getTaskById(id);
        //判断该任务是否全部完成，如果没有全部完成不允许删除

       /* if (oldTask.getTaskStatus()>=2){
            return ResultVoUtil.error("只有待审核状态的任务可以删除！");
        }*/
        taskService.deleteTaskById(id);
        return  ResultVoUtil.SAVE_SUCCESS;
    }


    @GetMapping("/audit/{id}")
    @RequiresPermissions("task:audit")
    @ResponseBody
    public ResultVo audit(@PathVariable("id") Long id, Model model){
        User user = ShiroUtil.getSubject();
        Task oldTask = taskService.getTaskById(id);
        Byte effective = oldTask.getEffective();
        Byte taskStatus = oldTask.getTaskStatus();
        if (effective==0){
            return ResultVoUtil.error("任务在待发布阶段，不能审核！");
        }
        if (taskStatus>2){
            return ResultVoUtil.error("非待审核的任务，不需要审核！");
        }
        taskService.auditTaskById(id);
        //审核成功，扣减B端商户的积分。
        //任务本金
        BigDecimal babyPrice = oldTask.getBabyPrice();
        String taskType = oldTask.getTaskType();
        //任务数量
        Integer personNum = oldTask.getPersonNum();
        String merchantName = oldTask.getMerchantName();
        List<Price> priceByPrice = priceService.getMerchantPriceByPrice(babyPrice, taskType);
        if(!CollectionUtils.isEmpty(priceByPrice)){
            try {
                //B端商户的促销价
                Price merchantPrice = priceByPrice.get(0);
                BigDecimal price = merchantPrice.getPrice();
                //计算积分 （本金+促销价）*任务数
                int point=(babyPrice.add(price)).multiply(BigDecimal.valueOf(personNum)).intValue();
                //扣减积分
                integralService.addIntegral( point*(-1),merchantName);
                //增加积分扣减日志
                IntegralLogger integralLogger=new IntegralLogger();
                integralLogger.setBusinessType("任务审核");
                integralLogger.setMerchantName(merchantName);
                integralLogger.setOperatorType("扣减");
                integralLogger.setPoint(point*(-1));
                integralLogger.setOperatorName(user.getUsername());
                integralLogger.setCreateDate(new Date());
                integralLogger.setDeleteFlg((byte)0);

                integralLoggerService.addIntegralLogger(integralLogger);
            } catch (Exception e) {
                taskService.unAuditTaskById(id);
                ResultVo resultVo=new ResultVo();
                resultVo.setMsg("审核失败，原因："+e.getMessage());
                resultVo.setCode(-1);
                return  resultVo;
            }
        }
        return  ResultVoUtil.SAVE_SUCCESS;
    }



    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("task:edit")
    public String toEdit(@PathVariable("id") Long id, Model model){
        Task oldTask = taskService.getTaskById(id);
        String [] babyPics=oldTask.getBabyPic()==null?new String[0]:oldTask.getBabyPic().split(",");
        model.addAttribute("task", oldTask);
        model.addAttribute("babyPic", babyPics);
        return "/task/edit";
    }


    @GetMapping("/detail/{id}")
    @RequiresPermissions("task:detail")
    public String detail(@PathVariable("id") Long id, Model model) {
        Task oldTask = taskService.getTaskById(id);
        String [] babyPics=oldTask.getBabyPic()==null?new String[0]:oldTask.getBabyPic().split(",");
        model.addAttribute("task", oldTask);
        model.addAttribute("babyPic", babyPics);
        return "/task/detail";
    }

}
