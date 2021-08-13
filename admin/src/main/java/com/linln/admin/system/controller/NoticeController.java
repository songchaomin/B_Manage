package com.linln.admin.system.controller;

import com.linln.admin.system.validator.NoticeValid;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.modules.notice.domain.Notice;
import com.linln.modules.notice.service.NoticeService;
import com.linln.modules.task.domain.Integral;
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
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/index")
    @RequiresPermissions("notice:index")
    public String index(Model model, Notice notice){
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("title", match -> match.contains());
        Example<Notice> example = Example.of(notice, matcher);
        Page<Notice> list = noticeService.getPageList(example);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/notice/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("notice:add")
    public String toAdd(){
        return "/notice/add";
    }

    /**
     * 查看页面
     */
    @GetMapping("/detail/{id}")
    public String toEdit(@PathVariable("id") Long id, Model model){
        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "/notice/detail";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add"})
    @RequiresPermissions({"notice:add"})
    @ResponseBody
    public ResultVo save(@Validated NoticeValid valid, @EntityParam Notice notice){
        notice.setCreateDate(new Date());
        noticeService.save(notice);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @ResponseBody
    @PostMapping("/edit")
    @RequiresPermissions("integral:edit")
    public ResultVo toDetail(@Validated NoticeValid valid, @EntityParam Integral integral){
        integral.setUpdateDate(new Date());
        //integralService.update(integral);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    @GetMapping("/delete/{id}")
    @RequiresPermissions("notice:delete")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") Long id, Model model){
       noticeService.deleteNotice(id);
        return  ResultVoUtil.SAVE_SUCCESS;
    }

}
