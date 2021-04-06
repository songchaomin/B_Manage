package com.linln.modules.task.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * 店铺管理
 */
@Data
@Entity
@Table(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 绑定的商户
     */
    private String userName;
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

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 删除标记
     */
    private Byte deleteFlg;

}
