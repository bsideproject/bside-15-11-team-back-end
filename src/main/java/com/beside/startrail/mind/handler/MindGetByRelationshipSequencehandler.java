package com.beside.startrail.mind.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.mind.MindProtoUtil;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import com.beside.startrail.mind.type.SortOrderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.mind.MindGetRequestProto;
import protobuf.mind.MindGetResponseProto;
import reactor.core.publisher.Mono;

@Component
public class MindGetByRelationshipSequencehandler extends AbstractSignedHandler {
  private final MindRepository mindRepository;

  public MindGetByRelationshipSequencehandler(
      @Value("${sign.attributeName}") String attributeName,
      MindRepository mindRepository
  ) {
    super(attributeName);
    this.mindRepository = mindRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return ProtocolBufferUtil
        .<MindGetRequestProto>from(serverRequest.queryParams(),
            MindGetRequestProto.newBuilder())
        .map(mindGetRequestProto ->
            MindService.getByRelationshipSequenceWithOrder(
                mindGetRequestProto.getRelationshipSequence(),
                SortOrderType.valueOf(mindGetRequestProto.getSort().name())
            )
        )
        .flatMap(mindFindAllByRelationshipSequenceCommand ->
            mindFindAllByRelationshipSequenceCommand.execute(mindRepository)
                .map(MindProtoUtil::toMindResponseProto)
                .collectList()
        )
        .filter(mindResponseProtos ->
            !CollectionUtils.isEmpty(mindResponseProtos)
        )
        .map(mindResponseProtos ->
            MindGetResponseProto.newBuilder()
                .addAllMinds(mindResponseProtos)
                .build()
        )
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
