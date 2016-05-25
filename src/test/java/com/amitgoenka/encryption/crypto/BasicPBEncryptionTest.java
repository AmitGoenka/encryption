package com.amitgoenka.encryption.crypto;

import com.amitgoenka.encryption.EncryptionApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Basic password based encryption tests using simple Java.
 * This approach could potentially be useful for user input driven encryption.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {EncryptionApplicationTests.class}
)
public class BasicPBEncryptionTest {

    @Test
    public void testDESedeWithKeyGen() throws Exception {
        String plaintText = "This is a plain text.";
        Cipher cipher = Cipher.getInstance("DESede");
        Key key = KeyGenerator.getInstance("DESede").generateKey();

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedText = cipher.doFinal(plaintText.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key);
        String decryptedText = new String(cipher.doFinal(encryptedText));
        Assert.assertEquals(plaintText, decryptedText);
    }

    @Test
    public void testAESWithGivenKey() throws Exception {
        String plaintText = "This is a plain text.";
        String passwordForEncryption = "ThisPasswordIsUserProvided32Byte";
        Cipher cipher = Cipher.getInstance("AES");
        Key key = new SecretKeySpec(passwordForEncryption.getBytes(), "AES");

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedText = cipher.doFinal(plaintText.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key);
        String decryptedText = new String(cipher.doFinal(encryptedText));
        Assert.assertEquals(plaintText, decryptedText);
    }

}
