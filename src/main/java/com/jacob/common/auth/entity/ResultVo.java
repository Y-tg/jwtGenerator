package com.jacob.common.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Jacob
 * @version 1.0
 * @date 2020/11/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultVo {
    private String privateKey;
    private String alg;
    private String publicKey;
}
