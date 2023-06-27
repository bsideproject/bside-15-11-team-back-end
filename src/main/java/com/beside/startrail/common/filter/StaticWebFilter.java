package com.beside.startrail.common.filter;

import com.google.common.base.Charsets;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Component
@Order(-1)
public class StaticWebFilter implements WebFilter {
  private final List<String> indexWhiteList;
  private final String staticPath;

  public StaticWebFilter(
      @Value("${static.path}") String staticPath,
      @Value("${oauth.redirectUri}") String oauthRedirectUri
  ) {
    this.staticPath = staticPath;

    this.indexWhiteList = List.of(
        "/",
        extractPath(oauthRedirectUri)
    );
  }

  @Override
  public @NonNull Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    String requestPath = UriUtils.decode(exchange.getRequest().getPath().value(), Charsets.UTF_8);

    if (!requestPath.startsWith("/api")) {
      if (indexWhiteList.contains(requestPath)) {
        requestPath = "/index.html";
      }

      String filePath = staticPath + requestPath;

      return serveFile(exchange, filePath);
    }

    return chain.filter(exchange);
  }

  private Mono<Void> serveFile(ServerWebExchange exchange, String filePath) {
    Resource resource = new FileSystemResource(filePath);
    if (resource.exists() && resource.isReadable()) {
      ServerHttpResponse response = exchange.getResponse();
      response.setStatusCode(HttpStatus.OK);

      MediaType contentType = exchange.getRequest().getHeaders().getContentType();
      if (Objects.nonNull(contentType)) {
        response.getHeaders()
            .set(
                HttpHeaders.CONTENT_TYPE,
                contentType.getType());
      }

      return response.writeWith(
          DataBufferUtils.read(
              resource,
              new DefaultDataBufferFactory(),
              4096
          )
      );
    }

    return Mono.error(
        new IllegalStateException(
            String.format("static 파일을 주는데 이상한 상태입니다. request: %s", exchange.getRequest())
        )
    );
  }

  private String extractPath(String uri) {
    return UriComponentsBuilder
        .fromUriString(uri)
        .build()
        .getPath();
  }
}
