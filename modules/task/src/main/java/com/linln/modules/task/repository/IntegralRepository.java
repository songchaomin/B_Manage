package com.linln.modules.task.repository;

import com.linln.modules.task.domain.Integral;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IntegralRepository extends BaseRepository<Integral,Long>, JpaSpecificationExecutor<Integral> {

    /**
     * 根据用户名更新积分
     * @param userName
     * @return
     */
    @Transactional
    @Modifying
    @Query(value="update integral set point =:point where user_name=:userName",nativeQuery = true)
    Integer updatePoint(String userName,Integer point);


    @Query(value="select * from integral where user_name=?1 and delete_flg=0",nativeQuery = true)
    Integral getIntegralByUserName(String userName);

    @Transactional
    @Modifying
    @Query(value = "update integral set delete_flg=1 where id=?1",nativeQuery = true)
    Integer deleteIntegral(Long id);

}
