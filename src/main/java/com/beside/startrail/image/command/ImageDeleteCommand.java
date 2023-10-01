package com.beside.startrail.image.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.image.repository.ImageRepository;
import reactor.core.publisher.Mono;

public class ImageDeleteCommand implements Command<Mono<Void>, ImageRepository> {
  private final String bucketName;
  private final String key;

  private Mono<Void> result;

  public ImageDeleteCommand(String bucketName, String key) {
    this.bucketName = bucketName;
    this.key = key;
  }

  @Override
  public Mono<Void> execute(ImageRepository imageRepository) {
    result = imageRepository.delete(bucketName, key);

    return result;
  }
}
