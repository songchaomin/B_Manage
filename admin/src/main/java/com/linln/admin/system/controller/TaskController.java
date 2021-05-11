package com.linln.admin.system.controller;

import com.linln.admin.system.validator.ShopValid;
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
import com.linln.modules.task.domain.Shop;
import com.linln.modules.task.domain.Task;
import com.linln.modules.task.service.ShopService;
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
       if ( taskService.repeateTaskName(task.getUserName(),task.getTaskName())) {
            throw new ResultException(ResultEnum.SHOP_SHOPNAME_ERROR);
        }
        //2、保存任务
        task.setCreateDate(new Date());
        task.setUpdateDate(new Date());
        task.setDeleteFlg((byte)0);
        //用户编号
        task.setUserName(user.getUsername());
        task.setTaskStatus((byte)1);
        task.setEffective((byte)1);
        taskService.save(task);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("task:detail")
    public String toDetail(@PathVariable("id") Long id, Model model){
       /* Shop shop=taskService.getShopById(id);
        String [] shopPics=shop.getShopPic()==null ? new String[0] :shop.getShopPic().split(",");
        model.addAttribute("shopPics",shopPics);
        model.addAttribute("shop",shop);*/
        return "/shop/detail";
    }


    @GetMapping("/delete/{id}")
    @RequiresPermissions("task:delete")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") Long id, Model model){
        taskService.deleteTaskById(id);
        return  ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("shop:edit")
    public String toEdit(@PathVariable("id") Long id, Model model){
        /*Shop shop = taskService.getShopById(id);
        String [] shopPics=shop.getShopPic()==null ? new String[0] :shop.getShopPic().split(",");
        model.addAttribute("shopPics",shopPics);
        model.addAttribute("shop",shop);*/
        return "/shop/edit";
    }


    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/edit"})
    @RequiresPermissions({"shop:edit"})
    @ResponseBody
    @ActionLog(name = "店铺管理", message = "店铺：${userName}", action = SaveAction.class)
    public ResultVo updateShop(@Validated TaskValid valid, @EntityParam Task task){
        //2、更新店铺
        /*shop.setUpdateDate(new Date());
        taskService.save(shop);*/
        return ResultVoUtil.SAVE_SUCCESS;
    }

}
