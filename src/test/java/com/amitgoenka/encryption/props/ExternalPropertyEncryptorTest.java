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

import java.util.Map;
import java.util.Objects;

import static com.amitgoenka.encryption.props.YamlUtil.getProperty;
import static com.amitgoenka.encryption.props.YamlUtil.loadYaml;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                EncryptionApplicationTests.class,
                TestPropertyHolder.class,
                ExternalPropertyEncryptor.class
        }
)
public class ExternalPropertyEncryptorTest {

    @Autowired
    TestPropertyHolder propertyHolder;

    @Autowired
    ExternalPropertyEncryptor propertyEncryptor;

    @Test
    public void testAESEncryptorOnSuccess() throws Exception {
        String plainText = "This Is A Pointless Password!";
        String decryptedText = propertyEncryptor.decrypt(propertyEncryptor.encrypt(plainText));
        assertTrue(plainText.equals(decryptedText));
    }

    @Test
    public void testDecryptByPropertyEncryptor() throws Exception {
        assertFalse(StringUtils.isEmpty(propertyHolder.getLocation()));
        assertFalse(StringUtils.isEmpty(propertyHolder.getFilename()));

        Map<?, ?> yamlMap = loadYaml(propertyHolder.getLocation() + propertyHolder.getFilename());
        assertTrue(yamlMap != null && yamlMap.size() > 0);

        Object isEncrypted = getProperty(yamlMap, "test.encryption.isEncrypted");
        if (isEncrypted != null && (Boolean) isEncrypted) {
            String password = Objects.toString(getProperty(yamlMap, "test.encryption.password"), null);
            assertFalse(StringUtils.isEmpty(password));
            String decryptedPassword = propertyEncryptor.decrypt(password);
            assertFalse(StringUtils.isEmpty(decryptedPassword));
        }
    }

}