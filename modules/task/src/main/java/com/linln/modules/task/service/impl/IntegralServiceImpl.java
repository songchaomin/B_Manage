package com.linln.modules.task.service.impl;

import com.linln.common.data.PageSort;
import com.linln.modules.task.domain.Integral;
import com.linln.modules.task.repository.IntegralRepository;
import com.linln.modules.task.service.IntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class IntegralServiceImpl implements IntegralService {
    @Autowired
    private IntegralRepository integralRepository;
    @Override
    public Page<Integral> getPageList(Example<Integral> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.DESC);
        return integralRepository.findAll(example, page);
    }

    @Override
    public Integral save(Integral integral) {
        return integralRepository.save(integral);
    }

    @Override
    public boolean repeatByUserName(String userName) {
        return integralRepository.getIntegralByUserName(userName)!=null;
    }

    @Override
    public Integer deleteIntegral(Long id) {
        return integralRepository.deleteIntegral(id);
    }

    @Override
    public Integral getIntegralById(Long id) {
        return integralRepository.getOne(id);
    }

    @Override
    public Integer update(Integral integral) {
        return integralRepository.updatePoint(integral.getUserName(),integral.getPoint());
    }

    @Override
    public Integral getIntegralByUserName(String username) {
        return integralRepository.getIntegralByUserName(username);
    }

    /**
     * 积分增减
     * @param point
     */
    @Override
    public void addIntegral(int point,String userName) {
        integralRepository.addIntegral(point,userName);
    }
}
