package com.example.billingservice.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "cloud")
@ConditionalOnProperty(name = "spring.storage.cloud.provider", havingValue = "r2")
public class R2Config {

    @Bean
    public S3Client r2S3Client(R2Properties properties) {
        String endpoint = "https://" + properties.getAccountId() + ".r2.cloudflarestorage.com";

        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        properties.getAccessKeyId(),
                                        properties.getSecretAccessKey()
                                )
                        )
                )
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .build()
                )
                .build();
    }
}
