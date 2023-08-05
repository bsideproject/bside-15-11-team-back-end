package com.beside.startrail.image.service;

import com.beside.startrail.image.command.ImageDeleteCommand;
import com.beside.startrail.image.command.ImageSaveCommand;
import java.util.UUID;

public class ImageService {
  public static ImageSaveCommand save(String bucketName, byte[] image, String imageName, String fileExtension) {
    String key = makeKey(imageName, fileExtension);

    return new ImageSaveCommand(bucketName, image, key);
  }

  public static ImageDeleteCommand delete(String bucketName, String key) {
    return new ImageDeleteCommand(bucketName, key);
  }

  public static String getKey(String url) {
    int lastIndex = url.lastIndexOf("/");

    if (lastIndex == -1) {
      return "";
    }

    return url.substring(lastIndex + 1);
  }

  private static String makeKey(String imageName, String fileExtension) {
    return imageName + UUID.randomUUID() + "." + fileExtension;
  }
}
