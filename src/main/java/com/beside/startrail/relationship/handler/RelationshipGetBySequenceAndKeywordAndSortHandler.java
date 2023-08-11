package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.common.protocolbuffer.levelinformation.LevelInformationProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import com.beside.startrail.relationship.repository.CustomRelationshipRepository;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import com.beside.startrail.relationshiplevel.repository.RelationshipLevelRepository;
import com.beside.startrail.relationshiplevel.service.RelationshipLevelService;
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
  private final MindRepository mindRepository;
  private final RelationshipLevelRepository relationshipLevelRepository;

  public RelationshipGetBySequenceAndKeywordAndSortHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipService relationshipService,
      RelationshipRepository relationshipRepository,
      CustomRelationshipRepository customRelationshipRepository,
      MindRepository mindRepository,
      RelationshipLevelRepository relationshipLevelRepository
  ) {
    super(attributeName);
    this.relationshipService = relationshipService;
    this.relationshipRepository = relationshipRepository;
    this.customRelationshipRepository = customRelationshipRepository;
    this.mindRepository = mindRepository;
    this.relationshipLevelRepository = relationshipLevelRepository;
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
//        .flatMap(relationship ->
//          MindService
//              .countByRelationshipSequenceAndUseYn(relationship.getSequence(), YnType.Y)
//              .execute(mindRepository)
//              .map(count -> )
//        )
        // todo
        .map(RelationshipProtoUtil::toRelationshipResponseProto)
        .flatMap(
            relationshipResponseProto ->
                MindService
              .countByRelationshipSequenceAndUseYn(relationshipResponseProto.getSequence(), YnType.Y)
              .execute(mindRepository)
              .map(RelationshipLevelService::getBetweenCount)
                    .flatMap(relationshipLevelFindBetweenCountCommand ->
                        relationshipLevelFindBetweenCountCommand.execute(relationshipLevelRepository)
                    )
                    .map(LevelInformationProtoUtil::toLevelInformationProto)
                    .map(relationshipLevelProto ->
                        RelationshipProtoUtil.from(relationshipResponseProto, relationshipLevelProto)
                        )
        )
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
