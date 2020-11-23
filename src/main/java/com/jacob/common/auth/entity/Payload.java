package com.jacob.common.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Jacob
 * @version 1.0
 * @date 2020/11/22
 */
@Data
public class Payload<T> {
    private String id;
    private T userInfo;
    private Date expiration;
}
