package com.beside.startrail.mind.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.mind.MindProtoUtil;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MindGetBySequenceHandler extends AbstractSignedHandler {
  private final MindRepository mindRepository;

  public MindGetBySequenceHandler(
      @Value("${sign.attributeName}") String attributeName,
      MindRepository mindRepository
  ) {
    super(attributeName);
    this.mindRepository = mindRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return MindService
        .getBySequence(
            super.jwtPayloadProto.getSequence(),
            sequence
        )
        .execute(mindRepository)
        .map(MindProtoUtil::toMindResponseProto)
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
