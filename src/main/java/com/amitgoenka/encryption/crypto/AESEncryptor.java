package com.amitgoenka.encryption.crypto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.amitgoenka.encryption.crypto.AESEncryptionUtil.generateIV;
import static com.amitgoenka.encryption.crypto.AESEncryptionUtil.generateSalt;

/**
 * An AES Encryption Implementation
 */
@Configuration
public class AESEncryptor {

    @Getter
    @Setter
    private transient String salt;
    @Getter
    @Setter
    private transient String iv;

    @Bean
    public AESEncryptor aesEncryptor() {
        return new AESEncryptor();
    }

    /**
     * Applies the AES Encryption Algorithm on a plain text.
     *
     * @param plainText the text which needs to be encrypted
     * @param key       master key for the encryption
     * @return returns the encrypted text
     * @throws Exception
     */
    public String encrypt(String plainText, String key) throws Exception {
        salt = generateSalt();
        iv = generateIV();
        return AESEncryptionUtil.encrypt(plainText, key, salt, iv);
    }

    /**
     * Decrypts the encrypted text using the same key and iv
     *
     * @param cipherText the encrypted text which needs to be decrypted
     * @param key        master key for the decryption
     * @return the original decrypted plain text
     * @throws Exception
     */
    public String decrypt(String cipherText, String key) throws Exception {
        return AESEncryptionUtil.decrypt(cipherText, key, salt, iv);
    }

}