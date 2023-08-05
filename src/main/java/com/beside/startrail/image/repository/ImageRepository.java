package com.beside.startrail.image.repository;

import reactor.core.publisher.Mono;

public interface ImageRepository {
  Mono<Void> save(String bucketName, byte[] image, String key);

  Mono<Void> delete(String bucketName, String key);
}
