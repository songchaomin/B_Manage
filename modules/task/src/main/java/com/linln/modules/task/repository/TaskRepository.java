package com.linln.modules.task.repository;

import com.linln.modules.task.domain.Task;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends BaseRepository<Task,Long>, JpaSpecificationExecutor<Task> {
    @Query(value = "select * from task where user_name=?1 and task_name=?2 and delete_flg=0",nativeQuery = true)
    Task repeateTaskName(String userName, String taskName);


    @Modifying
    @Transactional
    @Query(value = "update  task set task_status= 2  where id=?1 and delete_flg=0",nativeQuery = true)
    void auditTaskById(Long id);


}
