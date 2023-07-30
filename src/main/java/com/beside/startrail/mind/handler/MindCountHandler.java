package com.beside.startrail.mind.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.mind.MindProtoUtil;
import com.beside.startrail.mind.repository.CustomMindRepository;
import com.beside.startrail.mind.service.MindService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.mind.MindCountResponseProto;
import reactor.core.publisher.Mono;

@Component
public class MindCountHandler extends AbstractSignedHandler {
  private final CustomMindRepository customMindRepository;

  public MindCountHandler(
      @Value("${sign.attributeName}") String attributeName,
      CustomMindRepository customMindRepository
  ) {
    super(attributeName);
    this.customMindRepository = customMindRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return MindService
        .countByUserSequenceAndRelationshipType(super.jwtPayloadProto.getSequence())
        .execute(customMindRepository)
        .map(MindProtoUtil::toMindCountResponseProto)
        .flatMap(mindCountResponseProto ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ProtocolBufferUtil.print(mindCountResponseProto))
        )
        .switchIfEmpty(
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ProtocolBufferUtil.print(
                    MindCountResponseProto.newBuilder()
                        .setTaken(0)
                        .setGiven(0)
                        .setTaken(0)
                        .build()
                ))
        );
  }
}
