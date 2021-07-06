package com.linln.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class TaskValid implements Serializable {
    /**
     * 任务名称
     */
    @NotEmpty(message = "任务名称不能为空")
    private String taskName;
    /**
     * 任务类型
     */
    @NotEmpty(message = "任务类型不能为空")
    private String taskType;
    /**
     *店铺名称
     */
    @NotEmpty(message = "店铺名称不能为空")
    private String shopName;

    /**
     * 宝贝链接
     */
    @NotEmpty(message = "宝贝链接不能为空")
    private String babyLink;

    /**
     * 宝贝关键字
     */
    @NotEmpty(message = "宝贝关键字不能为空")
    private String babyKey;
    /**
     * 宝贝规格
     */
    @NotEmpty(message = "宝贝规格不能为空")
    private String babySpec;
    /**
     * 宝贝本金
     */
    @NotNull(message = "宝贝本金不能为空")
    private BigDecimal babyPrice;
    /**
     * 是否关注店铺
     */
    @NotNull(message = "是否关注店铺不能为空")
    private Byte attentionStoreFlag;
    /**
     * 是否收藏宝贝
     */
    @NotNull(message = "是否收藏宝贝不能为空")
    private Byte collectBabyFlag;
    /**
     * 宝贝图片
     */
    @NotEmpty(message = "宝贝图片不能为空")
    private String babyPic;
    /**
     * 任务人数
     */
    @NotNull(message = "任务人数不能为空")
    @Min(value = 1,message = "人数不能小于0")
    private Integer personNum;

    /**
     * 任务标签-性别
     */
    @NotEmpty(message = "性别不能为空")
    private  String sex;
    /**
     * 任务标签-年龄
     */
    @NotEmpty(message = "年龄不能为空")
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
    @NotEmpty(message = "子女年龄不能为空")
    private String childAge;
    /**
     * 收入区间
     */
    @NotEmpty(message = "收入区间不能为空")
    private String incomeRange;
    /**
     * 身高区间'
     */
    @NotEmpty(message = "身高区间不能为空")
    private String height;
    /**
     * 体重区间'
     */
    @NotEmpty(message = "体重区间不能为空")
    private String weight;
    /**
     * 学历'
     */
    @NotEmpty(message = "淘宝常规分类不能为空")
    private String xl;
    /**
     * 手机品牌'
     */
    @NotEmpty(message = "手机品牌不能为空")
    private String phoneBrand;
    /**
     * 是否有车1'
     */
    @NotEmpty(message = "是否有车不能为空")
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
     * 是否活动
     */
    private Byte effective;
    /**
     * 是否删除
     */
    private Byte deleteFlg;

    /**
     * 用户编号
     * */
    private String userName;

    /**
     * 任务状态 1:审核中，2：
     */
    private Byte taskStatus;

}
