package com.amitgoenka.encryption.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * AES Encryption Utility class
 */
class AESEncryptionUtil {

    private static final transient String algorithm = "AES";
    private static final transient String secretKeyGenAlgorithm = "PBKDF2WithHmacSHA512";
    private static final transient String cipherAlgorithm = "AES/CBC/PKCS5Padding";
    private static final transient String secureRandomAlgorithm = "SHA1PRNG";
    private static final transient String messageDigestAlgorithm = "SHA-256";
    private static final transient String charsetName = "UTF-8";
    private static final transient String encodingStyle = Base64.class.getSimpleName();
    private static final transient int keyLength = 256;
    private static final transient int saltLength = 20;
    private static final transient int ivLength = 16;
    private static final transient int iterations = 65536;

    private static String encode(byte[] bytes) throws UnsupportedEncodingException {
        return Base64.class.getSimpleName().equalsIgnoreCase(encodingStyle)
                ? Base64.getEncoder().encodeToString(bytes)
                : new String(bytes, charsetName);
    }

    private static byte[] decode(String input) throws UnsupportedEncodingException {
        return Base64.class.getSimpleName().equalsIgnoreCase(encodingStyle)
                ? Base64.getDecoder().decode(input)
                : input.getBytes(charsetName);
    }

    /**
     * Generate Salt for a given length
     *
     * @return a new pseudo random salt of a given length.
     */
    static String generateSalt() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Random random = SecureRandom.getInstance(secureRandomAlgorithm);
        byte[] bytes = new byte[saltLength];
        random.nextBytes(bytes);
        return encode(bytes);
    }

    /**
     * Generate IV for a given length
     *
     * @return a new pseudo random IV of a given length.
     */
    static String generateIV() throws UnsupportedEncodingException {
        Random random = new SecureRandom();
        byte[] bytes = new byte[ivLength];
        random.nextBytes(bytes);
        return encode(bytes);
    }

    /**
     * Generates an encoded message digest
     *
     * @param text text that need to be hashed
     * @return the encoded character array of the hashed input
     * @throws NoSuchAlgorithmException
     * @see {@link} https://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html
     */
    private static char[] getDigestWithEncoding(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(messageDigestAlgorithm);
        messageDigest.update(text.getBytes(charsetName));
        return Base64.getEncoder().encodeToString(messageDigest.digest()).toCharArray();
    }

    /**
     * Derive an encryption key from given key and salt.
     *
     * @param key  the key for the key derivation
     * @param salt the salt for the key derivation
     * @return the secret key object generated out of the key and salt
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static byte[] generateKey(char[] key, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(secretKeyGenAlgorithm);
        KeySpec keySpec = new PBEKeySpec(key, salt, iterations, keyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey.getEncoded();
    }

    /**
     * Generates the Cipher for Encryption/Decryption operation
     *
     * @param mode encrypt/decrypt mode
     * @param key  master key for the cipher
     * @param salt salt for the key generation
     * @param iv   initializing vector for the CBC operation
     * @return the cipher
     * @throws Exception
     */
    private static Cipher generateCipher(int mode, String key, String salt, String iv) throws Exception {
        // Decode the salt
        byte[] saltBytes = decode(salt);
        // Decode the iv
        byte[] ivBytes = decode(iv);
        // Hash the master key
        char[] keyDigest = getDigestWithEncoding(key);
        // Generate Secret Key from the hashed key and salt
        byte[] secretKey = generateKey(keyDigest, saltBytes);
        // Generate Secret Key Spec from the available secret key
        SecretKey secretKeySpec = new SecretKeySpec(secretKey, algorithm);
        // Instantiate the Cipher
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // Initialize the Cipher with the Key
        cipher.init(mode, secretKeySpec, new IvParameterSpec(ivBytes));
        return cipher;
    }

    /**
     * @param plainText the plain text to encrypt
     * @param key       the key for the encryption
     * @param salt      the salt for the key generation
     * @param iv        the initializing vector for the CBC operation
     * @return the encrypted text
     * @throws Exception
     */
    static synchronized String encrypt(String plainText, String key, String salt, String iv) throws Exception {
        // Generate the Cipher
        Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE, key, salt, iv);
        // Perform the encryption and return the encrypted text in an encoded format
        return encode(cipher.doFinal(plainText.getBytes(charsetName)));
    }

    /**
     * @param cipherText the plain text to encrypt
     * @param key        the key for the encryption
     * @param salt       the salt for the key generation
     * @param iv         the initializing vector for the CBC operation
     * @return the encrypted text
     * @throws Exception
     */
    static synchronized String decrypt(String cipherText, String key, String salt, String iv) throws Exception {
        // Generate the Cipher
        Cipher cipher = generateCipher(Cipher.DECRYPT_MODE, key, salt, iv);
        // Perform the decryption and return the decrypted text in a plain text format
        return new String(cipher.doFinal(decode(cipherText)), charsetName);
    }

}