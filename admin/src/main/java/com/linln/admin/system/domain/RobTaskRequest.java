package com.linln.admin.system.domain;

import lombok.Data;

@Data
public class RobTaskRequest {
    public String userName;
    public int page;
    public int limit;
}
