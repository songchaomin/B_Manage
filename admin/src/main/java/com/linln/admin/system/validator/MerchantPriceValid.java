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
public class MerchantPriceValid implements Serializable {
    @NotEmpty(message = "价格类型不能为空")
    private String priceType;

    /**
     * 起始价格
     */
    @NotNull(message = "起始价格不能为空")
    private Integer priceStart;

    /**
     * 结束价格
     */
    @NotNull(message = "结束价格不能为空")
    private Integer priceEnd;

    /**
     * price
     */
    @NotNull(message = "价格不能为空")
    private BigDecimal price;


}
