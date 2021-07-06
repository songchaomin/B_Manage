package com.linln.admin.system.controller;

import com.linln.admin.system.domain.UserCenter;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.service.RoleService;
import com.linln.modules.system.service.UserService;
import com.linln.modules.task.domain.Integral;
import com.linln.modules.task.service.IntegralService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/userCenter")
public class UserCenterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private IntegralService integralService;



    /**
     * 个人信息
     */
    @GetMapping("/userInfo")
    @RequiresPermissions("userCenter:userInfo")
    public String userInfo(Model model) {
        User user = ShiroUtil.getSubject();
        User newUser = userService.getById(user.getId());
        model.addAttribute("user", newUser);
        return "/userCenter/userInfo";
    }

    /**
     * 获取用户积分
     */
    @GetMapping("/getIntegral")
    @RequiresPermissions("userCenter:getIntegral")
    public String getIntegral(Model model) {
        User user = ShiroUtil.getSubject();
        Integral integral = integralService.getIntegralByUserName(user.getUsername());
        if (integral==null){
            integral=new Integral();
            integral.setPoint(0);
        }
        model.addAttribute("integral", integral);
        return "/userCenter/integral";
    }

    /**
     * 获取用户积分
     */
    @GetMapping("/reback")
    @RequiresPermissions("userCenter:reback")
    public String reback(Model model) {
        return "/userCenter/reback";
    }





}
