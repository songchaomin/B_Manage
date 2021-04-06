package com.linln.modules.task.repository;

import com.linln.modules.task.domain.Shop;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ShopRepository extends BaseRepository<Shop,Long>, JpaSpecificationExecutor<Shop> {


    @Query(value = "select * from shop where user_name=?1 and shop_name=?2",nativeQuery = true)
    Shop repeateShopName(String userName, String shopName);


}
