package com.beside.startrail.common.filter;

import com.beside.startrail.common.service.JwtProtoService;
import com.google.common.base.Charsets;
import com.mongodb.lang.NonNull;
import io.jsonwebtoken.security.SignatureException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

@Component
public class SignWebFilter implements WebFilter {
  private static final String API_PATH_PREFIX = "/api";
  private final List<String> whitePaths;
  private final String attributeName;
  private final JwtProtoService jwtService;

  public SignWebFilter(
      @Value("${oauth.kakao.redirectUri}") String kakaoRedirectUri,
      @Value("${oauth.apple.redirectUri}") String appleRedirectUri,
      @Value("${sign.attributeName}") String attributeName,
      JwtProtoService jwtService
  ) {
    this.whitePaths = Stream.of(kakaoRedirectUri, appleRedirectUri)
        .map(redirectUri -> {
          try {
            return new URI(redirectUri);
          } catch (URISyntaxException uriSyntaxException) {
            throw new IllegalArgumentException(
                String.format("redirectUri가 이상합니다. redirectUri: %s", redirectUri)
            );
          }
        })
        .map(URI::getPath)
        .map(path -> path.replace(API_PATH_PREFIX, ""))
        .collect(Collectors.toList());
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

    List<String> authorizations = exchange.getRequest().getHeaders().get("Authorization");

    if (Objects.isNull(authorizations) || authorizations.isEmpty()) {
      ServerHttpResponse serverHttpResponse = exchange.getResponse();
      serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
      return serverHttpResponse.setComplete();
    }

    for (String authorization : authorizations) {
      try {
        return jwtService.getPayload(authorization)
            .flatMap(jwtPayload -> {
              exchange.getAttributes().put(
                  attributeName,
                  jwtPayload
              );

              return chain.filter(exchange);
            });
      } catch (IllegalArgumentException | SignatureException ignored) {
      }
    }

    ServerHttpResponse serverHttpResponse = exchange.getResponse();
    serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
    return serverHttpResponse.setComplete();
  }
}
