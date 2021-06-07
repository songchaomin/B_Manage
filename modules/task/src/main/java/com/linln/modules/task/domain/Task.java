package com.linln.modules.task.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "task")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     *店铺名称
     */
    private String shopName;

    /**
     * 宝贝链接
     */
    private String babyLink;

    /**
     * 宝贝关键字
     */
    private String babyKey;
    /**
     * 宝贝规格
     */
    private String babySpec;
    /**
     * 宝贝本金
     */
    private BigDecimal babyPrice;
    /**
     * 是否关注店铺
     */
    private Byte attentionStoreFlag;
    /**
     * 是否收藏宝贝
     */
    private Byte collectBabyFlag;
    /**
     * 宝贝图片
     */
    private String babyPic;
    /**
     * 任务人数
     */
    private Integer personNum;

    /**
     * 任务标签-性别
     */
    private  String sex;
    /**
     * 任务标签-年龄
     */
    private  String age;
    /**
     * 任务标签-地区
     */
    private String area;
    /**
     * 任务标签-婚姻状态
     */
    private String marryStatus;
    /**
     * 子女年龄
     */
    private String childAge;
    /**
     * 收入区间
     */
    private String incomeRange;
    /**
     * 身高区间'
     */
    private String height;
    /**
     * 身高区间'
     */
    private String weight;
    /**
     * 学历'
     */
    private String xl;
    /**
     * 手机品牌'
     */
    private String phoneBrand;
    /**
     * 是否有车1'
     */
    private String carInfo;
    /**
     * 星座'
     */
    private String constellation;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 是否删除
     */
    private Byte deleteFlg;

    /**
     * 商户编号
     * */
    private String merchantName;
    private Long merchantId;


    /**
     * 任务状态 1:待审核 2:已审核 3:进行中 4：待付款 5：已付款 6：已发货 7：已完成
     */
    private Byte taskStatus;


    /**
     * 发布状态  0:待发布 1：已发布
     */
    private Byte effective;

}
