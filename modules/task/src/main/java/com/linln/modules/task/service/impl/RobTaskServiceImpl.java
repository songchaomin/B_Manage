package com.linln.modules.task.service.impl;

import com.linln.common.vo.ResultVo;
import com.linln.modules.task.domain.RobTask;
import com.linln.modules.task.repository.RobTaskRepository;
import com.linln.modules.task.service.RobTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;

@Service
public class RobTaskServiceImpl implements RobTaskService {
    @Autowired
    private RobTaskRepository robTaskRepository;

    @Override
    public Page<RobTask> merchantGetRobTaskList(Example<RobTask> example) {
        return null;
    }

    @Override
    public Page<RobTask> getRobTaskByUserName(String userName, int page, int limit) {
        return null;
    }

    @Override
    public ResultVo robOrder(String task) {
        return null;
    }

    @Override
    public void delRobTaskById(Long id) {
        robTaskRepository.deleteById(id);
    }

    @Override
    public void changeRobTaskStatus(Long id, int i) {
        robTaskRepository.changeRobTaskStatus(id,i);
    }
}