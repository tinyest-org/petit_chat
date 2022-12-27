package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.minio.MinioAsyncClient;
import lombok.Value;


public class S3Config {
    @ConfigProperty(name = "s3.host")
    String host;
    
    @ConfigProperty(name = "s3.accessKey")
    String accessKey;

    @ConfigProperty(name = "s3.passkey")
    String passkey;

    // @ConfigProperty(name = "seaweed.master", defaultValue = "")
    // String seaweedMaster;

    
    @Value(staticConstructor = "of")
    public static class S3Wrapper {
        protected MinioAsyncClient client;
    }

    @Named("s3Client")
    @ApplicationScoped
    public S3Wrapper makeS3() {
        return S3Wrapper.of(MinioAsyncClient.builder()
            .endpoint(host)
            .credentials(accessKey, passkey)
            .build());
    }

    // @ApplicationScoped
    // public SeaweedClient makeFiler() {
    //     return new SeaweedClient(this.seaweedMaster);
    // }
}
