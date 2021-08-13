package com.linln.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class NoticeValid implements Serializable {
    @NotEmpty(message ="标题不能为空")
    private String title;
    @NotEmpty(message = "内容不能为空")
    private String content;
}
