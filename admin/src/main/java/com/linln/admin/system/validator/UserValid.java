package com.linln.admin.system.validator;

import com.linln.modules.system.domain.Dept;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.annotation.RegEx;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class UserValid implements Serializable {
    @NotEmpty(message = "手机不能为空")
    @Length(min = 11, max = 11, message = "手机号只能为11位")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String username;

    @NotEmpty(message = "用户昵称不能为空")
    @Size(min = 2, message = "用户昵称：请输入至少2个字符")
    private String nickname;

    private String confirm;


}
