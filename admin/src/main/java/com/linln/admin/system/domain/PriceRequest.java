package com.linln.admin.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家价目表
 */
@Data
public class PriceRequest {
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
    private String price;

    private String managePrice;

}
