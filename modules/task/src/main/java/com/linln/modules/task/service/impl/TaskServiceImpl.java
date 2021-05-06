package com.linln.modules.task.service.impl;

import com.linln.common.data.PageSort;
import com.linln.modules.task.domain.Task;
import com.linln.modules.task.repository.TaskRepository;
import com.linln.modules.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public Page<Task> getPageList(Example<Task> example) {
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);
        return taskRepository.findAll(example, page);
    }

    @Override
    public Task save(Task shop) {
        return null;
    }

    @Override
    public boolean repeateTaskName(String userName, String taskName) {
        return false;
    }

    @Override
    public void deleteTaskById(Long id) {

    }

    @Override
    public Task getTaskById(Long id) {
        return null;
    }
}
