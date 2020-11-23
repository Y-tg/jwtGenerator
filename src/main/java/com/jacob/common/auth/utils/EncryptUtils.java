package com.jacob.common.auth.utils;

import com.jacob.common.auth.entity.AlgorithmConstants;
import com.jacob.common.auth.entity.ResultVo;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Jacob
 * @version 1.0
 * @date 2020/11/22
 */
@Data
public class EncryptUtils {
    private SignatureAlgorithm algorithm;

    public EncryptUtils(String algorithm) {
        if (algorithm == null) {
            throw new RuntimeException("algorithm cannot be null");
        }
        this.algorithm = AlgorithmConstants.algorithms.get(algorithm);
        if (this.algorithm == null) {
            throw new RuntimeException(algorithm + "is not support");
        }
    }

    public ResultVo generateKey(String publicKeyFilename, String privateKeyFilename) {
        switch (this.algorithm.getFamilyName()) {
            case "HMAC":
                return generateHsKey(publicKeyFilename, privateKeyFilename);
            default:
                return generateRsKey(publicKeyFilename, privateKeyFilename);
        }
    }

    public Key getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    public Key getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * 获取密钥
     *
     * @param bytes 私钥的字节形式
     * @return
     * @throws Exception
     */
    private Key getPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        bytes = Base64.getDecoder().decode(bytes);
        switch (algorithm) {
            case HS512:
                return new SecretKeySpec(bytes,0, bytes.length, algorithm.getJcaName());
            default:
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
                KeyFactory factory = KeyFactory.getInstance(algorithm.getFamilyName());
                return factory.generatePrivate(spec);
        }
    }

    /**
     * 获取密钥
     *
     * @param bytes 私钥的字节形式
     * @return
     * @throws Exception
     */
    private Key getPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        bytes = Base64.getDecoder().decode(bytes);
        switch (algorithm) {
            case HS512:
                return new SecretKeySpec(bytes,0, bytes.length, algorithm.getJcaName());
            default:
                X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
                KeyFactory factory = KeyFactory.getInstance(algorithm.getFamilyName());
                return factory.generatePublic(spec);
        }
    }

    private static byte[] readFile(String fileName) throws Exception {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    private ResultVo generateHsKey(String publicKeyFilename, String privateKeyFilename) {
        SecretKey secretKey = Keys.secretKeyFor(algorithm);
        byte[] secretEncoded = secretKey.getEncoded();
        secretEncoded = Base64.getEncoder().encode(secretEncoded);
        try {
            writeFile(publicKeyFilename, secretEncoded);
            writeFile(privateKeyFilename, secretEncoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultVo(new String(secretEncoded), algorithm.getValue(), new String(secretEncoded));
    }

    private ResultVo generateRsKey(String publicKeyFilename, String privateKeyFilename) {
        KeyPair keyPair = Keys.keyPairFor(algorithm);
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();
        byte[] bPrivate = aPrivate.getEncoded();
        byte[] bPublic = aPublic.getEncoded();
        bPrivate = Base64.getEncoder().encode(bPrivate);
        bPublic = Base64.getEncoder().encode(bPublic);
        try {
            writeFile(publicKeyFilename, bPublic);
            writeFile(privateKeyFilename, bPrivate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultVo(new String(bPrivate), algorithm.getValue(), new String(bPublic));
    }

    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (!dest.exists()) {
            dest.createNewFile();
        }
        Files.write(dest.toPath(), bytes);
    }
}
