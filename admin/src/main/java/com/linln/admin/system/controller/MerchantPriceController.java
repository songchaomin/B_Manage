package com.linln.admin.system.controller;

import com.linln.admin.system.validator.MerchantPriceValid;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.SaveAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.User;
import com.linln.modules.task.domain.Price;
import com.linln.modules.task.service.PriceService;
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


@Controller
@RequestMapping("/merchantPrice")
public class MerchantPriceController {
    @Autowired
    private PriceService priceService;

    @GetMapping("/index")
    @RequiresPermissions("merchantPrice:index")
    public String index(Model model, Price price){
        User user = ShiroUtil.getSubject();
        // 创建匹配器，进行动态查询匹配
        price.setSystemName("B");
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("price", match -> match.contains())
                .withMatcher("systemName",match->match.contains());

        Example<Price> example = Example.of(price, matcher);
        Page<Price> list = priceService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/merchantPrice/index";
    }

    @GetMapping("/delete/{id}")
    @RequiresPermissions("merchantPrice:delete")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") Long id, Model model){
        priceService.deleteMerchantPriceById(id);
        return  ResultVoUtil.success("删除成功");
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResultVo getRobTask(@PathVariable("id") Long id, Model model){
        ResultVo resultVo=new ResultVo();
        Price price = priceService.getMerchantPriceById(id);
        resultVo.setCode(200);
        resultVo.setMsg("查询成功");
        resultVo.setData(price);
        return  resultVo;
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("merchantPrice:add")
    public String toAdd(){
        return "/merchantPrice/add";
    }


    @PostMapping({"/add"})
    @RequiresPermissions({"merchantPrice:add"})
    @ResponseBody
    @ActionLog(name = "店铺管理", message = "店铺：${userName}", action = SaveAction.class)
    public ResultVo save(@Validated MerchantPriceValid valid, @EntityParam Price price){

        price.setDeleteFlg((byte)0);
        price.setCreateDate(new Date());

        priceService.save(price);
        return ResultVoUtil.SAVE_SUCCESS;
    }



    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("merchantPrice:edit")
    public String toEdit(@PathVariable("id") Long id, Model model){
        Price price = priceService.getMerchantPriceById(id);
        model.addAttribute("item", price);
        return "/merchantPrice/edit";
    }


    @GetMapping("/detail/{id}")
    @RequiresPermissions("merchantPrice:detail")
    public String detail(@PathVariable("id") Long id, Model model) {
        Price price = priceService.getMerchantPriceById(id);
        model.addAttribute("item", price);
        return "/merchantPrice/detail";
    }


}
