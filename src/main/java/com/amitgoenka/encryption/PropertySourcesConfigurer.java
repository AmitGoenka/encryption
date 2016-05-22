package com.amitgoenka.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;
import java.util.Properties;

/**
 * This class is a Spring's implementation of property sources placeholder configuration.
 *
 * @see @link https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/PropertySourcesPlaceholderConfigurer.html
 * <p>
 * The loaded property files are injected into environment and are available at runtime.
 */
@Configuration
public class PropertySourcesConfigurer {

    static Properties externalProperties;

    @Bean
    @DependsOn({"externalProperties"})
    public static PropertySourcesPlaceholderConfigurer propertySourcesConfigurer() throws IOException {
        PropertySourcesPlaceholderConfigurer propertySourcesConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesConfigurer.setIgnoreUnresolvablePlaceholders(true);
        if (null != externalProperties && !externalProperties.isEmpty()) {
            propertySourcesConfigurer.setLocalOverride(true);
            propertySourcesConfigurer.setProperties(externalProperties);
        }
        return propertySourcesConfigurer;
    }

}