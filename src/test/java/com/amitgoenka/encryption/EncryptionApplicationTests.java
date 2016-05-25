package com.amitgoenka.encryption;

import com.amitgoenka.encryption.config.ExternalPropertySourcesConfigurer;
import com.amitgoenka.encryption.config.PropertySourcesConfigurer;
import com.amitgoenka.encryption.crypto.AESEncryptor;
import com.amitgoenka.encryption.props.ExternalPropertyEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.Import;

@Import(value = {
        EncryptionApplication.class,
        AESEncryptor.class,
        ExternalPropertyEncryptor.class,
        ExternalPropertySourcesConfigurer.class,
        PropertySourcesConfigurer.class,
})
public class EncryptionApplicationTests {

    @Test
    public void contextLoads() {
        Assert.assertTrue(true);
    }

}