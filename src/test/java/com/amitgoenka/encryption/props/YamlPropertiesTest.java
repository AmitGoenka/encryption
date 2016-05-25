package com.amitgoenka.encryption.props;

import com.amitgoenka.encryption.EncryptionApplicationTests;
import com.amitgoenka.encryption.TestPropertyHolder;
import com.amitgoenka.encryption.props.YamlUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                EncryptionApplicationTests.class,
                TestPropertyHolder.class
        }
)
public class YamlPropertiesTest {

    @Autowired
    TestPropertyHolder propertyHolder;

    @Test
    public void yamlLoaderTest() throws FileNotFoundException {
        Map<?, ?> yamlMap = YamlUtil.loadYaml(propertyHolder.getLocation() + propertyHolder.getFilename());

        String password = (String) YamlUtil.getProperty(yamlMap, "test.encryption.password");
        Assert.assertFalse(StringUtils.isEmpty(password));

        boolean isEncrypted = (Boolean) YamlUtil.getProperty(yamlMap, "test.encryption.isEncrypted");
        Assert.assertFalse(StringUtils.isEmpty(isEncrypted));
    }
}
