package com.sleekydz86.chat.global.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Jasypt 암호화/복호화 테스트")
class JasyptUtilTest {

    private PooledPBEStringEncryptor encryptor;

    @BeforeEach
    void setUp() {
        String encryptKey = System.getenv("ENCRYPT_KEY");
        if (encryptKey == null || encryptKey.isEmpty()) {
            encryptKey = "test-encrypt-key";
        }

        encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(encryptKey);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations(1000);
        config.setPoolSize(1);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
    }

    @Test
    @DisplayName("문자열 암호화 및 복호화 테스트")
    void encryptAndDecrypt() {
        String plainText = "test-password-123";

        String encrypted = encryptor.encrypt(plainText);
        String decrypted = encryptor.decrypt(encrypted);

        assertThat(encrypted).isNotEqualTo(plainText);
        assertThat(decrypted).isEqualTo(plainText);
    }

    @Test
    @DisplayName("DB URL 암호화 테스트")
    void encryptDatabaseUrl() {
        String dbUrl = "jdbc:mysql://localhost:3306/market?useSSL=false&serverTimezone=UTC";

        String encrypted = encryptor.encrypt(dbUrl);
        String decrypted = encryptor.decrypt(encrypted);

        System.out.println("Original: " + dbUrl);
        System.out.println("Encrypted: ENC(" + encrypted + ")");
        System.out.println("Decrypted: " + decrypted);

        assertThat(decrypted).isEqualTo(dbUrl);
    }

    @Test
    @DisplayName("Redis 호스트 암호화 테스트")
    void encryptRedisHost() {
        String redisHost = "localhost";

        String encrypted = encryptor.encrypt(redisHost);
        String decrypted = encryptor.decrypt(encrypted);

        System.out.println("Original: " + redisHost);
        System.out.println("Encrypted: ENC(" + encrypted + ")");
        System.out.println("Decrypted: " + decrypted);

        assertThat(decrypted).isEqualTo(redisHost);
    }
}
