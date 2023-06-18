package com.beside.mamgwanboo.relationship.repository;

import com.beside.mamgwanboo.relationship.document.Relationship;
import java.util.UUID;
import protobuf.common.type.SortOrderType;
import reactor.core.publisher.Flux;

public interface CustomRelationshipRepository {
  Flux<Relationship> findAllByFriendSequence(UUID friendSequence, SortOrderType sortOrderType);
}
