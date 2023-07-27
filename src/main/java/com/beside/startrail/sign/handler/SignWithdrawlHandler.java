package com.beside.startrail.sign.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.friend.command.FriendFindByUserSequenceCommand;
import com.beside.startrail.friend.command.FriendSaveCommand;
import com.beside.startrail.friend.document.Friend;
import com.beside.startrail.friend.repository.FriendRepository;
import com.beside.startrail.relationship.command.RelationshipFindAllByUserSequenceCommand;
import com.beside.startrail.relationship.command.RelationshipSaveCommand;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.user.command.UserFindBySequenceCommand;
import com.beside.startrail.user.command.UserSaveCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.SignWithdrawlRequestProto;
import reactor.core.publisher.Mono;

@Component
public class SignWithdrawlHandler extends AbstractSignedTransactionalHandler {
  private final UserRepository userRepository;
  private final FriendRepository friendRepository;
  private final RelationshipRepository relationshipRepository;

  public SignWithdrawlHandler(
      @Value("${sign.attributeName}") String attributeName,
      UserRepository userRepository,
      FriendRepository friendRepository,
      RelationshipRepository relationshipRepository
  ) {
    super(attributeName);
    this.userRepository = userRepository;
    this.friendRepository = friendRepository;
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = super.jwtPayloadProto.getSequence();

    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<SignWithdrawlRequestProto>parse(body,
                SignWithdrawlRequestProto.newBuilder())
        )
        .flatMap(signWithdrawlRequestProto ->
            Mono.when(
                    new UserFindBySequenceCommand(sequence)
                        .execute(userRepository)
                        .map(user ->
                            User.fromReason(user, signWithdrawlRequestProto.getWithdrawlReason())
                        )
                        .map(user ->
                            User.fromUseYn(user, YnType.N)
                        )
                        .map(UserSaveCommand::new)
                        .flatMap(userSaveCommand -> userSaveCommand.execute(userRepository)),
                    new FriendFindByUserSequenceCommand(sequence)
                        .execute(friendRepository)
                        .map(friend -> new FriendSaveCommand(Friend.from(friend, YnType.N)))
                        .flatMap(friendSaveCommand -> friendSaveCommand.execute(friendRepository)),
                    new RelationshipFindAllByUserSequenceCommand(sequence)
                        .execute(relationshipRepository)
                        .map(relationship -> new RelationshipSaveCommand(
                            Relationship.from(relationship, YnType.N))
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
