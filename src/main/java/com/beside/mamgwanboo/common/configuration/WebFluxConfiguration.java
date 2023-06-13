package com.beside.mamgwanboo.common.configuration;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer {
  private final List<String> origins;

  public WebFluxConfiguration(
      @Value("${cors.origins}") List<String> origins
  ) {
    this.origins = origins;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        // todo 여기 임시임
        .addMapping("/**")
        .allowedOrigins(origins.toArray(String[]::new));
  }
}
