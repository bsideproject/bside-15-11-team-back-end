package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.levelinformation.LevelInformationProtoUtil;
import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.repository.CustomMindRepository;
import com.beside.startrail.mind.service.MindService;
import com.beside.startrail.relationship.repository.CustomRelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import com.beside.startrail.relationship.type.SortType;
import com.beside.startrail.relationshiplevel.repository.RelationshipLevelRepository;
import com.beside.startrail.relationshiplevel.service.RelationshipLevelService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipGetRequestProto;
import protobuf.relationship.SortTypeProto;
import reactor.core.publisher.Mono;

@Component
public class RelationshipGetByNicknameKeywordWithSortHandler extends AbstractSignedHandler {
  private final CustomRelationshipRepository customRelationshipRepository;
  private final CustomMindRepository customMindRepository;
  private final RelationshipLevelRepository relationshipLevelRepository;

  public RelationshipGetByNicknameKeywordWithSortHandler(
      @Value("${sign.attributeName}") String attributeName,
      CustomRelationshipRepository customRelationshipRepository,
      CustomMindRepository customMindRepository,
      RelationshipLevelRepository relationshipLevelRepository
  ) {
    super(attributeName);
    this.customRelationshipRepository = customRelationshipRepository;
    this.customMindRepository = customMindRepository;
    this.relationshipLevelRepository = relationshipLevelRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return ProtocolBufferUtil
        .<RelationshipGetRequestProto>from(serverRequest.queryParams(),
            RelationshipGetRequestProto.newBuilder())
        .flatMapMany(relationshipGetRequestProto ->
            RelationshipService
                .sort(
                    RelationshipService.getByNicknameKeyword(
                            super.jwtPayloadProto.getSequence(),
                            relationshipGetRequestProto.getKeyword(),
                            YnType.Y
                        )
                        .execute(customRelationshipRepository)
                        .map(RelationshipProtoUtil::toRelationshipResponseProto)
                        .flatMap(
                            relationshipResponseProto ->
                                MindService
                                    .countByRelationshipSequence(
                                        super.jwtPayloadProto.getSequence(),
                                        relationshipResponseProto.getSequence(),
                                        YnType.Y
                                    )
                                    .execute(customMindRepository)
                                    .switchIfEmpty(Mono.just(MindCountResult.makeDefault()))
                                    .flatMap(mindCountResult ->
                                        RelationshipLevelService
                                            .getBetweenCount(mindCountResult.getTotal())
                                            .execute(relationshipLevelRepository)
                                            .map(relationshipLevel ->
                                                LevelInformationProtoUtil.toLevelInformationProto(
                                                    relationshipLevel,
                                                    mindCountResult.getTotal(),
                                                    mindCountResult.getGiven(),
                                                    mindCountResult.getTaken()
                                                )
                                            )
                                    )
                                    .map(levelInformationProto ->
                                        RelationshipProtoUtil.from(
                                            relationshipResponseProto,
                                            levelInformationProto
                                        )
                                    )
                        ),
                    getSortType(relationshipGetRequestProto.getSort())
                )
        )
        .collectList()
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }

  private SortType getSortType(SortTypeProto sortTypeProto) {
    if (Objects.isNull(sortTypeProto)) {
      return SortType.NICKNAME;
    }

    return SortType.valueOf(sortTypeProto.name());
  }
}
