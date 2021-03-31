package com.linln.modules.task.service;

import com.linln.modules.task.domain.Task;
import org.springframework.data.domain.Page;

public interface TaskService {

    /**
     * 任务列表
     * @param task
     * @return
     */
    Page<Task> getPageList(Task task);

    /**
     * 保存任务
     * @param task 用户实体类
     * @return 任务信息
     */
    Task save(Task task);


}
