package com.amitgoenka.encryption;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
		TestPropertySourcesConfigurer.class
})
public class EncryptionApplicationTests {

	@Test
	public void contextLoads() {
	}

}