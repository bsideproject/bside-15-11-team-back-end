package com.beside.startrail.common.filter;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class IndexWebFilter implements WebFilter {
  @Override
  @NonNull
  public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    String requestPath = exchange.getRequest().getPath().value();

    if ("/".equals(requestPath)) {
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
