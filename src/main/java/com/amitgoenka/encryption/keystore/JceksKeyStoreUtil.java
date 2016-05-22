package com.amitgoenka.encryption.keystore;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;

class JceksKeyStoreUtil {

    private static final transient String type = "JCEKS";
    private static final transient String keySpec = "AES";

    private static String getRandom() {
        return getRandom(null);
    }

    private static String getRandom(String input) {
        return StringUtils.isEmpty(input) ? new BigInteger(130, new SecureRandom()).toString(32) : input;
    }

    private static void writeInfoToFile(String filename, StringBuilder builder) throws IOException {
        Resource keyStoreInfoResource = new FileSystemResource(filename);
        File keyStoreInfoFile = keyStoreInfoResource.getFile();
        FileWriter fw = new FileWriter(keyStoreInfoFile);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(builder.toString());
        bw.flush();
        fw.flush();
        bw.close();
    }

    private static KeyStore getKeyStore(String type, String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance(type);
        keyStore.load(null, password.toCharArray());
        return keyStore;
    }

    private static KeyStore getKeyStore(String type, String password, String filename) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        if (!StringUtils.isEmpty(filename) && !StringUtils.isEmpty(password)) {
            Resource keyStoreResource = new FileSystemResource(filename);
            if (keyStoreResource.getFile().exists()) {
                FileInputStream fis = new FileInputStream(keyStoreResource.getFile());
                KeyStore keyStore = KeyStore.getInstance(type);
                keyStore.load(fis, password.toCharArray());
                fis.close();
                return keyStore;
            }
        }
        return null;
    }

    static void buildKeyStore(String filename) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableEntryException {
        // Initialize the Password
        String password = getRandom();
        KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(password.toCharArray());

        // Initialize the keystore with the password
        KeyStore secretKeyStore = getKeyStore(type, password);

        // Build the Secret Key
        SecretKey secretKey = new SecretKeySpec(getRandom().getBytes(), keySpec);
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);

        // Initialize the Alias and set in the keystore
        String alias = getRandom();
        secretKeyStore.setEntry(alias, secretKeyEntry, keyPassword);

        // Save off the Keystore in file system
        FileOutputStream fos = new FileOutputStream(filename);
        secretKeyStore.store(fos, password.toCharArray());
        fos.close();

        // Log key information for reference
        // IMPORTANT NOTE: TODO: Once key information is used, delete the info file
        StringBuilder infoBuilder = new StringBuilder().append("SENSITIVE INFORMATION:");
        infoBuilder.append("\nGENERATED NEW PASSWORD: ").append(password);
        infoBuilder.append("\nGENERATED NEW ALIAS: ").append(alias);
        infoBuilder.append("\n\nNOTE: The key store password and master key alias to be updated in the application property for the first time.");
        writeInfoToFile(filename + ".info", infoBuilder);
    }

    static String getKey(String filename, String password, String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableEntryException {
        // Retrieve the keystore
        KeyStore secretKeyStore = getKeyStore(type, password, filename);
        if (secretKeyStore == null) return null;

        // Initialize the Password
        KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(password.toCharArray());
        if (!StringUtils.isEmpty(alias) && secretKeyStore.getEntry(alias, keyPassword) != null) {
            KeyStore.Entry entry = secretKeyStore.getEntry(alias, keyPassword);
            SecretKey keyFound = ((KeyStore.SecretKeyEntry) entry).getSecretKey();
            return new String(keyFound.getEncoded(), "UTF-8");
        } else {
            return null;
        }
    }

}