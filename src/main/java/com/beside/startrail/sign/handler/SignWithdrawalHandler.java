package com.beside.startrail.sign.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.image.repository.ImageRepository;
import com.beside.startrail.image.service.ImageService;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceCommand;
import com.beside.startrail.mind.command.MindSaveOneCommand;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.relationship.command.RelationshipFindAllByUserSequenceCommand;
import com.beside.startrail.relationship.command.RelationshipSaveOneCommand;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.user.command.UserFindOneBySequenceCommand;
import com.beside.startrail.user.command.UserSaveOneCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.SignWithdrawalRequestProto;
import reactor.core.publisher.Mono;

@Component
public class SignWithdrawalHandler extends AbstractSignedTransactionalHandler {
  private final String bucketName;
  private final UserRepository userRepository;
  private final RelationshipRepository friendRepository;
  private final MindRepository relationshipRepository;
  private final ImageRepository imageRepository;

  public SignWithdrawalHandler(
      @Value("${sign.attributeName}") String attributeName,
      @Value("${objectStorage.bucketName}") String bucketName,
      UserRepository userRepository,
      RelationshipRepository friendRepository,
      MindRepository relationshipRepository,
      ImageRepository imageRepository
  ) {
    super(attributeName);
    this.bucketName = bucketName;
    this.userRepository = userRepository;
    this.friendRepository = friendRepository;
    this.relationshipRepository = relationshipRepository;
    this.imageRepository = imageRepository;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = super.jwtPayloadProto.getSequence();

    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<SignWithdrawalRequestProto>parse(body,
                SignWithdrawalRequestProto.newBuilder())
        )
        .flatMap(signWithdrawlRequestProto ->
            Mono.when(
                    new UserFindOneBySequenceCommand(sequence)
                        .execute(userRepository)
                        .map(user ->
                            User.fromReason(user, signWithdrawlRequestProto.getWithdrawalReason())
                        )
                        .map(user ->
                            User.fromUseYn(user, YnType.N)
                        )
                        .map(UserSaveOneCommand::new)
                        .flatMap(userSaveCommand ->
                            userSaveCommand.execute(userRepository)
                        ),
                    new RelationshipFindAllByUserSequenceCommand(sequence)
                        .execute(friendRepository)
                        .map(friend ->
                            new RelationshipSaveOneCommand(Relationship.from(friend, YnType.N))
                        )
                        .flatMap(friendSaveCommand ->
                            friendSaveCommand.execute(friendRepository)),
                    new MindFindAllByUserSequenceCommand(sequence)
                        .execute(relationshipRepository)
                        .flatMap(relationship ->
                            ImageService.delete(
                                    bucketName,
                                    ImageService.getKey(relationship.getItem().getImageLink())
                                )
                                .execute(imageRepository)
                                .thenReturn(
                                    new MindSaveOneCommand(
                                        Mind.from(relationship, YnType.N)
                                    )
                                )
                        )
                        .flatMap(relationshipSaveCommand ->
                            relationshipSaveCommand.execute(relationshipRepository)
                        )
                )
                .then(
                    ServerResponse
                        .ok()
                        .build()
                )
        );
  }
}
