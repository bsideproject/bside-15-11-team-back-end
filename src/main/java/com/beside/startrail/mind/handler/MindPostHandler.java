package com.beside.startrail.mind.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.mind.MindProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.image.command.ImageDeleteCommand;
import com.beside.startrail.image.repository.ImageRepository;
import com.beside.startrail.image.service.ImageService;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import io.micrometer.common.util.StringUtils;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.mind.MindPostRequestProto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MindPostHandler extends AbstractSignedTransactionalHandler {
  private final String bucketName;
  private final MindRepository mindRepository;
  private final ImageRepository imageRepository;
  private String key;

  public MindPostHandler(
      @Value("${sign.attributeName}") String attributeName,
      @Value("${objectStorage.bucketName}") String bucketName,
      MindRepository mindRepository,
      ImageRepository imageRepository
  ) {
    super(attributeName);
    this.bucketName = bucketName;
    this.mindRepository = mindRepository;
    this.imageRepository = imageRepository;
  }

// todo
// @TransactionalEventListener

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<MindPostRequestProto>parse(
                body,
                MindPostRequestProto.newBuilder()
            )
        )
        .map(MindPostRequestProto::getMindsList)
        .flatMap(mindRequestProtos ->
            Flux.fromIterable(mindRequestProtos)
                .flatMap(mindRequestProto ->
                    Optional.ofNullable(
                            ImageService.create(
                                bucketName,
                                mindRequestProto.getItem().getImage().toByteArray(),
                                mindRequestProto.getItem().getName(),
                                mindRequestProto.getItem().getImageExtension()
                            )
                        )
                        .map(imageSaveCommand ->
                            imageSaveCommand
                                .execute(imageRepository)
                                .doOnNext(imageLink -> key = imageLink)
                                .mapNotNull(imageLink ->
                                    MindProtoUtil.toMindWithImageLink(
                                        mindRequestProto,
                                        imageLink,
                                        super.jwtPayloadProto.getSequence(),
                                        YnType.Y
                                    )
                                )
                        )
                        .orElse(
                            Mono.justOrEmpty(
                                MindProtoUtil.toMindWithImageLink(
                                    mindRequestProto,
                                    "",
                                    super.jwtPayloadProto.getSequence(),
                                    YnType.Y
                                )
                            )
                        )
                )
                .collectList()
        )
        .flatMap(minds -> MindService.create(minds)
            .execute(mindRepository)
            .map(MindProtoUtil::toMindResponseProto)
            .map(ProtocolBufferUtil::print)
            .collect(Collectors.joining())
        )
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        )
        .onErrorMap(throwable -> {
              if (!StringUtils.isBlank(key)) {
                new ImageDeleteCommand(bucketName, key)
                    .execute(imageRepository);
              }

              return throwable;
            }
        );
  }
}
