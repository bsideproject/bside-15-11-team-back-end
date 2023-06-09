package com.beside.mamgwanboo.sign.common.service;

import com.beside.mamgwanboo.common.service.JwtService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import protobuf.common.type.OauthServiceType;
import protobuf.sign.MamgwanbooJwtPayload;
import protobuf.sign.SignRequest;
import protobuf.sign.SignResponse;
import reactor.core.publisher.Mono;

@Component
public class SignServiceProxy {
  private final List<AbstractSignService> abstractSignServices;
  private final JwtService jwtService;

  public SignServiceProxy(List<AbstractSignService> abstractSignServices, JwtService jwtService) {
    this.abstractSignServices = abstractSignServices;
    this.jwtService = jwtService;
  }

  public Mono<SignResponse> sign(SignRequest signRequest) {
    return Optional.ofNullable(getTargetService(signRequest.getOauthServiceType()))
        .map(abstractSignService -> abstractSignService.sign(
                signRequest.getOauthServiceType(),
                signRequest.getCode())
            .map(signResult -> SignResponse.newBuilder()
                .setJwt(jwtService.makeMamgwanbooJwt(MamgwanbooJwtPayload.newBuilder()
                    .setSequence(signResult.getUser().getSequence())
                    .build()))
                .setIsNewUser(signResult.isNewUser())
                .build())
        )
        .orElse(Mono.empty());
  }

  private AbstractSignService getTargetService(OauthServiceType oauthServiceType) {
    return abstractSignServices.stream()
        .filter(abstractSignService -> abstractSignService.isTargetService(oauthServiceType))
        .findFirst()
        .orElse(null);
  }
}
