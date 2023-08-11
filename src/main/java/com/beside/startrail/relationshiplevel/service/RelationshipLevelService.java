package com.beside.startrail.relationshiplevel.service;

import com.beside.startrail.relationshiplevel.command.RelationshipLevelFindBetweenCountCommand;

public class RelationshipLevelService {
  public static RelationshipLevelFindBetweenCountCommand getBetweenCount(int count) {
    return new RelationshipLevelFindBetweenCountCommand(count);
  }
}
