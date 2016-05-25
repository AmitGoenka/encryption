package com.amitgoenka.encryption.crypto;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.util.StringUtils;

public class JasyptEncryptorUtil {

    public static String encrypt(String plainText, String key) {
        if (!StringUtils.isEmpty(plainText) && !StringUtils.isEmpty(key)) {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(key);
            return encryptor.encrypt(plainText);
        }
        return null;
    }

    public static String decrypt(String cipherText, String key) {
        if (!StringUtils.isEmpty(cipherText) && !StringUtils.isEmpty(key)) {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(key);
            return encryptor.decrypt(cipherText);
        }
        return null;
    }
}
