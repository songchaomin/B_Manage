package com.linln.modules.task.service;

import com.linln.common.vo.ResultVo;
import com.linln.modules.task.domain.RobTask;
import com.linln.modules.task.domain.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

public interface TaskService {

    /**
     * 获取店铺分页数据
     * @param example
     * @return
     */
    Page<Task> getPageList(Example<Task> example);
    
    Page<RobTask> merchantGetRobTaskList(Example<RobTask> example);

    /**
     * 保存任务
     * @param task
     * @return
     */
    Task save(Task task);

    boolean repeateTaskName(String userName, String taskName);

    void deleteTaskById(Long id);

    Task getTaskById(Long id);


    void auditTaskById(Long id);

    void unAuditTaskById(Long id);

    Page<Task> getPageList2C(String cUser);

    ResultVo robOrder(String task);

    Page<RobTask> getRobTaskByUserName(String userName,int page,int limit);

    void updateStatus(Long id, int i);
}
