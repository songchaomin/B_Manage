package com.linln.modules.task.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 积分实体
 */
@Data
@Entity
@Table(name = "integral")
public class Integral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 商户名称
     */
    private String userName;

    /**
     * 积分数
     */
    private Integer point;

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
