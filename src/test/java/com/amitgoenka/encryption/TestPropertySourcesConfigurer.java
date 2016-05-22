package com.amitgoenka.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources(value = {
        @PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:security.yml", ignoreResourceNotFound = true)
})
public class TestPropertySourcesConfigurer {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return propertySourcesConfigurer;
    }
}
