package com.beside.startrail.mind.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.model.MindCountResult;
import reactor.core.publisher.Mono;

public interface CustomMindRepository {
  Mono<MindCountResult> countByUserSequenceAndUseYn(String sequence, YnType useYn);
}
