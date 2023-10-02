package com.beside.startrail.image.service;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.image.command.ImageDeleteCommand;
import com.beside.startrail.image.command.ImageSaveCommand;
import com.beside.startrail.image.repository.ImageRepository;
import io.jsonwebtoken.lang.Objects;
import io.micrometer.common.util.StringUtils;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class ImageService {
  public static Command<Mono<String>, ImageRepository> create(
      String bucketName,
      byte[] image,
      String imageName,
      String fileExtension
  ) {
    if (isInValidToCreate(image, imageName, fileExtension)) {
      return null;
    }

    String key = makeKey(imageName, fileExtension);

    return new ImageSaveCommand(bucketName, image, key);
  }

  public static Command<Mono<Void>, ImageRepository> delete(String bucketName, String key) {
    if (StringUtils.isBlank(key)) {
      return null;
    }

    return new ImageDeleteCommand(bucketName, key);
  }

  public static String getKey(String url) {
    int lastIndex = url.lastIndexOf("/");

    if (lastIndex == -1) {
      return "";
    }

    return url.substring(lastIndex + 1);
  }

  private static boolean isInValidToCreate(byte[] image, String imageName, String fileExtension) {
    return Objects.isEmpty(image)
        || StringUtils.isBlank(imageName)
        || StringUtils.isBlank(fileExtension);
  }

  private static String makeKey(String imageName, String fileExtension) {
    return imageName + UUID.randomUUID() + "." + fileExtension;
  }
}
