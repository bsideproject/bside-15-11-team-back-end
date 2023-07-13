package com.beside.startrail.sign.apple.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.sign.apple.model.AppleAccessTokenRequest;
import com.beside.startrail.sign.apple.model.AppleAccessTokenResponse;
import com.beside.startrail.sign.apple.model.AppleIdToken;
import com.beside.startrail.sign.common.command.AbstractSignCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.model.UserInformation;
import com.beside.startrail.user.type.OauthServiceType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AppleSignCommand extends AbstractSignCommand {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private final WebClient webClient;
  private final URI accessTokenUri;
  private final String redirectUri;
  private final String key;
  private final String teamId;
  private final String clientId;
  private final String audience;
  private final String keyPath;

  public AppleSignCommand(
      WebClient webClient,
      String accessTokenUri,
      String code,
      String redirectUri,
      String key,
      String teamId,
      String clientId,
      String audience,
      String keyPath
  ) {
    super(OauthServiceType.APPLE, code);
    this.webClient = webClient;
    this.teamId = teamId;
    this.clientId = clientId;
    this.audience = audience;
    this.keyPath = keyPath;

    try {
      this.accessTokenUri = new URI(accessTokenUri);
    } catch (URISyntaxException uriSyntaxException) {
      throw new IllegalArgumentException(
          String.format("accessTokenUri가 잘못되었습니다. accessTokenUri: %s", accessTokenUri));
    }

    this.redirectUri = redirectUri;
    this.key = key;
  }

  @Override
  protected Mono<User> makeUser() {
    return Mono.just(System.currentTimeMillis())
        .map(nowMilli -> {
          try {
            return Jwts.builder()
                .setHeaderParam("alg", SignatureAlgorithm.ES256.getValue())
                .setHeaderParam("kid", key)
                .setIssuer(teamId)
                .setIssuedAt(new Date(nowMilli))
                .setExpiration(new Date(nowMilli + Duration.ofMinutes(5).toMillis()))
                .setAudience(audience)
                .setSubject(clientId)
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
          } catch (Exception exception) {
            throw new IllegalStateException("privateKey 생성에 실패했습니다.", exception);
          }
        })
        .map(clientSecret ->
            AppleAccessTokenRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .code(code)
                .build()
        )
        .flatMap(appleAccessTokenRequest ->
            webClient
                .post()
                .uri(uriBuilder ->
                    uriBuilder
                        .scheme(accessTokenUri.getScheme())
                        .host(accessTokenUri.getHost())
                        .path(accessTokenUri.getPath())
                        .build()
                )
                .bodyValue(appleAccessTokenRequest.toMultiValueMap())
                .retrieve()
                .bodyToMono(AppleAccessTokenResponse.class)
                .map(AppleAccessTokenResponse::getData)
                .map(AppleAccessTokenResponse.Data::getIdToken)
        )
        .map(idToken ->
            Jwts.parserBuilder()
                .build()
                .parseClaimsJws(idToken)
                .getBody()
        )
        .<String>handle((claims, sink) -> {
          try {
            sink.next(OBJECT_MAPPER.writeValueAsString(claims));
          } catch (JsonProcessingException jsonProcessingException) {
            sink.error(new IllegalArgumentException(
                String.format("jwt payload의 json 변환에 실패했습니다. jwt: %s", claims),
                jsonProcessingException
            ));
          }
        })
        .map(appleIdtoken -> {
          try {
            return OBJECT_MAPPER.readValue(appleIdtoken, AppleIdToken.class);
          } catch (JsonProcessingException jsonProcessingException) {
            throw new IllegalArgumentException(
                String.format("apple oauth 응답이 이상합니다. appleIdtoken: %s", appleIdtoken)
            );
          }
        })
        .map(appleIdToken ->
            User.builder()
                .userId(
                    UserId.builder()
                        .oauthServiceType(OauthServiceType.APPLE)
                        .serviceUserId(appleIdToken.getEmail())
                        .build()
                )
                .sequence(UUID.randomUUID().toString())
                .userInformation(
                    UserInformation.builder()
                        .profileNickname(appleIdToken.getEmail())
                        .build()
                )
                .useYn(YnType.Y)
                .build()
        );
  }

  private PrivateKey getPrivateKey() throws Exception {
//    String path = new ClassPathResource(keyPath).getFile().getAbsolutePath();

    Resource resource = new ClassPathResource(keyPath);
    InputStream is = resource.getInputStream();
    File file = File.createTempFile("KEY_TEMP", System.currentTimeMillis() + ".p8");
    OutputStream outputStream = new FileOutputStream(file);
    IOUtils.copy(is, outputStream);

//    PEMParser pemParser = new PEMParser(new FileReader(path));
    PEMParser pemParser = new PEMParser(new FileReader(file));
    JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
    PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
    PrivateKey privateKey = jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);

    return privateKey;
  }
}
