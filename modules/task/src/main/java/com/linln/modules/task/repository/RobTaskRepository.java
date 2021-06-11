package com.linln.modules.task.repository;

import com.linln.modules.task.domain.RobTask;
import com.linln.modules.task.domain.Task;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RobTaskRepository extends BaseRepository<RobTask,Long>, JpaSpecificationExecutor<RobTask> {

    @Query(value = "select count(1)  from rob_task  where task_id=?1 ",nativeQuery = true)
    int queryRobTaskByTaskId(long taskId);

    @Query(value = "select count(1)  from rob_task  where c_user_id=?1 and task_id=?2",nativeQuery = true)
    int queryRobTaskReapter(Long cUserId, Long id);

    @Query(value = "select * from rob_task where user_name=?1 ",nativeQuery = true)
    Task queryTaskByUserName(String userName);

    @Modifying
    @Transactional
    @Query(value = "update  rob_task set rob_task_status= ?2  where id=?1 ",nativeQuery = true)
    void changeRobTaskStatus(Long id, int i);
    @Modifying
    @Transactional
    @Query(value = "update  rob_task set pay_pic_url=?2,rob_task_status= ?3  where id=?1 ",nativeQuery = true)
    int updateRobTask(Long id, String payPicUrl, int i);

    @Query(value = "SELECT count(1) FROM rob_task a left join task b on a.task_id=b.id where a.c_user_name=?1 and b.shop_name=?2 and DATE_ADD(a.create_date,INTERVAL ?3 day)>now()",nativeQuery = true)
    int queryTenDayRobTask(String cUserName, String shopName,int day);
}
