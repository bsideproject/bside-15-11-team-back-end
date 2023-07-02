package com.beside.startrail.relationLevel.service;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationLevel.document.RelationLevel;
import com.beside.startrail.relationLevel.repository.RelationLevelRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import protobuf.common.RelationLevelGetCriteriaProto;
import protobuf.common.RelationLevelProto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RelationLevelService {

    private final String LEVEL_FILED = "level";
    private final RelationLevelRepository relationLevelRepository;

    public RelationLevelService(RelationLevelRepository relationLevelRepository) {
        this.relationLevelRepository = relationLevelRepository;
    }

    public Mono<RelationLevelProto> getRelationLevelBySequence(String sequence) {
        return getVerifiedRelationLevel(sequence)
                .flatMap(relationLevel -> Mono.just(this.toRelationLevelProto(relationLevel)));
    }

    public Flux<RelationLevelProto> getRelationLevelsByCriteria(RelationLevelGetCriteriaProto criteriaProto){
        return relationLevelRepository.findRelationLevelByCriteria(criteriaProto)
                .onErrorMap(NumberFormatException.class,
                        ex -> new IllegalArgumentException("요청값이 잘못 되었습니다.")
                )
                .flatMap(relationLevel -> Mono.just(this.toRelationLevelProto(relationLevel)));
    }

    public Flux<RelationLevelProto> getRelationLevels(){
        return relationLevelRepository.findAllByUseYn(YnType.Y, Sort.by(Sort.Order.asc(LEVEL_FILED)))
                .flatMap(relationLevel -> Mono.just(this.toRelationLevelProto(relationLevel)));
    }

    public Mono<RelationLevelProto> createRelationLevel(RelationLevelProto relationLevelProto){
        return relationLevelRepository.save(this.toRelationLevel(null, relationLevelProto))
                .flatMap(relationLevel -> Mono.just(this.toRelationLevelProto(relationLevel)));
    }

    public Mono<RelationLevelProto> updateRelationLevel(String sequence, RelationLevelProto relationLevelProto){
        return getVerifiedRelationLevel(sequence)
                .flatMap(prevRelationLevel -> Mono.just(this.toRelationLevel(prevRelationLevel, relationLevelProto)))
                .flatMap(toRelationLevel -> relationLevelRepository.save(toRelationLevel))
                .flatMap(relationLevel -> Mono.just(this.toRelationLevelProto(relationLevel)));
    }

    public Mono<RelationLevelProto> removeRelationLevel(String sequence){
        return getVerifiedRelationLevel(sequence)
                .doOnNext(RelationLevel::notUse)
                .flatMap(relationLevelRepository::save)
                .flatMap(relationLevel -> Mono.just(this.toRelationLevelProto(relationLevel)));
    }


    private Mono<RelationLevel> getVerifiedRelationLevel(String sequence){
        return relationLevelRepository.findBySequenceAndUseYn(sequence, YnType.Y)
                .switchIfEmpty(
                        Mono.error(
                                new IllegalArgumentException("Not Found Relation Level")
                        )
                );
    }

    private RelationLevelProto toRelationLevelProto(RelationLevel relationLevel){
        return RelationLevelProto.newBuilder()
                .setSequence(relationLevel.getSequence())
                .setTitle(relationLevel.getTitle())
                .setDescription(relationLevel.getDescription())
                .setLevel(relationLevel.getLevel())
                .setCountFrom(relationLevel.getCountFrom())
                .setCountTo(relationLevel.getCountTo())
                .build();
    }

    private RelationLevel toRelationLevel(RelationLevel relationLevel,
                                          RelationLevelProto relationLevelProto){

        if(ObjectUtils.isEmpty(relationLevel)){
            relationLevel = RelationLevel.builder().build();
        }

        return RelationLevel.builder()
                .sequence(relationLevel.getSequence())
                .title(relationLevelProto.getTitle())
                .description(relationLevelProto.getDescription())
                .level(relationLevelProto.getLevel())
                .countFrom(relationLevelProto.getCountFrom())
                .countTo(relationLevelProto.getCountTo())
                .build();
    }
}
