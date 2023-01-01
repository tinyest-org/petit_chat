package org.tyniest.files.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.config.S3Config.S3Wrapper;
import org.tyniest.files.utils.FilePath;
import org.tyniest.files.utils.S3Exception;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class FileService {

    // should be only ref to minio and rest should be agnostic
    private final MinioAsyncClient minioClient;

    // /** Default maxSize for the upload, 1 MiB as of now */
    // private int defaultMaxSize = 1 * 1024 * 1024;

    public FileService(S3Wrapper wrapper) {
        this.minioClient = wrapper.getClient();
    }

    public Uni<ObjectWriteResponse> uploadFile(final File file, final FilePath to)
            throws S3Exception {
        try (final var is = new FileInputStream(file)) {
            return toUni(this.minioClient.putObject(
                    PutObjectArgs.builder().bucket(to.getBucket()).object(to.getPath()).stream(
                            is, file.length(), -1)
                            .build()));
        } catch (Exception e) {
            throw new S3Exception();
        }
    }

    public Uni<Void> deleteFile(final FilePath path) // should never be used
            throws S3Exception {
        try {
            return toUni(this.minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(path.getBucket()).object(path.getPath()).build()));
        } catch (Exception e) {
            throw new S3Exception();
        }
    }

    protected <T> Uni<T> toUni(final CompletableFuture<T> future) {
        return Uni.createFrom().completionStage(future);
    }

    public String getDownloadUrl(final FilePath path)
            throws S3Exception {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(path.getBucket())
                            .object(path.getPath())
                            .expiry(1, TimeUnit.MINUTES)
                            .build());
        } catch (Exception e) {
            throw new S3Exception();
        }
    }
}
