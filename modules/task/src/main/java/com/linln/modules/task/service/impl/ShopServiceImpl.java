package com.linln.modules.task.service.impl;

import com.linln.common.data.PageSort;
import com.linln.modules.task.domain.Shop;
import com.linln.modules.task.repository.ShopRepository;
import com.linln.modules.task.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopRepository shopRepository;
    @Override
    public Page<Shop> getPageList(Example<Shop> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.DESC);
        return shopRepository.findAll(example, page);
    }

    @Override
    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

    @Override
    public boolean repeateShopName(String userName, String shopName) {
        return shopRepository.repeateShopName(userName,shopName)!=null;
    }

    @Override
    public void deleteById(Long id) {
         shopRepository.deleteById(id);
    }

    @Override
    public Shop getShopById(Long id) {
        return shopRepository.getOne(id);
    }

}
