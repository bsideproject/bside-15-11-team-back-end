package com.beside.startrail.common.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer {
  private final List<String> origins;
  private final String staticPath;

  public WebFluxConfiguration(
      @Value("${cors.origins}") List<String> origins,
      @Value("${static.path}") String staticPath
  ) {
    this.origins = origins;
    this.staticPath = staticPath;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins(origins.toArray(String[]::new));
  }

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    configurer.defaultCodecs().jackson2JsonEncoder(
        new Jackson2JsonEncoder(Jackson2ObjectMapperBuilder.json().serializerByType(
            Message.class, new JsonSerializer<Message>() {
              @Override
              public void serialize(
                  Message message,
                  JsonGenerator jsonGenerator,
                  SerializerProvider serializerProvider
              ) throws IOException {
                jsonGenerator.writeRawValue(JsonFormat.printer().print(message));
              }
            }
        ).build())
    );
  }

  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    WebFluxConfigurer.super.addResourceHandlers(registry);

    registry.addResourceHandler("/**")
        .addResourceLocations("file:" + staticPath);
  }
}
