package com.linln.admin.system.domain;

import lombok.Data;

@Data
public class Response<T> {
    public String msg;
    public int code;
    public T data;
    public int count;
}
