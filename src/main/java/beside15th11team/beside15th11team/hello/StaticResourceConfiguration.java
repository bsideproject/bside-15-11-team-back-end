package beside15th11team.beside15th11team.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.PathResourceResolver;

@Configuration
public class StaticResourceConfiguration implements WebFluxConfigurer {
	private final String staticPath;

	public StaticResourceConfiguration(@Value("${static.path}") String staticPath) {
		this.staticPath = staticPath;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
			.addResourceLocations("file:" + staticPath)
			.resourceChain(true)
			.addResolver(new PathResourceResolver());
	}
}
