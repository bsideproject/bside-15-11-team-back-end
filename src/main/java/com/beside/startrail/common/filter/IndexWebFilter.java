package com.beside.startrail.common.filter;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Order(-1)
@Component
public class IndexWebFilter implements WebFilter {
  private final List<String> indexPaths;

  public IndexWebFilter(@Value("${oauth.redirectUri}") String redirectUri) {
    // todo 여기 임시
    this.indexPaths = List.of(
        "/",
        redirectUri,
        "/main",
        "/mind"
        );
  }

  @Override
  @NonNull
  public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    String requestPath = exchange.getRequest().getPath().value();

    if (indexPaths.contains(requestPath) || requestPath.startsWith("/page")) {
      return chain.filter(
          exchange.mutate()
              .request(
                  exchange
                      .getRequest()
                      .mutate()
                      .path("/index.html")
                      .build()
              )
              .build()
      );
    }

    return chain.filter(exchange);
  }
}
