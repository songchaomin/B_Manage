package com.linln.modules.task.service;

import com.linln.modules.task.domain.Shop;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

public interface ShopService {
    /**
     * 获取店铺分页数据
     * @param example
     * @return
     */
    Page<Shop> getPageList(Example<Shop> example);

    /**
     * 保存店铺
     * @param shop
     * @return
     */
    Shop save(Shop shop);

    boolean repeateShopName(String userName, String shopName);

    void deleteById(Long id);

    Shop getShopById(Long id);


}
