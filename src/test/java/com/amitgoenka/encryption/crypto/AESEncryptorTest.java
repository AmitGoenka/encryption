package com.amitgoenka.encryption.crypto;

import com.amitgoenka.encryption.EncryptionApplicationTests;
import com.amitgoenka.encryption.TestPropertyHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                EncryptionApplicationTests.class,
                TestPropertyHolder.class,
                AESEncryptor.class
        }
)
public class AESEncryptorTest {

    @Autowired
    TestPropertyHolder propertyHolder;

    @Autowired
    AESEncryptor aesEncryptor;

    @Test
    public void testAESEncryptorOnSuccess() throws Exception {
        Assert.assertNotNull(aesEncryptor);
        String plainText = "This Is A Pointless Password!";
        String key = "Having Key Like This Doesn't Make Any Sense!";

        String encryptedText = aesEncryptor.encrypt(plainText, key);
        assertFalse(StringUtils.isEmpty(encryptedText));
        String decryptedText = aesEncryptor.decrypt(encryptedText, key);
        assertFalse(StringUtils.isEmpty(decryptedText));
        assertTrue(plainText.equals(decryptedText));
        assertNotSame(plainText, decryptedText);
    }

    @Test(expected = BadPaddingException.class)
    public void testAESEncryptorOnFailure() throws Exception {
        Assert.assertNotNull(aesEncryptor);
        String plainText = "This Is A Pointless Password, Again!";
        String key = "First Key";
        String anotherKey = "Second Key";

        String encryptedText = aesEncryptor.encrypt(plainText, key);
        assertFalse(StringUtils.isEmpty(encryptedText));
        aesEncryptor.decrypt(encryptedText, anotherKey);
    }

}