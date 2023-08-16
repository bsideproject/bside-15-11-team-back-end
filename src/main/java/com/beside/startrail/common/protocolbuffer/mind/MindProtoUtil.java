package com.beside.startrail.common.protocolbuffer.mind;

import com.beside.startrail.common.protocolbuffer.common.DateProtoUtil;
import com.beside.startrail.common.protocolbuffer.common.ItemProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.type.MindType;
import java.util.Objects;
import protobuf.common.type.MindTypeProto;
import protobuf.mind.MindCountResponseProto;
import protobuf.mind.MindPutRequestProto;
import protobuf.mind.MindPutResponseProto;
import protobuf.mind.MindRequestProto;
import protobuf.mind.MindResponseProto;

public class MindProtoUtil {
  private MindProtoUtil() {
  }

  public static MindResponseProto toMindResponseProto(Mind mind) {
    if (Objects.isNull(mind)) {
      return MindResponseProto
          .newBuilder()
          .clear()
          .build();
    }

    MindResponseProto.Builder builder = MindResponseProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(mind.getSequence())) {
      builder.setSequence(mind.getSequence());
    }
    if (Objects.nonNull(mind.getType())) {
      builder.setType(
          MindTypeProto.valueOf(mind.getType().name())
      );
    }
    if (Objects.nonNull(mind.getEvent())) {
      builder.setEvent(mind.getEvent());
    }
    if (Objects.nonNull(mind.getDate())) {
      builder.setDate(DateProtoUtil.toDate(mind.getDate()));
    }
    if (Objects.nonNull(mind.getItem())) {
      builder.setItem(ItemProtoUtil.toItemProto(mind.getItem()));
    }
    if (Objects.nonNull(mind.getMemo())) {
      builder.setMemo(mind.getMemo());
    }

    return builder.build();
  }

  public static Mind toMindWithImageLink(
      MindRequestProto mindRequestProto,
      String imageLink,
      String userSequence,
      YnType useYn
  ) {
    if (Objects.isNull(mindRequestProto)) {
      return null;
    }

    return Mind.builder()
        .userSequence(userSequence)
        .relationshipSequence(mindRequestProto.getRelationshipSequence())
        .type(
            MindType.valueOf(mindRequestProto.getType().name())
        )
        .event(mindRequestProto.getEvent())
        .date(DateProtoUtil.toLocalDateTime(mindRequestProto.getDate()))
        .item(ItemProtoUtil.toItemWithImageLink(mindRequestProto.getItem(), imageLink))
        .memo(mindRequestProto.getMemo())
        .useYn(useYn)
        .build();
  }

  public static MindCountResponseProto toMindCountResponseProto(
      MindCountResult mindCountResult
  ) {
    if (Objects.isNull(mindCountResult)) {
      return MindCountResponseProto.newBuilder()
          .clear()
          .build();
    }

    return MindCountResponseProto.newBuilder()
        .setTotal(mindCountResult.getTotal())
        .setGiven(mindCountResult.getGiven())
        .setTaken(mindCountResult.getTaken())
        .build();
  }

  public static Mind toMindWithImageLink(
      MindPutRequestProto mindPutRequestProto,
      String imageLink,
      String sequence,
      YnType ynType
  ) {
    if (Objects.isNull(mindPutRequestProto) ||
        !mindPutRequestProto.isInitialized()) {
      return null;
    }

    return Mind.builder()
        .sequence(mindPutRequestProto.getSequence())
        .userSequence(sequence)
        .relationshipSequence(mindPutRequestProto.getRelationshipSequence())
        .type(
            MindType.valueOf(mindPutRequestProto.getType().name())
        )
        .event(mindPutRequestProto.getEvent())
        .date(DateProtoUtil.toLocalDateTime(mindPutRequestProto.getDate()))
        .item(ItemProtoUtil.toItemWithImageLink(mindPutRequestProto.getItem(), imageLink))
        .memo(mindPutRequestProto.getMemo())
        .useYn(ynType)
        .build();
  }

  public static MindPutResponseProto toMindPutResponseProto(
      Mind relationship) {
    if (Objects.isNull(relationship)) {
      return MindPutResponseProto
          .newBuilder()
          .clear()
          .build();
    }

    MindPutResponseProto.Builder builder = MindPutResponseProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(relationship.getSequence())) {
      builder.setSequence(relationship.getSequence());
    }
    if (Objects.nonNull(relationship.getRelationshipSequence())) {
      builder.setRelationshipSequence(relationship.getRelationshipSequence());
    }
    if (Objects.nonNull(relationship.getType())) {
      builder.setType(
          MindTypeProto.valueOf(relationship.getType().name())
      );
    }
    if (Objects.nonNull(relationship.getEvent())) {
      builder.setEvent(relationship.getEvent());
    }
    if (Objects.nonNull(relationship.getDate())) {
      builder.setDate(DateProtoUtil.toDate(relationship.getDate()));
    }
    if (Objects.nonNull(relationship.getItem())) {
      builder.setItem(ItemProtoUtil.toItemProto(relationship.getItem()));
    }
    if (Objects.nonNull(relationship.getMemo())) {
      builder.setMemo(relationship.getMemo());
    }

    return builder.build();
  }
}
