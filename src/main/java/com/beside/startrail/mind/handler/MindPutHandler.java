package com.beside.startrail.mind.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.mind.MindProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.image.repository.ImageRepository;
import com.beside.startrail.image.service.ImageService;
import com.beside.startrail.mind.document.Item;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.mind.MindPutRequestProto;
import reactor.core.publisher.Mono;

@Component
public class MindPutHandler extends AbstractSignedTransactionalHandler {
  private final String bucketName;
  private final MindRepository mindRepository;
  private final ImageRepository imageRepository;

  public MindPutHandler(
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

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    AtomicReference<List<String>> keys = new AtomicReference<>(Lists.newArrayList());

    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<MindPutRequestProto>parse(
                body,
                MindPutRequestProto.newBuilder()
            )
        )
        .flatMap(mindPutRequestProto ->
            MindService
                .getBySequence(
                    super.jwtPayloadProto.getSequence(),
                    mindPutRequestProto.getSequence(),
                    YnType.Y
                )
                .execute(mindRepository)
                .map(Mind::getItem)
                .map(Item::getImageLink)
                .map(imageLink ->
                    Optional.ofNullable(
                            ImageService
                                .delete(
                                    bucketName,
                                    ImageService.getKey(imageLink)
                                )
                        )
                        .map(command ->
                            command.execute(imageRepository)
                        )
                )
                .thenReturn(mindPutRequestProto)
        )
        .flatMap(mindPutRequestProto ->
            Optional.ofNullable(
                    ImageService.create(
                        bucketName,
                        mindPutRequestProto.getItem().getImage().toByteArray(),
                        mindPutRequestProto.getItem().getName(),
                        mindPutRequestProto.getItem().getImageExtension()
                    )
                )
                .map(command ->
                    command
                        .execute(imageRepository)
                        .doOnNext(imageLink ->
                            keys.get().add(ImageService.getKey(imageLink))
                        )
                        .mapNotNull(imageLink ->
                            MindProtoUtil.toMindWithImageLink(
                                mindPutRequestProto,
                                imageLink,
                                super.jwtPayloadProto.getSequence(),
                                YnType.Y
                            )
                        )
                )
                .orElse(
                    Mono.justOrEmpty(
                        MindProtoUtil.toMindWithImageLink(
                            mindPutRequestProto,
                            "",
                            super.jwtPayloadProto.getSequence(),
                            YnType.Y
                        )
                    )
                )
                .map(MindService::update)
                .flatMap(command ->
                    command.execute(mindRepository)
                )
                .map(MindProtoUtil::toMindPutResponseProto)
                .map(ProtocolBufferUtil::print)
                .flatMap(body ->
                    ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                )
                .switchIfEmpty(
                    ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                )
                .onErrorMap(throwable -> {
                      if (!CollectionUtils.isEmpty(keys.get())) {
                        keys
                            .get()
                            .stream()
                            .map(ImageService::getKey)
                            .forEach((key) ->
                                Optional.ofNullable(
                                    ImageService
                                        .delete(bucketName, key)
                                ).map(command ->
                                    command.execute(imageRepository)
                                )
                            );
                      }

                      return throwable;
                    }
                )
        );
  }
}
