package com.linln.modules.task.repository;

import com.linln.modules.task.domain.Task;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends BaseRepository<Task,Long>, JpaSpecificationExecutor<Task> {


}
