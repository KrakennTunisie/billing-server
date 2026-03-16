package com.example.billingservice.infrastructure.out.storage.cloud;

import com.example.billingservice.infrastructure.out.persistance.dto.CloudStoredObject;

public interface CloudObjectStorageClient{

    CloudStoredObject upload(
            String objectKey,
            byte[] content,
            String contentType
    );
}
