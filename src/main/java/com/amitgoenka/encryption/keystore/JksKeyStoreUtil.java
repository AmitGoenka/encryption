package com.amitgoenka.encryption.keystore;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;

class JksKeyStoreUtil {

    private static final String type = "JKS";

    private static KeyStore getKeyStore(String filename, String password) throws Exception {

        if (StringUtils.isEmpty(password)) throw new Exception("KEY STORE PASSWORD NOT FOUND.");
        if (StringUtils.isEmpty(filename)) throw new Exception("KEY STORE FILE NAME NOT FOUND.");
        else if (filename.contains("classpath:")) filename = filename.replace("classpath:", "");
        else if (filename.contains("classpath*:")) filename = filename.replace("classpath*:", "");

        Resource keyStoreResource = new FileSystemResource(filename);
        if (!keyStoreResource.getFile().exists()) throw new Exception("KEY STORE FILE DOES NOT EXIST.");

        KeyStore keyStore = KeyStore.getInstance(type);
        try (FileInputStream fis = new FileInputStream(keyStoreResource.getFile())) {
            keyStore.load(fis, password.toCharArray());
        }

        return keyStore;
    }

    static KeyPair getKey(String filename, String password, String alias) throws Exception {
        KeyStore keyStore = getKeyStore(filename, password);
        if (keyStore == null) throw new Exception("KEY STORE NOT FOUND.");

        KeyPair keyPair = null;
        if (keyStore.containsAlias(alias)) {
            PrivateKey privateKey = null;
            PublicKey publicKey = null;
            if (keyStore.isKeyEntry(alias)) {
                Key key = keyStore.getKey(alias, password.toCharArray());
                privateKey = (PrivateKey) key;
            }
            if (keyStore.isCertificateEntry(alias) || keyStore.getCertificate(alias) != null) {
                Certificate certificate = keyStore.getCertificate(alias);
                publicKey = certificate.getPublicKey();
            }
            keyPair = new KeyPair(publicKey, privateKey);
        }

        return keyPair;
    }
}