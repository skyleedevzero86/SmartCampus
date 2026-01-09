package com.sleekydz86.server.global.util;

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
    @DisplayName("사용자명 암호화 테스트")
    void encryptUsername() {
        String username = "root";

        String encrypted = encryptor.encrypt(username);
        String decrypted = encryptor.decrypt(encrypted);

        System.out.println("Original: " + username);
        System.out.println("Encrypted: ENC(" + encrypted + ")");
        System.out.println("Decrypted: " + decrypted);

        assertThat(decrypted).isEqualTo(username);
    }

    @Test
    @DisplayName("비밀번호 암호화 테스트")
    void encryptPassword() {
        String password = "root";

        String encrypted = encryptor.encrypt(password);
        String decrypted = encryptor.decrypt(encrypted);

        System.out.println("Original: " + password);
        System.out.println("Encrypted: ENC(" + encrypted + ")");
        System.out.println("Decrypted: " + decrypted);

        assertThat(decrypted).isEqualTo(password);
    }

    @Test
    @DisplayName("MinIO Endpoint 암호화 테스트")
    void encryptMinioEndpoint() {
        String endpoint = "http://localhost:9000";

        String encrypted = encryptor.encrypt(endpoint);
        String decrypted = encryptor.decrypt(encrypted);

        System.out.println("Original: " + endpoint);
        System.out.println("Encrypted: ENC(" + encrypted + ")");
        System.out.println("Decrypted: " + decrypted);

        assertThat(decrypted).isEqualTo(endpoint);
    }

    @Test
    @DisplayName("MinIO AccessKey 암호화 테스트")
    void encryptMinioAccessKey() {
        String accessKey = "minioadmin";

        String encrypted = encryptor.encrypt(accessKey);
        String decrypted = encryptor.decrypt(encrypted);

        System.out.println("Original: " + accessKey);
        System.out.println("Encrypted: ENC(" + encrypted + ")");
        System.out.println("Decrypted: " + decrypted);

        assertThat(decrypted).isEqualTo(accessKey);
    }

    @Test
    @DisplayName("MinIO SecretKey 암호화 테스트")
    void encryptMinioSecretKey() {
        String secretKey = "minioadmin";

        String encrypted = encryptor.encrypt(secretKey);
        String decrypted = encryptor.decrypt(encrypted);

        System.out.println("Original: " + secretKey);
        System.out.println("Encrypted: ENC(" + encrypted + ")");
        System.out.println("Decrypted: " + decrypted);

        assertThat(decrypted).isEqualTo(secretKey);
    }

    @Test
    @DisplayName("MinIO Bucket 암호화 테스트")
    void encryptMinioBucket() {
        String bucket = "market-local";

        String encrypted = encryptor.encrypt(bucket);
        String decrypted = encryptor.decrypt(encrypted);

        System.out.println("Original: " + bucket);
        System.out.println("Encrypted: ENC(" + encrypted + ")");
        System.out.println("Decrypted: " + decrypted);

        assertThat(decrypted).isEqualTo(bucket);
    }

    @Test
    @DisplayName("Redis Host 암호화 테스트")
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
