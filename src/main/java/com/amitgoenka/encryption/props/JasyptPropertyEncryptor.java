package com.amitgoenka.encryption.props;

import com.amitgoenka.encryption.crypto.JasyptEncryptorUtil;
import com.amitgoenka.encryption.keystore.JceksKeyStoreUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Jasypt Implementation to encrypt properties in property files (e.g. user credentials, database password etc).
 * This currently works on the mock-jasypt-encryption.properties file, which can be externalized.
 * The security.yml file contains the JCEKS access details to find the encryption key.
 * The encryption config file contains the actual salt, iv and the plain text password.
 * This program is an on demand encryptor decryptor.
 * Jasypt integrate nicely with spring to encrypt and decrypt properties automatically for seamless property access,
 * But that is left for future exploration.
 */
@Configuration
public class JasyptPropertyEncryptor {

    private transient String location;
    private transient String jasyptFilename;
    private transient String jceksFilename;
    private transient String jceksPassword;
    private transient String jceksMasterKeyAlias;

    private static final String keyIsEncrypted = "test.jasypt.isEncrypted";
    private static final String keyPassword = "test.jasypt.password";

    @Bean
    @Autowired
    JasyptPropertyEncryptor jasyptEncryptor(Environment env) throws IOException {
        JasyptPropertyEncryptor jasyptEncryptor = new JasyptPropertyEncryptor();
        jasyptEncryptor.location = env.getProperty("security.config.location");
        jasyptEncryptor.jasyptFilename = env.getProperty("security.config.jasypt.filename");
        jasyptEncryptor.jceksFilename = env.getProperty("security.config.keystore-secret.filename");
        jasyptEncryptor.jceksPassword = env.getProperty("security.config.keystore-secret.password");
        jasyptEncryptor.jceksMasterKeyAlias = env.getProperty("security.config.keystore-secret.master-key.alias");
        return jasyptEncryptor;
    }

    String encrypt(String plainText) throws Exception {
        return JasyptEncryptorUtil.encrypt(plainText, JceksKeyStoreUtil.getKey(location + jceksFilename, jceksPassword, jceksMasterKeyAlias));
    }

    String decrypt(String plainText) throws Exception {
        return JasyptEncryptorUtil.decrypt(plainText, JceksKeyStoreUtil.getKey(location + jceksFilename, jceksPassword, jceksMasterKeyAlias));
    }

    boolean isEncrypted() throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration(location + jasyptFilename);
        return Boolean.valueOf(config.getString(keyIsEncrypted)) && !StringUtils.isEmpty(config.getString(keyPassword));
    }

    void encryptProperty() throws Exception {
        PropertiesConfiguration configuration = new PropertiesConfiguration(location + jasyptFilename);
        if (!isEncrypted()) {
            configuration.setProperty(keyPassword, encrypt(configuration.getString(keyPassword)));
            configuration.setProperty(keyIsEncrypted, "true");
            configuration.save();
        }
    }

    String decryptProperty() throws Exception {
        String password = new PropertiesConfiguration(location + jasyptFilename).getString(keyPassword);
        return isEncrypted() ? decrypt(password) : password;
    }

}