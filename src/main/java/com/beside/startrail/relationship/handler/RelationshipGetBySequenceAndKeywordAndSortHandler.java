package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.repository.CustomRelationshipRepository;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipGetRequestProto;
import reactor.core.publisher.Mono;

@Component
public class RelationshipGetBySequenceAndKeywordAndSortHandler extends AbstractSignedHandler {
  private final RelationshipService relationshipService;
  private final RelationshipRepository relationshipRepository;
  private final CustomRelationshipRepository customRelationshipRepository;

  public RelationshipGetBySequenceAndKeywordAndSortHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipService relationshipService,
      RelationshipRepository relationshipRepository,
      CustomRelationshipRepository customRelationshipRepository
  ) {
    super(attributeName);
    this.relationshipService = relationshipService;
    this.relationshipRepository = relationshipRepository;
    this.customRelationshipRepository = customRelationshipRepository;
  }


  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {


    return makeCriteriaParams(serverRequest)
        .map(relationshipGetRequestProto ->
            RelationshipService.getByUserSequenceAndNicknameKeywordAndUseYn(
                super.jwtPayloadProto.getSequence(),
                relationshipGetRequestProto.getKeyword(),
                YnType.Y
            )
        )
        .flatMapMany(relationshipFindAllByUserSequenceAndNicknameKeywordAndUseYnCommand ->
            relationshipFindAllByUserSequenceAndNicknameKeywordAndUseYnCommand.execute(
                customRelationshipRepository
            )
        )
        // todo
        .map(RelationshipProtoUtil::toRelationshipResponseProto)
        .map(ProtocolBufferUtil::print)
        .collectList()
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }

  private Mono<RelationshipGetRequestProto> makeCriteriaParams(ServerRequest serverRequest) {
    RelationshipGetRequestProto.Builder builder = RelationshipGetRequestProto
        .newBuilder()
        .clear();

    serverRequest.queryParams()
        .forEach((key, value) -> {
          if (Objects.nonNull(RelationshipGetRequestProto.getDescriptor().findFieldByName(key))) {
            builder.setField(
                RelationshipGetRequestProto.getDescriptor().findFieldByName(key),
                value.stream().findFirst().orElse("")
            );
          }
        });

    return Mono.just(builder.build());
  }
}
