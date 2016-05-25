package com.amitgoenka.encryption.crypto;

import com.amitgoenka.encryption.EncryptionApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;

/**
 * Basic encryption tests using spring framework's encryption library
 * @see {@link 'http://docs.spring.io/spring-security/site/docs/4.1.0.RELEASE/reference/htmlsingle/#crypto'}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {EncryptionApplicationTests.class}
)
public class SpringPBEncryptionTest {

    @Test
    public void testTextEncryptor() {
        String password = "This is a bad password to encrypt with.";
        final String salt = KeyGenerators.string().generateKey();

        String plainText = "This is a plain text.";
        String encryptedText = Encryptors.text(password, salt).encrypt(plainText);
        String decryptedText = Encryptors.text(password, salt).decrypt(encryptedText);
        Assert.assertEquals(plainText, decryptedText);
    }

    @Test
    public void testBytesEncryptor () throws UnsupportedEncodingException {
        String password = "This is a bad password to encrypt with.";
        final String salt = KeyGenerators.string().generateKey();

        String plainText = "This is a plain text.";
        byte[] encryptedTextBytes = Encryptors.standard(password, salt).encrypt(plainText.getBytes());
        String decryptedText = new String(Encryptors.standard(password, salt).decrypt(encryptedTextBytes));
        Assert.assertEquals(plainText, decryptedText);
    }

    @Test
    public void testBCryptEncoder () {
        String plainText = "This is a plain text.";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String cipherText = encoder.encode(plainText);
        Assert.assertTrue(encoder.matches(plainText, cipherText));
    }

}