package com.linln.admin.system.controller;

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
import com.linln.modules.task.domain.Task;
import com.linln.modules.task.service.TaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/index")
    @RequiresPermissions("task:index")
    public String index(Model model, Task task){
        User user = ShiroUtil.getSubject();
        if (!Objects.equals(user.getUsername(),"admin")) {
            task.setUserName(user.getUsername());
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

    @PostMapping("/robOrder")
    @ResponseBody
    public ResultVo robOrder(@RequestBody String  task) {
        // 获取用户列表
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
    public ResultVo save(@Validated TaskValid valid, @EntityParam Task task){
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
        task.setUserName(user.getUsername());
        task.setTaskStatus((byte)1);

        task.setEffective((byte)0);
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
     * 发布浏览
     * @param valid 验证对象
     */
    @PostMapping({"/update"})
    @RequiresPermissions({"task:update"})
    @ResponseBody
    public ResultVo update(@Validated TaskValid valid, @EntityParam Task task){
        User user = ShiroUtil.getSubject();
        //1、任务名称是否重复
        if ( taskService.repeateTaskName(user.getUsername(),task.getTaskName())) {
            throw new ResultException(ResultEnum.SHOP_SHOPNAME_ERROR);
        }
        if(task.getId()==null){
            task.setCreateDate(new Date());
            task.setUpdateDate(new Date());
            task.setDeleteFlg((byte)0);
            //用户编号
            task.setUserName(user.getUsername());
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
        taskService.save(task);
        return ResultVoUtil.SAVE_SUCCESS;
    }



    @GetMapping("/delete/{id}")
    @RequiresPermissions("task:delete")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") Long id, Model model){
        Task oldTask = taskService.getTaskById(id);
        if (oldTask.getTaskStatus()>=2){
            return ResultVoUtil.error("只有待审核状态的任务可以删除！");
        }
        taskService.deleteTaskById(id);
        return  ResultVoUtil.SAVE_SUCCESS;
    }


    @GetMapping("/audit/{id}")
    @RequiresPermissions("task:audit")
    @ResponseBody
    public ResultVo audit(@PathVariable("id") Long id, Model model){
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
