package com.amitgoenka.encryption.props;

import com.amitgoenka.encryption.crypto.AESEncryptor;
import com.amitgoenka.encryption.keystore.JceksKeyStoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static com.amitgoenka.encryption.props.YamlUtil.*;

/**
 * AES Encryption Implementation to encrypt one password (e.g. database password).
 * This implementation reads the security.yml file (which can be externalized for security purposes) for access information.
 * The security.yml file contains the JCEKS access details and also, the location for the external encryption config file.
 * The encryption config file contains the actual salt, iv and the plain text password.
 * This program attempts to encrypt the password in the encryption config yaml file during start-up and overwrites with encrypted version.
 * NOTE: This Implementation can be converted into a Factory/Builder pattern if required.
 */
@Configuration
public class ExternalPropertyEncryptor {

    private static final Logger LOGGER = Logger.getLogger(ExternalPropertyEncryptor.class.getName());

    private AESEncryptor encryptor;

    private transient String location;
    private transient String encryptionFilename;
    private transient String jceksFilename;
    private transient String jceksPassword;
    private transient String jceksMasterKeyAlias;

    private static final String KEY_IS_ENCRYPTED = "test.encryption.isEncrypted";
    private static final String KEY_SECURE_PROPERTY = "test.encryption.password";
    private static final String KEY_SALT = "test.encryption.salt";
    private static final String KEY_IV = "test.encryption.iv";

    @Bean
    @Autowired
    ExternalPropertyEncryptor propertyEncryptor(ConfigurableApplicationContext context, AESEncryptor aesEncryptor) throws IOException {
        Environment env = context.getEnvironment();
        securityProperties(context);

        ExternalPropertyEncryptor propertyEncryptor = new ExternalPropertyEncryptor();
        propertyEncryptor.encryptor = aesEncryptor;
        propertyEncryptor.location = env.getProperty("security.config.location");
        propertyEncryptor.encryptionFilename = env.getProperty("security.config.filename");
        propertyEncryptor.jceksFilename = env.getProperty("security.config.keystore-secret.filename");
        propertyEncryptor.jceksPassword = env.getProperty("security.config.keystore-secret.password");
        propertyEncryptor.jceksMasterKeyAlias = env.getProperty("security.config.keystore-secret.master-key.alias");
        return propertyEncryptor;
    }

    private static void securityProperties(ConfigurableApplicationContext context) throws IOException {
        Resource resource = new ClassPathResource("security.yml");
        YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
        org.springframework.core.env.PropertySource<?> yamlPropertySource = yamlLoader.load("security-properties", resource, null);
        context.getEnvironment().getPropertySources().addFirst(yamlPropertySource);
    }

    private boolean isRaw(Map<?, ?> map, Object key) {
        Object value = getProperty(map, key);
        return value == null || !(Boolean) value;
    }

    String encrypt(String plainText) throws Exception {
        return encryptor.encrypt(plainText, JceksKeyStoreUtil.getKey(location + jceksFilename, jceksPassword, jceksMasterKeyAlias));
    }

    String decrypt(String plainText) throws Exception {
        return encryptor.decrypt(plainText, JceksKeyStoreUtil.getKey(location + jceksFilename, jceksPassword, jceksMasterKeyAlias));
    }

    public void encryptSecureProperties() throws Exception {
        String filename = location + encryptionFilename;
        Map<?, ?> yamlMap = loadYaml(filename);
        String secureProperty = Objects.toString(getProperty(yamlMap, KEY_SECURE_PROPERTY), null);

        if (StringUtils.hasLength(secureProperty)) {
            if (isRaw(yamlMap, KEY_IS_ENCRYPTED)) {
                String encryptedProperty = encrypt(secureProperty);
                setProperty(yamlMap, KEY_SALT, encryptor.getSalt());
                setProperty(yamlMap, KEY_IV, encryptor.getIv());
                setProperty(yamlMap, KEY_SECURE_PROPERTY, encryptedProperty);
                setProperty(yamlMap, KEY_IS_ENCRYPTED, true);
                dumpYaml(filename, yamlMap);
            } else {
                encryptor.setSalt(Objects.toString(getProperty(yamlMap, KEY_SALT), null));
                encryptor.setIv(Objects.toString(getProperty(yamlMap, KEY_IV), null));
                LOGGER.finest("\nDB Password encryption skipped. " +
                        "Either password is already encrypted or password property or encrypted qualifier is not present in yaml configuration file.");
            }
        } else {
            LOGGER.info("\nDB Password encryption skipped. Password is not present in the yaml configuration file.");
        }
    }

}