package com.beside.startrail.mind.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.mind.MindProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.mind.MindPutRequestProto;
import reactor.core.publisher.Mono;

@Component
public class MindPutHandler extends AbstractSignedTransactionalHandler {
  private final MindRepository mindRepository;

  public MindPutHandler(
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
            ProtocolBufferUtil.<MindPutRequestProto>parse(
                body,
                MindPutRequestProto.newBuilder()
            )
        )
        .mapNotNull(mindPutRequestProto ->
            MindProtoUtil.toMind(
                mindPutRequestProto,
                jwtPayloadProto.getSequence(),
                YnType.Y
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
        );
  }
}
