package com.linln.modules.task.service.impl;

import com.linln.common.data.PageSort;
import com.linln.modules.task.domain.*;
import com.linln.modules.task.repository.PriceRepository;
import com.linln.modules.task.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    private PriceRepository priceRepository;

    @Override
    public Page<Price> getPageList(Example<Price> example) {
        PageRequest page = PageSort.pageRequest(Sort.Direction.DESC);
        return priceRepository.findAll(example, page);
    }

    @Override
    public Price save(Price price) {
        return priceRepository.save(price);
    }

    @Override
    public void deleteMerchantPriceById(Long id) {
        priceRepository.deleteById(id);
    }

    @Override
    public Price getMerchantPriceById(Long id) {
        return priceRepository.getOne(id);
    }

    @Override
    public List<Price> getMerchantPriceByPrice(BigDecimal price, String priceType) {
        return priceRepository.getMerchantPriceByPrice(price,priceType);
    }

    @Override
    public List<Price> getCustomerPriceByPrice(BigDecimal price, String priceType) {
        return priceRepository.getCustomerPriceByPrice(price,priceType);
    }

    @Override
    public int update(Price price) {
        return priceRepository.update(price.getId(),price.getPrice()==null? BigDecimal.ZERO:price.getPrice(),price.getManagePrice()==null?BigDecimal.ZERO:price.getManagePrice());
    }
}
