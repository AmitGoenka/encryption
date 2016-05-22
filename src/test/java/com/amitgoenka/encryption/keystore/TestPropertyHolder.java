package com.amitgoenka.encryption.keystore;

import com.amitgoenka.encryption.EncryptionApplicationTests;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;

@Getter
@Setter
@Import(value = {EncryptionApplicationTests.class})
@ConfigurationProperties(prefix = "security.config")
class TestPropertyHolder {

    private String location;
    private String filename;
    private Keystore keystoreSecret;
    private Keystore keystoreSsl;

    @Getter
    @Setter
    public static class Keystore {
        private String type;
        private String filename;
        private String password;
        private MasterKey masterKey;

        @Getter
        @Setter
        public static class MasterKey {
            private String alias;
        }
    }

}