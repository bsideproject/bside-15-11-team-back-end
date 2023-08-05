package com.beside.startrail.image.configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class ImageConfiguration {
  private static final String END_POINT = "https://kr.object.ncloudstorage.com";
  private static final String REGION = "kr-standard";

  private final String accessKey;
  private final String secretKey;

  public ImageConfiguration(
      @Value("${objectStorage.accessKey}") String accessKey,
      @Value("${objectStorage.secretKey}") String secretKey
  ) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
  }

  @Bean
  public S3AsyncClient s3AsyncClient() throws URISyntaxException {
    SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
        .writeTimeout(Duration.ZERO)
        .maxConcurrency(64)
        .build();
    S3Configuration serviceConfiguration = S3Configuration.builder()
        .checksumValidationEnabled(false)
        .chunkedEncodingEnabled(true)
        .build();

    return S3AsyncClient.builder().httpClient(httpClient)
        .region(software.amazon.awssdk.regions.Region.of(REGION))
        .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
        .serviceConfiguration(serviceConfiguration)
        .endpointOverride(new URI(END_POINT))
        .build();
  }
}
