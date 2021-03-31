package com.linln.admin.system.domain;

import com.linln.component.excel.annotation.Excel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
@Component
public class UserCenter implements Serializable {
    private Long id;
    private String username;
    private String nickname;
    private String picture;
    @Excel(value = "性别", dict = "USER_SEX")
    private Byte sex;
    @Excel("手机号码")
    private String phone;
    @Excel("电子邮箱")
    private String email;
    @Excel("创建时间")
    private Date createDate;
    @Excel("更新时间")
    private Date updateDate;
    @Excel("备注")
    private String remark;
    @Excel(value = "状态", dict = "DATA_STATUS")
    private Byte status;
    private Long age;// '年龄'
    @Column(name="id_card_pic")
    private String idCardPic; // '身份证号码图片',
    private String bankCardPic;// '提现银行卡图片',
    private String payPic;//  '支付宝图片',
    private String wangwangId;//  '旺旺ID',
    private String taobaoPic; //  '我的淘宝页面截图',
    private String receveAddress; //  '常用收货地址',
    private Byte shopInfo; // '店铺信息1：是2：否',
    private String shppingXyPic; //   '买家信誉（买家信誉截图）',
    private Byte marryInfo; // '婚姻状态1：是2：否',
    private String childAgeRange; // '子女年龄区间',
    private String chargeRange; // '月收入区间',
    private String heightRange; //'身高区间',
    private String weightRange; // '体重区间',
    private String xl; //'学历',
    private String phoneBrand; // '手机品牌',
    private Byte carInfo; // '是否有车1：是2：否',
    private String tgLink ;
    private Long pid ;
    private String roleName;
    //推广数量
    private int tgNum;

}
