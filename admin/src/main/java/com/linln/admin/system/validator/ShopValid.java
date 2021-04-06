package com.linln.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class ShopValid implements Serializable {
    /**
     * 绑定的商户
     */
    @NotEmpty(message = "商户编号不能为空")
    private String userName;

    @NotEmpty(message = "店铺名称不能为空")
    private String shopName;
    /**
     * 店铺掌柜
     */
    @NotEmpty(message = "店铺掌柜不能为空")
    private String shopManage;
    /**
     * 发货人
     */
    @NotEmpty(message = "发货人不能为空")
    private String deliverName;
    /**
     * 发货电话
     */
    @NotEmpty(message = "发货电话不能为空")
    private String deliverPhone;
    /**
     * 发货地址
     */
    @NotEmpty(message = "发货地址不能为空")
    private String deliverAddr;
    /**
     * 店铺图片
     */
    @NotEmpty(message = "店铺图片不能为空")
    private String shopPic;

}
