package beside15th11team.beside15th11team.hello;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.PathResourceResolver;

@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer {
  private final String staticPath;
  private final List<String> origins;

  public WebFluxConfiguration(@Value("${static.path}") String staticPath,
                              @Value("${cors.origins}") List<String> origins) {
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
        .addMapping("/**")
        .allowedOrigins(origins.toArray(String[]::new));
  }
}
