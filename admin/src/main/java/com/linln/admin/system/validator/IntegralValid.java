package com.linln.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class IntegralValid implements Serializable {
    @NotEmpty(message = "商户编号不能为空")
    private String userName;
    @NotEmpty(message = "积分不能为空")
    private String point;
}
