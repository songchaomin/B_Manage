package com.linln.modules.task.service;

import com.linln.modules.task.domain.Integral;
import com.linln.modules.task.domain.Shop;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

public interface IntegralService {
    /**
     * 获取积分页数据
     * @param example
     * @return
     */
    Page<Integral> getPageList(Example<Integral> example);


    /**
     * 保存积分
     * @param integral
     * @return
     */
    Integral save(Integral integral);

    boolean repeatByUserName(String userName);

    Integer deleteIntegral(Long id);

    Integral getIntegralById(Long id);

    Integer update(Integral integral);

    Integral getIntegralByUserName(String username);
}
