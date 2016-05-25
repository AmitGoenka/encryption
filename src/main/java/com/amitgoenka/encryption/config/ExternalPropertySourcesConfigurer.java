package com.amitgoenka.encryption.config;

import com.amitgoenka.encryption.props.ExternalPropertyEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class loads the configuration file from an external directory.
 * The external file is essentially the security configuration file.
 * In a production environment, the security configuration file should be externalized.
 */
@Configuration
public class ExternalPropertySourcesConfigurer {

    private static final Logger LOGGER = Logger.getLogger(ExternalPropertySourcesConfigurer.class.getName());

    @Bean
    @Autowired
    public Properties externalProperties(Environment env, ExternalPropertyEncryptor propertyEncryptor) throws Exception {
        Properties properties = null;
        try {
            String configFileLocation = env.getProperty("security.config.location");
            String configFileName = env.getProperty("security.config.filename");
            if (StringUtils.isEmpty(configFileLocation)) throw new Exception("EXTERNAL CONFIG LOCATION NOT FOUND.");
            if (StringUtils.isEmpty(configFileName)) throw new Exception("EXTERNAL CONFIG FILENAME NOT FOUND.");
            File file = new File(configFileLocation + configFileName);
            if (!file.exists()) throw new Exception("EXTERNAL CONFIG FILE NOT FOUND.");

            try {
                propertyEncryptor.encryptSecureProperties();
            } catch (Exception exception) {
                LOGGER.info(exception.getMessage());
            }

            FileSystemResource fsr = new FileSystemResource(file);
            YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
            yamlPropertiesFactoryBean.setResources(fsr);
            properties = yamlPropertiesFactoryBean.getObject();
            PropertySourcesConfigurer.externalProperties = properties;
        } catch (Exception exception) {
            LOGGER.warning(exception.getMessage());
        }
        return properties;
    }

}