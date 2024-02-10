package com.beside.startrail.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
public class SecurityConfiguration {
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
    return serverHttpSecurity
        .redirectToHttps()
        .and()
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .logout(ServerHttpSecurity.LogoutSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .headers(ServerHttpSecurity.HeaderSpec::disable)
        .authorizeExchange(authorizeExchangeSpec ->
            authorizeExchangeSpec.anyExchange().permitAll()
        )
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .build();
  }
}