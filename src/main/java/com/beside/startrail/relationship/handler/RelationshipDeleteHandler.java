package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.image.repository.ImageRepository;
import com.beside.startrail.image.service.ImageService;
import com.beside.startrail.mind.command.MindSaveOneCommand;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.service.MindService;
import com.beside.startrail.relationship.command.RelationshipSaveOneCommand;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationshipDeleteHandler extends AbstractSignedTransactionalHandler {
  private final String bucketName;
  private final RelationshipRepository relationshipRepository;
  private final MindRepository mindRepository;
  private final ImageRepository imageRepository;

  public RelationshipDeleteHandler(
      @Value("${sign.attributeName}") String attributeName,
      @Value("${objectStorage.bucketName}") String bucketName,
      RelationshipRepository relationshipRepository,
      MindRepository mindRepository,
      ImageRepository imageRepository
  ) {
    super(attributeName);
    this.bucketName = bucketName;
    this.relationshipRepository = relationshipRepository;
    this.mindRepository = mindRepository;
    this.imageRepository = imageRepository;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return Mono.when(
            MindService.getByRelationshipSequence(sequence)
                .execute(mindRepository)
                .doOnNext(mind ->
                    Optional.ofNullable(
                            ImageService.delete(
                                bucketName,
                                ImageService.getKey(mind.getItem().getImageLink())
                            )
                        )
                        .map(imageDeleteCommand -> imageDeleteCommand.execute(imageRepository))
                )
                .map(mind -> Mind.from(mind, YnType.N))
                .map(MindSaveOneCommand::new)
                .flatMap(mindSaveOneCommand ->
                    mindSaveOneCommand.execute(mindRepository)
                ),
            RelationshipService.getByUserSequenceAndSequenceAndUseYn(
                    super.jwtPayloadProto.getSequence(),
                    sequence,
                    YnType.Y
                )
                .execute(relationshipRepository)
                .map(relationship ->
                    Relationship.from(relationship, YnType.N)
                )
                .map(RelationshipSaveOneCommand::new)
                .flatMap(relationshipSaveOneCommand ->
                    relationshipSaveOneCommand.execute(relationshipRepository)
                )
        )
        .then(
            ServerResponse
                .ok()
                .build()
        );
  }
}
