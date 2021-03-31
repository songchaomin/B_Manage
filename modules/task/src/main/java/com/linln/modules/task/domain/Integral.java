package com.linln.modules.task.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "integral")
public class Integral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 帐号名称
     */
    private String username;

    /**
     * 积分数
     */
    private Integer point;

}
