package com.beside.startrail.image.command;

import com.beside.startrail.image.repository.ImageRepository;
import reactor.core.publisher.Mono;

public class ImageDeleteCommand {
  private final String bucketName;
  private final String key;

  private Mono<Void> result;

  public ImageDeleteCommand(String bucketName, String key) {
    this.bucketName = bucketName;
    this.key = key;
  }

  public Mono<Void> execute(ImageRepository imageRepository) {
    result = imageRepository.delete(bucketName, key);

    return result;
  }
}
