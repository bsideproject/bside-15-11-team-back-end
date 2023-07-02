package com.beside.startrail.relationLevel.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.relationLevel.service.RelationLevelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.common.RelationLevelGetCriteriaProto;
import reactor.core.publisher.Mono;

@Component
public class RelationLevelGetAllByCriteriaHandler extends AbstractSignedHandler {

    private final RelationLevelService relationLevelService;

    public RelationLevelGetAllByCriteriaHandler(@Value("${sign.attributeName}") String attributeName,
                                                RelationLevelService relationLevelService) {
        super(attributeName);
        this.relationLevelService = relationLevelService;
    }


    @Override
    protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
        return makeCriteriaParams(serverRequest)
                .flatMapMany(relationLevelService::getRelationLevelsByCriteria)
                .collectList()
                .flatMap(relationLevelProtos ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(relationLevelProtos)
                )
                .onErrorResume(throwable ->
                        ServerResponse.badRequest().bodyValue(throwable.getMessage())
                );
    }

    private Mono<RelationLevelGetCriteriaProto> makeCriteriaParams(ServerRequest request){
        RelationLevelGetCriteriaProto.Builder relationLevelGetCriteriaProtoBuilder = RelationLevelGetCriteriaProto.newBuilder();

        request.queryParams()
                .forEach((key, value) -> {
                    if(!ObjectUtils.isEmpty(RelationLevelGetCriteriaProto.getDescriptor().findFieldByName(key))){
                        relationLevelGetCriteriaProtoBuilder.setField(
                                RelationLevelGetCriteriaProto.getDescriptor().findFieldByName(key),
                                value.stream().findFirst().orElse("")
                        );
                    }
                });

        return Mono.just(relationLevelGetCriteriaProtoBuilder.build());
    }
}
