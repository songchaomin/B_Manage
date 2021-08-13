package com.linln.modules.task.repository;

import com.linln.modules.task.domain.Price;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface PriceRepository extends BaseRepository<Price,Long>, JpaSpecificationExecutor<Price> {

    @Query(value = "select * from price where price_start<=?1 and price_end >?1  and price_type=?2 and system_name='B'",nativeQuery = true)
  List<Price> getMerchantPriceByPrice(int price, String priceType);


    @Query(value = "select * from price where price_start<=?1 and price_end >?1  and price_type=?2 and system_name='C'",nativeQuery = true)
    List<Price> getCustomerPriceByPrice(int price, String priceType);

    @Modifying
    @Transactional
    @Query(value = "update  price set price= ?2 ,manage_price=?3 where id=?1 ",nativeQuery = true)
    int update(long id, BigDecimal price, BigDecimal managePrice);
}
