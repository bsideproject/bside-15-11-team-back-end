package com.beside.startrail.mind.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.mind.MindProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.mind.MindPostRequestProto;
import reactor.core.publisher.Mono;

@Component
public class MindPostHandler extends AbstractSignedTransactionalHandler {
  private final MindRepository mindRepository;

  public MindPostHandler(
      @Value("${sign.attributeName}") String attributeName,
      MindRepository mindRepository
  ) {
    super(attributeName);
    this.mindRepository = mindRepository;
  }

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
        .map(mindRequestProtos ->
            mindRequestProtos.stream()
                .map(relationshipDto ->
                    MindProtoUtil.toMind(
                        relationshipDto,
                        super.jwtPayloadProto.getSequence(),
                        YnType.Y
                    )
                )
                .collect(Collectors.toList())
        )
        .flatMap(minds -> MindService.save(minds)
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
        );
  }
}
