package com.linln.modules.task.service;

import com.linln.modules.task.domain.Price;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface PriceService {

    /**
     * 获取店铺分页数据
     * @param example
     * @return
     */
    Page<Price> getPageList(Example<Price> example);

    /**
     * 保存任务
     * @return
     */
    Price save(Price price);


    void deleteMerchantPriceById(Long id);

    Price getMerchantPriceById(Long id);

    List<Price> getMerchantPriceByPrice(BigDecimal price, String priceType);

    List<Price> getCustomerPriceByPrice(BigDecimal price, String priceType);


    int update(Price price);
}
