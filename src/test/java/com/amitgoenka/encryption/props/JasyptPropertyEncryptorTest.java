package com.amitgoenka.encryption.props;

import com.amitgoenka.encryption.EncryptionApplicationTests;
import com.amitgoenka.encryption.TestPropertyHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                EncryptionApplicationTests.class,
                TestPropertyHolder.class,
                JasyptPropertyEncryptor.class
        }
)
public class JasyptPropertyEncryptorTest {

    @Autowired
    TestPropertyHolder propertyHolder;

    @Autowired
    JasyptPropertyEncryptor jasyptEncryptor;

    @Test
    public void testJasyptEncryptor() throws Exception {
        assertNotNull(jasyptEncryptor);
        String plainText = "This Is A Pointless Password!";
        String decryptedText = jasyptEncryptor.decrypt(jasyptEncryptor.encrypt(plainText));
        assertTrue(plainText.equals(decryptedText));
    }

    @Test
    public void testEncryptJasyptProperties() throws Exception {
        assertFalse(StringUtils.isEmpty(propertyHolder.getLocation()));
        assertFalse(StringUtils.isEmpty(propertyHolder.getFilename()));
        jasyptEncryptor.encryptProperty();
        assertTrue(jasyptEncryptor.isEncrypted());
    }

    @Test
    public void testDecryptJasyptProperties() throws Exception {
        String decryptedText = jasyptEncryptor.decryptProperty();
        assertFalse(StringUtils.isEmpty(decryptedText));
    }
}