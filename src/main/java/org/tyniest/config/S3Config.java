package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.utils.seaweed.SeaweedClient;

import io.minio.MinioAsyncClient;


public class S3Config {
    @ConfigProperty(name = "s3.host")
    String host;
    
    @ConfigProperty(name = "s3.accessKey")
    String accessKey;

    @ConfigProperty(name = "s3.passkey")
    String passkey;

    @ConfigProperty(name = "seaweed.master", defaultValue = "")
    String seaweedMaster;

    @Named("s3Client")
    @ApplicationScoped
    public MinioAsyncClient makeS3() {
        return MinioAsyncClient.builder()
            .endpoint(host)
            .credentials(accessKey, passkey)
            .build();
    }

    @ApplicationScoped
    public SeaweedClient makeFiler() {
        return new SeaweedClient(this.seaweedMaster);
    }
}
