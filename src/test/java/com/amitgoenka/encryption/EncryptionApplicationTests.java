package com.amitgoenka.encryption;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(value = {
        EncryptionApplication.class,
        ExternalPropertySourcesConfigurer.class,
        PropertySourcesConfigurer.class,
})
public class EncryptionApplicationTests {

    @Test
    public void contextLoads() {
    }

}