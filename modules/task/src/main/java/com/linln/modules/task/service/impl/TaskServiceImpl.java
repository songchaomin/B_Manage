package com.linln.modules.task.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.linln.common.data.PageSort;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.modules.task.domain.CTask;
import com.linln.modules.task.domain.CUser;
import com.linln.modules.task.domain.RobTask;
import com.linln.modules.task.domain.Task;
import com.linln.modules.task.repository.RobTaskRepository;
import com.linln.modules.task.repository.TaskRepository;
import com.linln.modules.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private RobTaskRepository robTaskRepository;
    @Override
    public Page<Task> getPageList(Example<Task> example) {
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);
        return taskRepository.findAll(example, page);
    }


    @Override
    public Page<RobTask> merchantGetRobTaskList(Example<RobTask> example) {
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);
        return robTaskRepository.findAll(example, page);
    }




    @Override
    public Task save(Task task) {
        //保存
        return taskRepository.save(task);
    }

    @Override
    public boolean repeateTaskName(String userName, String taskName) {
        return taskRepository.repeateTaskName(userName,taskName)!=null;
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.getOne(id);
    }

    @Override
    public void auditTaskById(Long id) {
        taskRepository.auditTaskById(id);
    }

    @Override
    public Page<Task> getPageList2C(String cUser) {
        CUser user = JSONObject.parseObject(cUser, CUser.class);
        PageRequest page =PageRequest.of(user.page-1,user.limit);
        return taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(user.getChildAgeRange() != null){
                    preList.add(cb.like(root.get("childAge").as(String.class), "%"+user.getChildAgeRange()+ "%"));
                }
                if(user.getChargeRange() != null){
                    preList.add(cb.like(root.get("incomeRange").as(String.class), "%"+user.getChargeRange()+ "%"));
                }
                if(user.getHeightRange() != null){
                    preList.add(cb.like(root.get("height").as(String.class), "%"+ user.getHeightRange() + "%"));
                }
                List<Predicate> preListand = new ArrayList<>();
                preListand.add(cb.greaterThanOrEqualTo(root.get("taskStatus").as(Integer.class),3));
                Predicate[] pres = new Predicate[preList.size()];
                Predicate or = cb.or(preList.toArray(pres));

                Predicate[] presand = new Predicate[preListand.size()];
                Predicate and = cb.and(preListand.toArray(presand));
                return query.where(or,and).getRestriction();
            }
        }, page);
    }

    @Override
    public Page<RobTask> getRobTaskByUserName(String userName,int page,int limit) {
        PageRequest pageRequest =PageRequest.of(page-1,limit);
        return robTaskRepository.findAll(new Specification<RobTask>() {
            @Override
            public Predicate toPredicate(Root<RobTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(userName != null){
                    preList.add(cb.equal(root.get("cUserName").as(String.class), userName));
                }
                Predicate[] presand = new Predicate[preList.size()];
                Predicate and = cb.and(preList.toArray(presand));
                return query.where(and).getRestriction();
            }
        }, pageRequest);
    }

    @Override
    public ResultVo robOrder(String task) {
        ResultVo resultVo=new ResultVo();
        CTask cTask = JSONObject.parseObject(task, CTask.class);
        //判断是否超过最大抢单数量
        Optional<Task> optionalTask = taskRepository.findById(cTask.getId());
        Task oldtask = optionalTask.get();
        Integer personNum = oldtask.getPersonNum();
        int robTaskCount = robTaskRepository.queryRobTaskByTaskId(cTask.getId());
        if (personNum<robTaskCount+1){
            resultVo.setCode(0);
            resultVo.setMsg("抢单人数大于任务人数,抢单失败");
            return resultVo;
        }
        //判断是否同一个人抢单
        int reapterCount=robTaskRepository.queryRobTaskReapter(cTask.getCUserId(),cTask.getId());
        if (reapterCount>=1){
            resultVo.setCode(0);
            resultVo.setMsg("同一个任务只允许抢购一次！");
            return resultVo;
        }
        RobTask robTask=new RobTask();
        robTask.setTaskId(cTask.getId());
        robTask.setTaskName(cTask.getTaskName());
        robTask.setTaskType(cTask.getTaskType());
        robTask.setCUserId(cTask.getCUserId());
        robTask.setCUserName(cTask.getCUserName());
        robTask.setCNickName(cTask.getCNickName());
        robTask.setCreateDate(new Date());
        robTask.setWangwangId(cTask.getWangwangId());
        robTask.setQq(cTask.getQq());
        robTask.setRobTaskStatus("4");
        robTask.setMerchantId(cTask.getMerchantId());
        robTask.setMerchantName(cTask.getMerchantName());
        robTaskRepository.save(robTask);
        return ResultVoUtil.success("抢单成功！");
    }
}
