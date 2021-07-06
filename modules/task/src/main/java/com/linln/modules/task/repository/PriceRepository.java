package com.linln.modules.task.repository;

import com.linln.modules.task.domain.Price;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PriceRepository extends BaseRepository<Price,Long>, JpaSpecificationExecutor<Price> {

    @Query(value = "select * from price where price_start<=?1 and price_end >?1  and price_type=?2",nativeQuery = true)
  List<Price> getMerchantPriceByPrice(int price, String priceType);



}
