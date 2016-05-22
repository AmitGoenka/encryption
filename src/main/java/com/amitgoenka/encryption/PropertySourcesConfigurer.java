package com.amitgoenka.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * This class is a Spring's implementation of property sources placeholder configuration.
 * @see @link https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/PropertySourcesPlaceholderConfigurer.html
 *
 * The loaded property files are injected into environment and are available at runtime.
 */
@Configuration
@PropertySource({"classpath:security.yml"})
public class PropertySourcesConfigurer {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return propertySourcesConfigurer;
    }
}
