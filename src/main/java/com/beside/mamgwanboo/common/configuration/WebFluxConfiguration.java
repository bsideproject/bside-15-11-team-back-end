package com.beside.mamgwanboo.common.configuration;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.PathResourceResolver;

@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer {
  private final String staticPath;
  private final List<String> origins;

  public WebFluxConfiguration(
      @Value("${static.path}") String staticPath,
      @Value("${cors.origins}") List<String> origins
  ) {
    this.staticPath = staticPath;
    this.origins = origins;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
        .addResourceLocations("file:" + staticPath)
        .resourceChain(true)
        .addResolver(new PathResourceResolver());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        // todo 여기 임시임
        .addMapping("/**")
        .allowedOrigins(origins.toArray(String[]::new));
  }

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    configurer.defaultCodecs().jackson2JsonEncoder(
            new Jackson2JsonEncoder(Jackson2ObjectMapperBuilder.json().serializerByType(
                    Message.class, new JsonSerializer<Message>() {
                      @Override
                      public void serialize(Message message, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeRawValue(JsonFormat.printer().print(message));
                      }
                    }
            ).build())
    );
  }
}
