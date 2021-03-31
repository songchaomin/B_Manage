package com.linln.modules.task.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "task")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private String taskContent;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;
    private String merchantName;
    private String taskLable;
    private Byte effective;
    private Byte deleteFlg;

}
