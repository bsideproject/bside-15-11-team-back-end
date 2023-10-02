package com.beside.startrail.image.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.image.command.ImageDeleteCommand;
import com.beside.startrail.image.command.ImageSaveCommand;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ImageServiceTest {
  @ParameterizedTest
  @MethodSource("provideInvalidCreateParameter")
  void createWhenInvalid(CreateParameter createParameter) {
    // given
    String bucketName = createParameter.bucketName;
    byte[] image = createParameter.image;
    String imageName = createParameter.imageName;
    String fileExtension = createParameter.fileExtension;

    // when
    Command command = ImageService.create(bucketName, image, imageName, fileExtension);

    // then
    assertThat(command).isNull();
  }

  static List<CreateParameter> provideInvalidCreateParameter() {
    return List.of(
        new CreateParameter("bucketName", null, "imageName", "fileExtension"),
        new CreateParameter("bucketName", new byte[] {}, "imageName", "fileExtension"),
        new CreateParameter("bucketName", new byte[] {1, 2, 3}, null, "fileExtension"),
        new CreateParameter("bucketName", new byte[] {1, 2, 3}, "", "fileExtension"),
        new CreateParameter("bucketName", new byte[] {1, 2, 3}, "imageName", null),
        new CreateParameter("bucketName", new byte[] {1, 2, 3}, "imageName", "")
    );
  }

  @Test
  void create() {
    // given
    String bucketName = "bucketName";
    byte[] image = new byte[] {1, 2, 3};
    String imageName = "imageName";
    String fileExtension = "fileExtension";

    // when
    Command command = ImageService.create(bucketName, image, imageName, fileExtension);

    // then
    assert command instanceof ImageSaveCommand;
  }

  @ParameterizedTest
  @MethodSource("provideBlankKey")
  void deleteWhenBlankKey(String key) {
    // given
    String bucketName = "bucketName";

    // when
    Command command = ImageService.delete(bucketName, key);

    // then
    assertThat(command).isNull();
  }

  @Test
  void delete() {
    // given
    String bucketName = "bucketName";
    String key = "key";

    // when
    Command command = ImageService.delete(bucketName, key);

    // then
    assert command instanceof ImageDeleteCommand;
  }

  @Test
  void getKeyWhenInvalidUrl() {
    // given
    String url = "worngUrl";

    // when
    String result = ImageService.getKey(url);

    // then
    assertThat(result).isBlank();
  }

  @Test
  void getKey() {
    // given
    String url = "http://test.test/this/is/key";

    // when
    String key = ImageService.getKey(url);

    // then
    assertThat(key).isEqualTo("key");
  }

  static List<String> provideBlankKey() {
    ArrayList<String> keys = new ArrayList<>();
    keys.add(null);
    keys.add("");

    return keys;
  }

  @RequiredArgsConstructor
  private static class CreateParameter {
    private final String bucketName;
    private final byte[] image;
    private final String imageName;
    private final String fileExtension;
  }
}
