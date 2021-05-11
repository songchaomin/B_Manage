package com.linln.admin.system.controller;

import com.linln.admin.system.validator.ShopValid;
import com.linln.common.enums.ResultEnum;
import com.linln.common.exception.ResultException;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.SaveAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.User;
import com.linln.modules.task.domain.Integral;
import com.linln.modules.task.domain.Shop;
import com.linln.modules.task.service.ShopService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.Subject;
import java.util.Date;
import java.util.Objects;

@Controller
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @GetMapping("/index")
    @RequiresPermissions("shop:index")
    public String index(Model model, Shop shop){
        User user = ShiroUtil.getSubject();
        if (!Objects.equals(user.getUsername(),"admin")) {
            shop.setUserName(user.getUsername());
        }
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("shopName", match -> match.contains());

        Example<Shop> example = Example.of(shop, matcher);
        Page<Shop> list = shopService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/shop/index";
    }

    @GetMapping("/popShopList")
    @RequiresPermissions("shop:popShopList")
    public String popShopList(Model model, Shop shop){
        User user = ShiroUtil.getSubject();
        if (!Objects.equals(user.getUsername(),"admin")) {
            shop.setUserName(user.getUsername());
        }
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("shopName", match -> match.contains());

        Example<Shop> example = Example.of(shop, matcher);
        Page<Shop> list = shopService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/task/shop_pop";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("shop:add")
    public String toAdd(){
        return "/shop/add";
    }



    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add"})
    @RequiresPermissions({"shop:add"})
    @ResponseBody
    @ActionLog(name = "店铺管理", message = "店铺：${userName}", action = SaveAction.class)
    public ResultVo save(@Validated ShopValid valid, @EntityParam Shop shop){
        //1、店铺名称是否重复
        if ( shopService.repeateShopName(shop.getUserName(),shop.getShopName())) {
            throw new ResultException(ResultEnum.SHOP_SHOPNAME_ERROR);
        }
        //2、保存店铺
        shop.setCreateDate(new Date());
        shop.setUpdateDate(new Date());
        shop.setDeleteFlg((byte)0);
        shopService.save(shop);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("shop:detail")
    public String toDetail(@PathVariable("id") Long id, Model model){
        Shop shop=shopService.getShopById(id);
        String [] shopPics=shop.getShopPic()==null ? new String[0] :shop.getShopPic().split(",");
        model.addAttribute("shopPics",shopPics);
        model.addAttribute("shop",shop);
        return "/shop/detail";
    }


    @GetMapping("/delete/{id}")
    @RequiresPermissions("shop:delete")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") Long id, Model model){
        shopService.deleteById(id);
        return  ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("shop:edit")
    public String toEdit(@PathVariable("id") Long id, Model model){
        Shop shop = shopService.getShopById(id);
        String [] shopPics=shop.getShopPic()==null ? new String[0] :shop.getShopPic().split(",");
        model.addAttribute("shopPics",shopPics);
        model.addAttribute("shop",shop);
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
    public ResultVo updateShop(@Validated ShopValid valid, @EntityParam Shop shop){
        //2、更新店铺
        shop.setUpdateDate(new Date());
        shopService.save(shop);
        return ResultVoUtil.SAVE_SUCCESS;
    }

}
