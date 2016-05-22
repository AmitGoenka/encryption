package com.amitgoenka.encryption.keystore;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestPropertyHolder.class},
        initializers = {ConfigFileApplicationContextInitializer.class}
)
public class KeyStoreTest {

    @Autowired
    TestPropertyHolder propertyHolder;

    @Test
    public void buildJCEKSTest() throws CertificateException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String filename = propertyHolder.getLocation() + propertyHolder.getKeystoreSecret().getFilename();
        JceksKeyStoreUtil.buildKeyStore(filename);
    }

    @Test
    public void getKeyFromJCEKSTest() throws CertificateException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String filename = propertyHolder.getLocation() + propertyHolder.getKeystoreSecret().getFilename();
        String password = propertyHolder.getKeystoreSecret().getPassword();
        String alias = propertyHolder.getKeystoreSecret().getMasterKey().getAlias();
        String key = JceksKeyStoreUtil.getKey(filename, password, alias);
        Assert.assertFalse(StringUtils.isEmpty(key));
    }

    @Test
    public void getKeyFromJKSTest() throws Exception {
        String filename = propertyHolder.getLocation() + propertyHolder.getKeystoreSsl().getFilename();
        String password = propertyHolder.getKeystoreSsl().getPassword();
        String alias = propertyHolder.getKeystoreSsl().getMasterKey().getAlias();
        KeyPair keyPair = JksKeyStoreUtil.getKey(filename, password, alias);
        Assert.assertNotNull(keyPair);
        Assert.assertNotNull(keyPair.getPrivate());
        Assert.assertNotNull(keyPair.getPublic());
    }
}
