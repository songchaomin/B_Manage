package com.linln.admin.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.linln.admin.system.config.YamlPropertySourceFactory;
import com.linln.admin.system.utils.HttpClientUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.User;
import com.linln.modules.task.domain.Price;
import com.linln.modules.task.domain.RobTask;
import com.linln.modules.task.domain.Task;
import com.linln.modules.task.service.PriceService;
import com.linln.modules.task.service.RobTaskService;
import com.linln.modules.task.service.TaskService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/robTask")
@PropertySource(value = {"classpath:application.yml"},factory = YamlPropertySourceFactory.class)
public class RobTaskController {
    @Value("${addCustomIntegralUrl}")
    private  String addCustomIntegralUrl;

    @Autowired
    private RobTaskService robTaskService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/delete/{id}")
    @RequiresPermissions("robTask:delete")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") Long id, Model model){
        robTaskService.delRobTaskById(id);
        return  ResultVoUtil.success("删除成功");
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResultVo getRobTask(@PathVariable("id") Long id, Model model){
        ResultVo resultVo=new ResultVo();
        RobTask robTaskById = robTaskService.getRobTaskById(id);
        String payPicUrl = robTaskById.getPayPicUrl();
        if (!StringUtils.isEmpty(payPicUrl)){
            ArrayList<String> urls=new ArrayList<>();
            String[] split = payPicUrl.split(",");
            for(String s:split){
                urls.add(s);
            }
            resultVo.setData(urls);
        }
        resultVo.setCode(200);
        resultVo.setMsg("查询成功");
        return  resultVo;
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
        ResultVo resultVo=new ResultVo();
        User subject = ShiroUtil.getSubject();
        //发货成功，增加C端用户的积分
        RobTask oldRobTask = robTaskService.getRobTaskById(id);
        if (oldRobTask==null){
            resultVo.setCode(-1);
            resultVo.setMsg("没有有效的抢单任务，发货失败");
        }
        //获取历史任务
        Task oldTask = taskService.getTaskById(oldRobTask.getTaskId());
        String taskType = oldTask.getTaskType();//类型
        BigDecimal babyPrice = oldTask.getBabyPrice();//本金
        List<Price> customerPriceByPrice = priceService.getCustomerPriceByPrice(babyPrice.intValue(), taskType);
        //计算积分数
        if(!CollectionUtils.isEmpty(customerPriceByPrice)){
            Price price = customerPriceByPrice.get(0);
            int totalIntegral=babyPrice.intValue()+price.getPrice();
            //调用C端接口
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("price",totalIntegral);
            jsonObject.put("userName",oldRobTask.getCUserName());
            jsonObject.put("operator",subject.getUsername());
            try {
                HttpClientUtil.doPost(addCustomIntegralUrl, jsonObject.toJSONString());
            } catch (Exception e) {
                resultVo.setCode(-1);
                resultVo.setMsg("调用更新C端用户积分时，远程服务器没有响应，发货失败");
            }
        }else{
            resultVo.setCode(-1);
            resultVo.setMsg("C端积分增加失败，请确认C端价格管理有该对应的价格");
        }
        robTaskService.changeRobTaskStatus(id,6);
        return  ResultVoUtil.success("发货成功");
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
