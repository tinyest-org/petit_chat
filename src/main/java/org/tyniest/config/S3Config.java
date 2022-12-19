package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.minio.MinioAsyncClient;
import seaweedfs.client.FilerClient;

public class S3Config {
    @ConfigProperty(name = "s3.host")
    String host;
    
    @ConfigProperty(name = "s3.accessKey")
    String accessKey;

    @ConfigProperty(name = "s3.passkey")
    String passkey;

    @ConfigProperty(name = "s3.filer", defaultValue = "")
    String seaweedFiler;

    @ApplicationScoped
    public MinioAsyncClient makeS3() {
        return MinioAsyncClient.builder()
            .endpoint(host)
            .credentials(accessKey, passkey)
            .build();
    }

    @ApplicationScoped
    public FilerClient makeFiler() { // TODO: not used at the moment
        final var filerClient = new FilerClient("localhost", 18888);
        return filerClient;
    }
}
