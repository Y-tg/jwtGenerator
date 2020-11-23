package com.jacob.common.auth.entity;

import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jacob
 * @version 1.0
 * @date 2020/11/22
 */
public interface AlgorithmConstants {
    Map<String, SignatureAlgorithm> algorithms = new HashMap<String, SignatureAlgorithm>(){{
        put("HS512", SignatureAlgorithm.HS512);
        put("RS512", SignatureAlgorithm.RS512);
    }};
}
