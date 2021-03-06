package com.linln.admin.system.controller;

import com.google.common.collect.ImmutableSet;
import com.linln.admin.system.validator.UserRegisterValid;
import com.linln.admin.system.validator.UserValid;
import com.linln.common.constant.AdminConst;
import com.linln.common.enums.ResultEnum;
import com.linln.common.enums.StatusEnum;
import com.linln.common.exception.ResultException;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.SpringContextUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.StatusAction;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.fileUpload.config.properties.UploadProjectProperties;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.Role;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.repository.UserRepository;
import com.linln.modules.system.service.RoleService;
import com.linln.modules.system.service.UserService;
import com.linln.modules.task.domain.Shop;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author ?????????
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * ????????????
     */
    @GetMapping("/index")
    @RequiresPermissions("system:user:index")
    public String index(Model model, User user) {
        // ??????????????????
        Page<User> list = userService.getPageList(user);
        // ????????????
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("dept", user.getDept());
        return "/system/user/index";
    }

    @GetMapping("/userlistinfo")
    public String userlistinfo(Model model, User user) {
        // ??????????????????
        User sessionUser = ShiroUtil.getSubject();
        if (!Objects.equals(sessionUser.getUsername(),"admin")) {
            user.setUsername(sessionUser.getUsername());
        }
        Page<User> list = userService.getPageList(user);
        // ????????????
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("dept", user.getDept());
        return "/integral/user_pop";
    }

    /**
     * ?????????????????????
     */
    @GetMapping("/add")
    @RequiresPermissions("system:user:add")
    public String toAdd() {
        return "/system/user/add";
    }





    /**
     * ?????????????????????
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:user:edit")
    public String toEdit(@PathVariable("id") User user, Model model) {
        User dbUser = userService.getById(user.getId());
        model.addAttribute("user", dbUser);
        return "/system/user/edit";
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:user:edit")
    public String detail(@PathVariable("id") User user, Model model) {
        User dbUser = userService.getById(user.getId());
        model.addAttribute("user", dbUser);
        return "/system/user/detail";
    }

    /**
     * ????????????/???????????????
     * @param valid ????????????
     * @param user ????????????
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:user:add", "system:user:edit"})
    @ResponseBody
    @ActionLog(key = UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated UserValid valid, @EntityParam User user) {

        // ????????????????????????
        if (user.getId() == null) {

            // ????????????????????????
            if (user.getPassword().isEmpty() || "".equals(user.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }

            // ??????????????????????????????
            if (!user.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }

            // ?????????????????????
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(user.getPassword(), salt);
            user.setPassword(encrypt);
            user.setSalt(salt);
        }

        // ???????????????????????????
        if (userService.repeatByUsername(user)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }

        // ?????????????????????????????????
        if (user.getId() != null) {
            // ????????????????????????????????????
            if (user.getId().equals(AdminConst.ADMIN_ID) &&
                    !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }

            User beUser = userService.getById(user.getId());
            String[] fields = {"password", "salt", "picture", "roles"};
            EntityBeanUtil.copyProperties(beUser, user, fields);
        }
        // ????????????
        if (user.getId() == null) {
            user.setStatus((byte)2);
        }
        userService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * ????????????/???????????????
     * @param user ????????????
     */
    @PostMapping("/update")
    @RequiresPermissions({"system:user:update"})
    @ResponseBody
    public ResultVo update( @EntityParam User user) {

        // ????????????????????????
        if (user.getId() != null) {

            // ????????????????????????????????????
            if (user.getId().equals(AdminConst.ADMIN_ID) &&
                    !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }
            // ???????????????????????????
            if (userService.repeatByUsername(user)) {
                throw new ResultException(ResultEnum.USER_EXIST);
            }
        }
        userService.update(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }



    /**
     * ???????????????????????????
     */
    @GetMapping("/pwd")
    @RequiresPermissions("system:user:pwd")
    public String toEditPassword(Model model, @RequestParam(value = "ids", required = false) List<Long> ids) {
        model.addAttribute("idList", ids);
        return "/system/user/pwd";
    }

    /**
     * ????????????
     */
    @PostMapping("/pwd")
    @RequiresPermissions("system:user:pwd")
    @ResponseBody
    @ActionLog(key = UserAction.EDIT_PWD, action = UserAction.class)
    public ResultVo editPassword(String password, String confirm,
                                 @RequestParam(value = "ids", required = false) List<Long> ids,
                                 @RequestParam(value = "ids", required = false) List<User> users) {

        // ????????????????????????
        if (password.isEmpty() || "".equals(password.trim())) {
            throw new ResultException(ResultEnum.USER_PWD_NULL);
        }

        // ??????????????????????????????
        if (!password.equals(confirm)) {
            throw new ResultException(ResultEnum.USER_INEQUALITY);
        }

        // ????????????????????????????????????
        if (ids.contains(AdminConst.ADMIN_ID) &&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

        // ????????????????????????????????????
        users.forEach(user -> {
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(password, salt);
            user.setPassword(encrypt);
            user.setSalt(salt);
        });

        // ????????????
        userService.save(users);
        return ResultVoUtil.success("????????????");
    }

    /**
     * ???????????????????????????
     */
    @GetMapping("/role")
    @RequiresPermissions("system:user:role")
    public String toRole(@RequestParam(value = "ids") User user, Model model) {
        // ??????????????????????????????
        Set<Role> authRoles = user.getRoles();
        // ????????????????????????
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<Role> list = roleService.getListBySortOk(sort);

        model.addAttribute("id", user.getId());
        model.addAttribute("list", list);
        model.addAttribute("authRoles", authRoles);
        return "/system/user/role";
    }

    /**
     * ????????????????????????
     */
    @PostMapping("/role")
    @RequiresPermissions("system:user:role")
    @ResponseBody
    @ActionLog(key = UserAction.EDIT_ROLE, action = UserAction.class)
    public ResultVo auth(
            @RequestParam(value = "id", required = true) User user,
            @RequestParam(value = "roleId", required = false) HashSet<Role> roles) {

        // ????????????????????????????????????
        if (user.getId().equals(AdminConst.ADMIN_ID) &&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

        // ??????????????????
        user.setRoles(roles);

        // ????????????
        userService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * ??????????????????
     */
    @GetMapping("/picture")
    public void picture(String p, HttpServletResponse response) throws IOException {
        String defaultPath = "/images/user-picture.jpg";
        if (!(StringUtils.isEmpty(p) || p.equals(defaultPath))) {
            UploadProjectProperties properties = SpringContextUtil.getBean(UploadProjectProperties.class);
            String fuPath = properties.getFilePath();
            String spPath = properties.getStaticPath().replace("*", "");
            File file = new File(fuPath + p.replace(spPath, ""));
            if (file.exists()) {
                FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
                return;
            }
        }
        Resource resource = new ClassPathResource("static" + defaultPath);
        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
    }

    /**
     * ??????????????????
     */
    @GetMapping("/export")
    @RequiresPermissions("system:user:export")
    @ResponseBody
    public void exportExcel() {
        UserRepository userRepository = SpringContextUtil.getBean(UserRepository.class);
        ExcelUtil.exportExcel(User.class, userRepository.findAll());
    }

    /**
     * ???????????????????????????????????????
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:user:status")
    @ResponseBody
    @ActionLog(name = "????????????", action = StatusAction.class)
    public ResultVo updateStatus(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {

        // ?????????????????????????????????
        if (ids.contains(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_STATUS);
        }

        // ????????????
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (userService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "??????");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "????????????????????????");
        }
    }


    @GetMapping("/register")
    public String registerLogin( Model model) {
        return "/system/user/register";
    }

    /**
     * ????????????/???????????????
     * @param valid ????????????
     * @param user ????????????
     */
    @PostMapping("/register")
    @ResponseBody
    public ResultVo register(@Validated UserRegisterValid valid, @EntityParam User user, HttpServletRequest request) {
        ResultVo resultVo=new ResultVo();
        // ????????????????????????
        if (user.getId() == null) {
            // ????????????????????????
            if (user.getPassword().isEmpty() || "".equals(user.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }
            // ??????????????????????????????
            if (!user.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }
            // ?????????????????????
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(user.getPassword(), salt);
            user.setPassword(encrypt);
            user.setSalt(salt);
        }
        // ???????????????????????????
        if (userService.repeatByUsername(user)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }
        // ?????????????????????????????????
        if (user.getId() != null) {
            // ????????????????????????????????????
            if (user.getId().equals(AdminConst.ADMIN_ID) &&
                    !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }
            User beUser = userService.getById(user.getId());
            String[] fields = {"password", "salt", "picture", "roles"};
            EntityBeanUtil.copyProperties(beUser, user, fields);
        }
        Dept dept=new Dept();
        dept.setId(1L);
        user.setDept(dept);
        Role role=new Role();
        role.setId(2L);
        user.setRoles(ImmutableSet.of(role));
        user.setStatus((byte)2);
        // ????????????
        userService.save(user);
        return ResultVoUtil.success("??????????????????????????????");
    }

}
