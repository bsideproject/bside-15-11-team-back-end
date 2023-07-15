package com.beside.startrail.sign.common.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.friend.command.FriendFindByUserSequenceCommand;
import com.beside.startrail.friend.command.FriendSaveCommand;
import com.beside.startrail.friend.document.Friend;
import com.beside.startrail.friend.repository.FriendRepository;
import com.beside.startrail.relationship.command.RelationshipFindByUserSequenceCommand;
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

    return Mono.when(
            new UserFindBySequenceCommand(sequence)
                .execute(userRepository)
                .map(user -> new UserSaveCommand(User.fromUseYn(user, YnType.N)))
                .flatMap(userSaveCommand -> userSaveCommand.execute(userRepository)),
            new FriendFindByUserSequenceCommand(sequence)
                .execute(friendRepository)
                .map(friend -> new FriendSaveCommand(Friend.from(friend, YnType.N)))
                .flatMap(friendSaveCommand -> friendSaveCommand.execute(friendRepository)),
            new RelationshipFindByUserSequenceCommand(sequence)
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
        );
  }
}
