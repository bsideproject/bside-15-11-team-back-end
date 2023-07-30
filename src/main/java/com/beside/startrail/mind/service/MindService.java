package com.beside.startrail.mind.service;

import com.beside.startrail.mind.command.MindCountCommand;
import com.beside.startrail.mind.command.MindDeleteOneCommand;
import com.beside.startrail.mind.command.MindFindAllByRelationshipSequenceCommand;
import com.beside.startrail.mind.command.MindFindOneBySequenceCommand;
import com.beside.startrail.mind.command.MindSaveAllCommand;
import com.beside.startrail.mind.command.MindSaveOneCommand;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.type.SortOrderType;
import java.util.List;

public class MindService {
  public static MindCountCommand countByUserSequenceAndRelationshipType(
      String userSequence) {
    return new MindCountCommand(userSequence);
  }

  public static MindFindAllByRelationshipSequenceCommand getByRelationshipSequence(
      String relationshipSequence,
      SortOrderType sortOrderType
  ) {
    return new MindFindAllByRelationshipSequenceCommand(
        relationshipSequence,
        sortOrderType.getSort("date")
    );
  }

  public static MindFindOneBySequenceCommand getByUserSequenceAndSequence(String sequence) {
    return new MindFindOneBySequenceCommand(sequence);
  }

  public static MindSaveAllCommand save(List<Mind> minds) {
    return new MindSaveAllCommand(minds);
  }

  public static MindSaveOneCommand update(Mind mind) {
    return new MindSaveOneCommand(mind);
  }

  public static MindDeleteOneCommand removeByUserSequenceAndSequence(
      String userSequence,
      String sequence
  ) {
    return new MindDeleteOneCommand(userSequence, sequence);
  }
}
