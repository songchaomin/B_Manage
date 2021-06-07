package com.linln.modules.task.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "rob_task")
public class RobTask implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 任务名称
     */
    private Long taskId;

    private String taskName;
    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 创建时间
     */
    private Date createDate;


    private Long cUserId;

    private String cUserName;

    private String cNickName;

    private String wangwangId;

    private String qq;

    //任务状态 1:待审核 2:已审核 3:进行中 4：待付款 5：已付款 6：已发货 7：已完成
    private String robTaskStatus;

    private Long merchantId;
    private String merchantName;
}
