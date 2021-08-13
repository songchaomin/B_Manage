package com.linln.modules.task.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家价目表
 */
@Data
@Entity
@Table(name = "price")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 系统名称
     */
    private String systemName;


    /**
     * 类型ID 1：淘宝 2 京东  3 拼多多
     */
    private Integer priceTypeId;
    /**
     * 价格类型 淘宝，京东，拼多多
     */
    private String priceType;

    /**
     * 起始价格
     */
    private Integer priceStart;

    /**
     * 结束价格
     */
    private Integer priceEnd;

    /**
     * price
     */
    private BigDecimal price;

    private BigDecimal managePrice;

    /**
     * 删除标记
     */
    private Byte deleteFlg;


    private Date createDate;

}
