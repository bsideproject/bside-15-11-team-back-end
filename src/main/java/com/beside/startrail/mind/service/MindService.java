package com.beside.startrail.mind.service;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.command.MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindCountByUserSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceAndRelationshipSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceAndRelationshipSequenceWithOrderAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindOneByUserSequenceAndSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindSaveAllCommand;
import com.beside.startrail.mind.command.MindSaveOneCommand;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.type.SortOrderType;
import java.util.List;

public class MindService {
  public static MindCountByUserSequenceAndUseYnCommand countByUserSequence(
      String userSequence,
      YnType useYn
  ) {
    return new MindCountByUserSequenceAndUseYnCommand(userSequence, useYn);
  }

  public static MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand countByRelationshipSequence(
      String userSequence,
      String relationshipSequence,
      YnType useYn
  ) {
    return new MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand(
        userSequence,
        relationshipSequence,
        useYn
    );
  }

  public static MindFindAllByUserSequenceAndRelationshipSequenceWithOrderAndUseYnCommand getByRelationshipSequenceWithOrder(
      String userSequence,
      String relationshipSequence,
      SortOrderType sortOrderType,
      YnType useYn
  ) {
    return new MindFindAllByUserSequenceAndRelationshipSequenceWithOrderAndUseYnCommand(
        userSequence,
        relationshipSequence,
        sortOrderType.getSort("date"),
        useYn
    );
  }

  public static MindFindAllByUserSequenceAndRelationshipSequenceAndUseYnCommand getByRelationshipSequence(
      String userSequence,
      String relationshipSequence,
      YnType useYn
  ) {
    return new MindFindAllByUserSequenceAndRelationshipSequenceAndUseYnCommand(
        userSequence,
        relationshipSequence,
        useYn
    );
  }

  public static MindFindOneByUserSequenceAndSequenceAndUseYnCommand getBySequence(
      String userSequence,
      String sequence,
      YnType useYn
  ) {
    return new MindFindOneByUserSequenceAndSequenceAndUseYnCommand(
        userSequence,
        sequence,
        useYn
    );
  }

  public static MindSaveOneCommand create(Mind mind) {
    return new MindSaveOneCommand(mind);
  }


  public static MindSaveAllCommand create(List<Mind> minds) {
    return new MindSaveAllCommand(minds);
  }

  public static MindSaveOneCommand update(Mind mind) {
    return new MindSaveOneCommand(mind);
  }

  public static MindFindAllByUserSequenceAndUseYnCommand getByUserSequence(
      String userSequence,
      YnType useYn
  ) {
    return new MindFindAllByUserSequenceAndUseYnCommand(userSequence, useYn);
  }
}
