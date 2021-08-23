package com.linln.modules.task.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.linln.common.vo.ResultVo;
import com.linln.modules.task.domain.RobTask;
import com.linln.modules.task.repository.RobTaskRepository;
import com.linln.modules.task.service.RobTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.List;

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
        //确认完成增加C端用户的积分

        robTaskRepository.changeRobTaskStatus(id,i);
    }

    @Override
    public int updateRobTask(String robTask) {
        RobTask newRobtask = JSONObject.parseObject(robTask, RobTask.class);
        return robTaskRepository.updateRobTask(newRobtask.getTaskId(),newRobtask.getPayPicUrl(),5);
    }

    @Override
    public RobTask getRobTaskById(Long id) {
        return robTaskRepository.getOne(id);
    }

    @Override
    public int getRobTaskByTaskId(Long taskId) {
        return robTaskRepository.getRobTaskByTaskId(taskId);
    }
}
