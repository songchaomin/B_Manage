package com.linln.admin.system.validator;

import com.linln.modules.system.domain.Dept;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class UserRegisterValid implements Serializable {
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "确认密码不能为空")
    private String confirm;

    @NotEmpty(message = "用户名称不能为空")
    private String nickname;

    @NotEmpty(message = "手机号码不能为空")
    private String phone;

    @NotNull(message = "性别不能为空")
    private Byte sex;

    @NotEmpty(message = "生日不能为空")
    private String birthday;

    @NotNull(message = "QQ号不能为空")
    private Long qq;

    @NotEmpty(message = "旺旺ID不能为空")
    private String wangwangId;//  '旺旺ID',

    @NotNull(message = "婚姻状态不能为空")
    private Byte marryInfo; // '婚姻状态1：是2：否',

    @NotEmpty(message = "子女年龄区间不能为空")
    private String childAgeRange; // '子女年龄区间',

    @NotEmpty(message = "月收入区间不能为空")
    private String chargeRange; // '月收入区间',

    @NotEmpty(message = "身高区间不能为空")
    private String heightRange; //'身高区间',

    @NotEmpty(message = "体重区间不能为空")
    private String weightRange; // '体重区间',

    @NotEmpty(message = "学历不能为空")
    private String xl; //'学历',

    @NotEmpty(message = "手机品牌不能为空")
    private String phoneBrand; // '手机品牌',

    @NotNull(message = "是否有车不能为空")
    private Byte carInfo; // '是否有车1：是2：否',

    @NotEmpty(message = "常用收货地址不能为空")
    private String receveAddress; //  '常用收货地址',

    @NotEmpty(message = "身份证号码图片不能为空")
    private String idCardPic; // '身份证号码图片',

    @NotEmpty(message = "提现银行卡图片不能为空")
    private String bankCardPic;// '提现银行卡图片',

    @NotEmpty(message = "支付宝图片不能为空")
    private String payPic;//  '支付宝图片',

    @NotEmpty(message = "我的淘宝页面截图址不能为空")
    private String taobaoPic; //  '我的淘宝页面截图',

    @NotEmpty(message = "常买家信誉不能为空")
    private String shppingXyPic; //   '买家信誉（买家信誉截图）',

    @NotEmpty(message = "常用收货地址图片不能为空")
    private String receveAddressPic; //  '常用收货地址图片',
}
