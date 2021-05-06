package com.linln.modules.task.service;

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

    /**
     * 保存任务
     * @param task
     * @return
     */
    Task save(Task task);

    boolean repeateTaskName(String userName, String taskName);

    void deleteTaskById(Long id);

    Task getTaskById(Long id);


}
