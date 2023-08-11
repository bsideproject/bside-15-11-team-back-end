package com.beside.startrail.relationshiplevel.service;

import com.beside.startrail.relationshiplevel.command.RelationshipLevelFindOneBetweenCountCommand;

public class RelationshipLevelService {
  public static RelationshipLevelFindOneBetweenCountCommand getBetweenCount(int count) {
    return new RelationshipLevelFindOneBetweenCountCommand(count);
  }
}
