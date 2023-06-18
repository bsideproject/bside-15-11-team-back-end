package com.beside.mamgwanboo.common.filter;

import com.beside.mamgwanboo.common.service.JwtService;
import com.google.common.base.Charsets;
import com.mongodb.lang.NonNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

@Component
public class SignWebFilter implements WebFilter {
  private static final String API_PATH_PREFIX = "/api";
  private final List<String> whitePaths;
  private final String cookieName;
  private final String attributeName;
  private final JwtService jwtService;

  public SignWebFilter(
      @Value("${sign.whitePaths}") List<String> whitePaths,
      @Value("${sign.cookieName}") String cookieName,
      @Value("${sign.attributeName}") String attributeName,
      JwtService jwtService
  ) {
    this.whitePaths = whitePaths;
    this.cookieName = cookieName;
    this.attributeName = attributeName;
    this.jwtService = jwtService;
  }

  @SuppressWarnings("checkstyle:EmptyCatchBlock")
  @NonNull
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    String requestPath = UriUtils.decode(exchange.getRequest().getPath().value(), Charsets.UTF_8);

    if (!requestPath.startsWith(API_PATH_PREFIX)) {
      return chain.filter(exchange);
    }

    if (whitePaths.contains(requestPath.replace(API_PATH_PREFIX, ""))) {
      return chain.filter(exchange);
    }

    MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();

    if (cookies.isEmpty() || !cookies.containsKey(cookieName)) {
      ServerHttpResponse serverHttpResponse = exchange.getResponse();
      serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
      return serverHttpResponse.setComplete();
    }

    List<HttpCookie> jwtCookies = cookies.get(cookieName);
    for (HttpCookie jwtCookie : jwtCookies) {
      try {
        return jwtService.getPayload(jwtCookie.getValue())
            .flatMap(mamgwanbooJwtPayload -> {
              exchange.getAttributes().put(
                  attributeName,
                  mamgwanbooJwtPayload
              );

              return Mono.empty();
            });
      } catch (IllegalArgumentException ignored) {
      }
    }

    ServerHttpResponse serverHttpResponse = exchange.getResponse();
    serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
    return serverHttpResponse.setComplete();
  }
}
