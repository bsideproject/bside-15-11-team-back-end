package com.beside.startrail.image.command;

import com.beside.startrail.image.repository.ImageRepository;
import reactor.core.publisher.Mono;

public class ImageSaveCommand {
  private final String bucketName;
  private final byte[] image;
  private final String key;

  private Mono<String> result;

  public ImageSaveCommand(String bucketName, byte[] image, String key) {
    this.bucketName = bucketName;
    this.image = image;
    this.key = key;
  }

  public Mono<String> execute(ImageRepository imageRepository) {
    result = imageRepository
        .save(bucketName, image, key)
        .thenReturn(makeImageUrl());

    return result;
  }

  private String makeImageUrl() {
    return String.format("https://kr.object.ncloudstorage.com/%s/%s", bucketName, key);
  }
}
