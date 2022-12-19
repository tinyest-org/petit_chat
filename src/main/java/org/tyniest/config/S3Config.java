package org.tyniest.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.minio.MinioAsyncClient;

public class S3Config {
    @ConfigProperty(name = "s3.host")
    String host;
    
    @ConfigProperty(name = "s3.accessKey")
    String accessKey;

    @ConfigProperty(name = "s3.passkey")
    String passkey;


    public MinioAsyncClient makeS3() {
        return MinioAsyncClient.builder()
            .endpoint(host)
            .credentials(accessKey, passkey)
            .build();
    }
}
