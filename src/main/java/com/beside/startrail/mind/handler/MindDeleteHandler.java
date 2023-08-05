package com.beside.startrail.mind.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.mind.MindProtoUtil;
import com.beside.startrail.image.repository.ImageRepository;
import com.beside.startrail.image.service.ImageService;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MindDeleteHandler extends AbstractSignedHandler {
  private final String bucketName;
  private final MindRepository mindRepository;
  private final ImageRepository imageRepository;
  private String key;

  public MindDeleteHandler(
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
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return MindService
        .removeByUserSequenceAndSequence(
            super.jwtPayloadProto.getSequence(),
            sequence
        )
        .execute(mindRepository)
        .doOnNext(mind -> key = mind.getItem().getImageLink())
        .doOnNext(mind ->
            Optional.ofNullable(
                    ImageService.delete(
                        bucketName,
                        ImageService.getKey(mind.getItem().getImageLink())
                    )
                )
                .map(imageDeleteCommand -> imageDeleteCommand.execute(imageRepository))
        )
        .map(MindProtoUtil::toMindResponseProto)
        .map(ProtocolBufferUtil::print)
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body))
        .switchIfEmpty(
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build()
        );
  }
}
