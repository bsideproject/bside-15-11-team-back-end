package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.levelinformation.LevelInformationProtoUtil;
import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.repository.CustomMindRepository;
import com.beside.startrail.mind.service.MindService;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import com.beside.startrail.relationshiplevel.repository.RelationshipLevelRepository;
import com.beside.startrail.relationshiplevel.service.RelationshipLevelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationshipGetBySequenceHandler extends AbstractSignedHandler {
  private final RelationshipRepository relationshipRepository;
  private final CustomMindRepository customMindRepository;
  private final RelationshipLevelRepository relationshipLevelRepository;

  public RelationshipGetBySequenceHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipRepository relationshipRepository,
      CustomMindRepository customMindRepository,
      RelationshipLevelRepository relationshipLevelRepository
  ) {
    super(attributeName);
    this.relationshipRepository = relationshipRepository;
    this.customMindRepository = customMindRepository;
    this.relationshipLevelRepository = relationshipLevelRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return RelationshipService
        .getByUserSequenceAndSequenceAndUseYn(
            super.jwtPayloadProto.getSequence(),
            sequence,
            YnType.Y
        )
        .execute(relationshipRepository)
        .map(RelationshipProtoUtil::toRelationshipResponseProto)
        .flatMap(
            relationshipResponseProto ->
                MindService
                    .countByRelationshipSequenceAndUseYn(
                        super.jwtPayloadProto.getSequence(),
                        relationshipResponseProto.getSequence(),
                        YnType.Y
                    )
                    .execute(customMindRepository)
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
        )
        .map(ProtocolBufferUtil::print)
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }
}
