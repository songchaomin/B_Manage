package com.linln.admin.system.controller;

import com.alibaba.fastjson.JSONObject;
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
import com.linln.modules.task.domain.RobTask;
import com.linln.modules.task.domain.Task;
import com.linln.modules.task.service.RobTaskService;
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
import java.util.Objects;

@Controller
@RequestMapping("/robTask")
public class RobTaskController {
    @Autowired
    private RobTaskService robTaskService;





    @GetMapping("/delete/{id}")
    @RequiresPermissions("robTask:delete")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") Long id, Model model){
        robTaskService.delRobTaskById(id);
        return  ResultVoUtil.success("审核成功");
    }

    @GetMapping("/auditAccount/{id}")
    @RequiresPermissions("robTask:auditAccount")
    @ResponseBody
    public ResultVo auditAccount(@PathVariable("id") Long id, Model model){
        robTaskService.changeRobTaskStatus(id,4);
        return  ResultVoUtil.success("审核成功");
    }

    @GetMapping("/deliver/{id}")
    @RequiresPermissions("robTask:deliver")
    @ResponseBody
    public ResultVo deliver(@PathVariable("id") Long id, Model model){
        robTaskService.changeRobTaskStatus(id,6);
        return  ResultVoUtil.SAVE_SUCCESS;
    }






}
