package com.beside.startrail.mind.service;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.command.MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindCountByUserSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindAllByRelationshipSequenceWithOrderCommand;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceAndRelationshipSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindOneByUserSequenceAndSequenceCommand;
import com.beside.startrail.mind.command.MindSaveAllCommand;
import com.beside.startrail.mind.command.MindSaveOneCommand;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.type.SortOrderType;
import java.util.List;

public class MindService {
  public static MindCountByUserSequenceAndUseYnCommand countByUserSequenceAndUseYn(
      String userSequence,
      YnType useYn
  ) {
    return new MindCountByUserSequenceAndUseYnCommand(userSequence, useYn);
  }

  public static MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand countByRelationshipSequenceAndUseYn(
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

  public static MindFindAllByRelationshipSequenceWithOrderCommand getByRelationshipSequenceWithOrder(
      String userSequence,
      String relationshipSequence,
      SortOrderType sortOrderType
  ) {
    return new MindFindAllByRelationshipSequenceWithOrderCommand(
        userSequence,
        relationshipSequence,
        sortOrderType.getSort("date")
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

  public static MindFindOneByUserSequenceAndSequenceCommand getBySequence(
      String userSequence,
      String sequence
  ) {
    return new MindFindOneByUserSequenceAndSequenceCommand(userSequence, sequence);
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

  public static MindFindAllByUserSequenceAndUseYnCommand getByUserSequenceAndUseYn(
      String userSequence,
      YnType useYn
  ) {
    return new MindFindAllByUserSequenceAndUseYnCommand(userSequence, useYn);
  }
}
