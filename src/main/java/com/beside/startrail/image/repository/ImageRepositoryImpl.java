package com.beside.startrail.image.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
  private final S3AsyncClient s3AsyncClient;

  public ImageRepositoryImpl(
      S3AsyncClient s3AsyncClient
  ) {
    this.s3AsyncClient = s3AsyncClient;
  }

  @Override
  public Mono<Void> save(String bucketName, byte[] image, String key) {
    return Mono.fromFuture(
            s3AsyncClient
                .putObject(
                    PutObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .contentLength((long) image.length)
                        .key(key)
                        .build(),
                    AsyncRequestBody.fromBytes(image)
                )
        )
        .then();
  }

  @Override
  public Mono<Void> delete(String bucketName, String key) {
    return Mono.fromFuture(
            s3AsyncClient
                .deleteObject(
                    DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
                )
        )
        .then();
  }
}
