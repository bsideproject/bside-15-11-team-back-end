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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.mind.MindPutRequestProto;
import reactor.core.publisher.Mono;

@Component
public class MindPutHandler extends AbstractSignedTransactionalHandler {
  private final String bucketName;
  private final MindRepository mindRepository;
  private final ImageRepository imageRepository;
  private String key;

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
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<MindPutRequestProto>parse(
                body,
                MindPutRequestProto.newBuilder()
            )
        )
        .flatMap(mindPutRequestProto ->
            Optional.ofNullable(
                    ImageService.save(
                        bucketName,
                        mindPutRequestProto.getItem().getImage().toByteArray(),
                        mindPutRequestProto.getItem().getName(),
                        mindPutRequestProto.getItem().getImageExtension()
                    )
                )
                .map(imageSaveCommand ->
                    imageSaveCommand
                        .execute(imageRepository)
                        .doOnNext(imageLink -> key = imageLink)
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
                .flatMap(mindSaveOneCommand ->
                    mindSaveOneCommand.execute(mindRepository)
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
                      if (!StringUtils.isBlank(key)) {
                        new ImageDeleteCommand(bucketName, key)
                            .execute(imageRepository);
                      }

                      return throwable;
                    }
                )
        );
  }
}
