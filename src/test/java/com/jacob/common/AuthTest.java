package com.jacob.common;

import com.jacob.common.auth.entity.Payload;
import com.jacob.common.auth.entity.ResultVo;
import com.jacob.common.auth.entity.UserInfo;
import com.jacob.common.auth.utils.EncryptUtils;
import com.jacob.common.auth.utils.JsonUtils;
import com.jacob.common.auth.utils.JwtUtils;
import com.jacob.common.auth.utils.RsaUtils;
import org.json.JSONObject;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Jacob
 * @version 1.0
 * @date 2020/11/22
 */
public class AuthTest {
    private String privateFilePath = "G:\\JAVA\\workspace\\jetbrains\\jwtTest\\id_rsa";
    private String publicFilePath = "G:\\JAVA\\workspace\\jetbrains\\jwtTest\\id_rsa.pub";

    @Test
    public void testRSA() throws Exception {
    //    // 生成密钥对
    //    RsaUtils.generateKey(publicFilePath, privateFilePath, "HS512");
    //
    //// 获取私钥
    //PrivateKey privateKey = RsaUtils.getPrivateKey(privateFilePath, "HS512");
    //    System.out.println("privateKey = " + privateKey);
    //// 获取公钥
    //PublicKey publicKey = RsaUtils.getPublicKey(publicFilePath, "HS512");
    //    System.out.println("publicKey = " + publicKey);
        ResultVo result = new EncryptUtils("RS512").generateKey(publicFilePath, privateFilePath);
        System.out.println(JsonUtils.toString(result));
    }

    @Test
    public void testJWT() throws Exception {
        //// 获取私钥
        //PrivateKey privateKey = RsaUtils.getPrivateKey(privateFilePath, "HS512");
        //// 生成token
        //String token = JwtUtils.generateTokenExpireInMinutes(new UserInfo(1L, "Jack", "guest"), privateKey, 5, "HS512");
        //System.out.println("token = " + token);
        //
        //// 获取公钥
        //PublicKey publicKey = RsaUtils.getPublicKey(publicFilePath, "HS512");
        //// 解析token
        //Payload<UserInfo> info = JwtUtils.getInfoFromToken(token, publicKey, UserInfo.class);
        //
        //System.out.println("info.getExpiration() = " + info.getExpiration());
        //System.out.println("info.getUserInfo() = " + info.getUserInfo());
        //System.out.println("info.getId() = " + info.getId());

        Key privateKey = new EncryptUtils("RS512").getPrivateKey(privateFilePath);
        String token = JwtUtils.generateTokenExpireInMinutes(null, privateKey, 5, "RS512");
        System.out.println("token = " + token);

        Key publicKey = new EncryptUtils("RS512").getPublicKey(publicFilePath);
        Payload<UserInfo> info = JwtUtils.getInfoFromToken(token, publicKey, UserInfo.class);
        System.out.println("info: " + info);
    }
}
