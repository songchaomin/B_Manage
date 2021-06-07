package com.linln.modules.task.service;

import com.linln.common.vo.ResultVo;
import com.linln.modules.task.domain.RobTask;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

public interface RobTaskService {

    Page<RobTask> merchantGetRobTaskList(Example<RobTask> example);

    Page<RobTask> getRobTaskByUserName(String userName, int page, int limit);

    ResultVo robOrder(String task);

    void delRobTaskById(Long id);

    void changeRobTaskStatus(Long id, int i);
}
