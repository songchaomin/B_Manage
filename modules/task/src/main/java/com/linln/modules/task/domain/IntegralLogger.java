package com.linln.modules.task.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 积分实体
 */
@Data
@Entity
@Table(name = "integral_logger")
public class IntegralLogger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 商户名称
     */
    private String merchantName;

    /**
     *
     * 业务类型:1:积分管理    2：任务审核    3：任务删除
     *
     */
    private String businessType;

    /**
     * 操作类型：1：增加  2：减少
     */
    private String operatorType;
    /**
     * 积分数
     */
    private Integer point;

    /**
     * 创建时间
     */
    private Date createDate;


    /**
     * 删除标记
     */
    private Byte deleteFlg;


    /**
     * 操作人
     */
    private String operatorName;
}
