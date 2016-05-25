package com.amitgoenka.encryption;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.config")
public class TestPropertyHolder {

    private String location;
    private String filename;
    private Keystore keystoreSecret;
    private Keystore keystoreSsl;
    private Jasypt jasypt;

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

    @Getter
    @Setter
    public static class Jasypt {
        private String filename;
    }

}