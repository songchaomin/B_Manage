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
        // ??????????????????????????????????????????
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("merchantId", match -> match.contains());

        Example<Task> example = Example.of(task, matcher);
        Page<Task> list = taskService.getPageList(example);
        // ????????????
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/task/index";
    }


    /**
     * ???????????????????????????
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
        // ??????????????????????????????????????????
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("merchantName", match -> match.contains());

        Example<RobTask> example = Example.of(robTask, matcher);
        Page<RobTask> list = taskService.merchantGetRobTaskList(example);
        // ????????????
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/robTask/viewRobTask";
    }


    /**
     * ?????????????????????
     */
    @GetMapping("/add")
    @RequiresPermissions("task:add")
    public String toAdd(){
        return "/task/add";
    }


    /**
     * C?????????????????????
     */
    @PostMapping("/2cTaskList")
    @ResponseBody
    public Page<Task> task2c(@RequestBody String  cUser) {
        // ??????????????????
        Page<Task> list = taskService.getPageList2C(cUser);
        return list;
    }

    /**
     * C??????????????????
     */
    @PostMapping("/myRobTask")
    @ResponseBody
    public Page<RobTask> myRobTask(@RequestBody String  robTask) {
        RobTaskRequest robTaskRequest=JSONObject.parseObject(robTask,RobTaskRequest.class);
        // ??????????????????
        Page<RobTask> list = taskService.getRobTaskByUserName(robTaskRequest.getUserName(),robTaskRequest.page,robTaskRequest.getLimit());
        return list;
    }

    @PostMapping("/robOrder")
    @ResponseBody
    public ResultVo robOrder(@RequestBody String  task) {
        // ??????????????????
        ResultVo resultVo = taskService.robOrder(task);
        return resultVo;
    }



    /**
     * ????????????/???????????????
     * @param valid ????????????
     */
    @PostMapping({"/add"})
    @RequiresPermissions({"task:add"})
    @ResponseBody
    @ActionLog(name = "????????????", message = "?????????${userName}", action = SaveAction.class)
    public ResultVo save(@Validated TaskValid valid, @EntityParam Task task, HttpServletRequest request){
        User user = ShiroUtil.getSubject();
        //1???????????????????????????
       if ( taskService.repeateTaskName(user.getUsername(),task.getTaskName())) {
            throw new ResultException(ResultEnum.TASK_NAME_REPEATER_ERROR);
        }
        //2???????????????
        task.setCreateDate(new Date());
        task.setUpdateDate(new Date());
        task.setDeleteFlg((byte)0);
        //????????????
        task.setMerchantName(user.getUsername());
        task.setMerchantId(user.getId());
        task.setTaskStatus((byte)1);
        task.setEffective((byte)0);
        //????????????
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
     * ????????????
     * @param valid ????????????
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
            //????????????
            task.setMerchantName(user.getUsername());
            task.setMerchantId(user.getId());
            task.setTaskStatus((byte)1);
            task.setEffective((byte)0);
        }
        //2???????????????
        task.setCreateDate(new Date());
        task.setUpdateDate(new Date());
        task.setDeleteFlg((byte)0);
        //????????????
        task.setTaskStatus((byte)1);
        task.setEffective((byte)1);
        //????????????
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
        //???????????????????????????????????????????????????????????????????????????

       /* if (oldTask.getTaskStatus()>=2){
            return ResultVoUtil.error("?????????????????????????????????????????????");
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
            return ResultVoUtil.error("??????????????????????????????????????????");
        }
        if (taskStatus>2){
            return ResultVoUtil.error("??????????????????????????????????????????");
        }
        taskService.auditTaskById(id);
        //?????????????????????B?????????????????????
        //????????????
        BigDecimal babyPrice = oldTask.getBabyPrice();
        String taskType = oldTask.getTaskType();
        //????????????
        Integer personNum = oldTask.getPersonNum();
        String merchantName = oldTask.getMerchantName();
        List<Price> priceByPrice = priceService.getMerchantPriceByPrice(babyPrice, taskType);
        if(!CollectionUtils.isEmpty(priceByPrice)){
            try {
                //B?????????????????????
                Price merchantPrice = priceByPrice.get(0);
                BigDecimal price = merchantPrice.getPrice();
                //???????????? ?????????+????????????*?????????
                int point=(babyPrice.add(price)).multiply(BigDecimal.valueOf(personNum)).intValue();
                //????????????
                integralService.addIntegral( point*(-1),merchantName);
                //????????????????????????
                IntegralLogger integralLogger=new IntegralLogger();
                integralLogger.setBusinessType("????????????");
                integralLogger.setMerchantName(merchantName);
                integralLogger.setOperatorType("??????");
                integralLogger.setPoint(point*(-1));
                integralLogger.setOperatorName(user.getUsername());
                integralLogger.setCreateDate(new Date());
                integralLogger.setDeleteFlg((byte)0);

                integralLoggerService.addIntegralLogger(integralLogger);
            } catch (Exception e) {
                taskService.unAuditTaskById(id);
                ResultVo resultVo=new ResultVo();
                resultVo.setMsg("????????????????????????"+e.getMessage());
                resultVo.setCode(-1);
                return  resultVo;
            }
        }
        return  ResultVoUtil.SAVE_SUCCESS;
    }



    /**
     * ?????????????????????
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
