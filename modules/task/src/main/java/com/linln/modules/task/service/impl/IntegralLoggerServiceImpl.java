package com.linln.modules.task.service.impl;

import com.linln.common.data.PageSort;
import com.linln.modules.task.domain.Integral;
import com.linln.modules.task.domain.IntegralLogger;
import com.linln.modules.task.repository.IntegralLoggerRepository;
import com.linln.modules.task.repository.IntegralRepository;
import com.linln.modules.task.service.IntegralLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class IntegralLoggerServiceImpl implements IntegralLoggerService {
    @Autowired
    private IntegralLoggerRepository integralLoggerRepository;
    @Override
    public Page<IntegralLogger> getPageList(Example<IntegralLogger> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);
        return integralLoggerRepository.findAll(example, page);
    }

    @Override
    public IntegralLogger addIntegralLogger(IntegralLogger integralLogger) {
        return integralLoggerRepository.save(integralLogger);
    }
}
