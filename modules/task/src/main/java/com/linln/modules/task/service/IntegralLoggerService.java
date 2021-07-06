package com.linln.modules.task.service;

import com.linln.modules.task.domain.Integral;
import com.linln.modules.task.domain.IntegralLogger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

public interface IntegralLoggerService {
    /**
     * 获取积分页数据
     * @param example
     * @return
     */
    Page<IntegralLogger> getPageList(Example<IntegralLogger> example);

    /**
     * 增加积分
     * @param integralLogger
     * @return
     */
    IntegralLogger addIntegralLogger(IntegralLogger integralLogger);

}
