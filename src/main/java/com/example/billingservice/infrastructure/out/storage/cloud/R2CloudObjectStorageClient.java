package com.example.billingservice.infrastructure.out.storage.cloud;

import com.example.billingservice.infrastructure.config.R2Properties;
import com.example.billingservice.infrastructure.out.persistance.dto.CloudStoredObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "cloud")
@ConditionalOnProperty(name = "spring.storage.cloud.provider", havingValue = "r2")
public class R2CloudObjectStorageClient implements CloudObjectStorageClient {
    private final S3Client s3Client;
    private final R2Properties r2Properties;

    public R2CloudObjectStorageClient(S3Client s3Client, R2Properties r2Properties) {
        this.s3Client = s3Client;
        this.r2Properties = r2Properties;
    }
    @Override
    public CloudStoredObject upload(String objectKey, byte[] content, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(r2Properties.getBucketName())
                .key(objectKey)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(content));

        return new CloudStoredObject(
                objectKey,
                buildUrl(objectKey)
        );
    }

    private String buildUrl(String objectKey) {
        if (r2Properties.getPublicBaseUrl() != null && !r2Properties.getPublicBaseUrl().isBlank()) {
            return r2Properties.getPublicBaseUrl() + "/" + objectKey;
        }

        return "r2://" + r2Properties.getBucketName() + "/" + objectKey;
    }

}
