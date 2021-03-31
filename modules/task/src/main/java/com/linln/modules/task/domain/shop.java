package com.linln.modules.task.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 店铺管理
 */
@Data
@Entity
@Table(name = "shop")
public class shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺掌柜
     */
    private String shopManage;
    /**
     * 发货人
     */
    private String deliverName;
    /**
     * 发货电话
     */
    private String deliverPhone;
    /**
     * 发货地址
     */
    private String deliverAddr;
    /**
     * 店铺图片
     */
    private String shopPic;

}
