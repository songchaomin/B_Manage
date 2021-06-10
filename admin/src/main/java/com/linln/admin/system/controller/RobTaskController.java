package com.linln.admin.system.controller;

import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.modules.task.service.RobTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
        return  ResultVoUtil.success("删除成功");
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
        return  ResultVoUtil.success("审核成功");
    }



    @PostMapping("/updateRobTask")
    @ResponseBody
    public ResultVo updateRobTask(@RequestBody String  robTask) {
        try {
            robTaskService.updateRobTask(robTask);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVoUtil.error(e.getMessage());
        }
        return ResultVoUtil.success("提交成功");
    }

}
