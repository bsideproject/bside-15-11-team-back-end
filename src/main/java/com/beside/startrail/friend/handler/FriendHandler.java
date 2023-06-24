package com.beside.startrail.friend.handler;

import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.friend.service.FriendService;
import com.mongodb.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.friend.FriendCreateDto;
import protobuf.friend.FriendDto;
import protobuf.friend.FriendSearchCriteria;
import protobuf.friend.FriendUpdateDto;
import protobuf.sign.JwtPayloadProto;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class FriendHandler {

    private final FriendService friendService;
    private final FriendRequestValidator friendValidator;
    private final String attributeName;

    public FriendHandler(FriendService friendService,
                         FriendRequestValidator friendValidator,
                         @Value("${sign.attributeName}")String attributeName) {
        this.friendService = friendService;
        this.friendValidator = friendValidator;
        this.attributeName = attributeName;
    }

    public Mono<ServerResponse> getFriendBySequence(ServerRequest request){
        JwtPayloadProto jwtPayloadProto = (JwtPayloadProto) request.attribute(attributeName).orElseThrow();
        String sequence = request.pathVariable("sequence");

        return friendService.getFriendBySequence(jwtPayloadProto.getSequence(), sequence)
                .log()
                .flatMap(friend ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(friend)
                );
    }

    public Mono<ServerResponse> getFriendsByCriteria(ServerRequest request){
        JwtPayloadProto jwtPayloadProto = (JwtPayloadProto) request.attribute(attributeName).orElseThrow();

        return makeCriteriaParams(request)
                .flatMapMany(friendSearchCriteria -> friendService.getFriendsByCriteria(jwtPayloadProto.getSequence(), friendSearchCriteria))
                .collectList()
                .log()
                .flatMap(friendDtoList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(friendDtoList)
                );
    }


    public Mono<ServerResponse> createFriend(ServerRequest request){
        JwtPayloadProto jwtPayloadProto = (JwtPayloadProto) request.attribute(attributeName).orElseThrow();
        return request.bodyToMono(String.class)
                .flatMap(body -> ProtocolBufferUtil.<FriendCreateDto>parse(body, FriendCreateDto.newBuilder()))
                .log()
                .doOnNext(friendValidator::createValidate)
                .flatMap(friendDto -> friendService.createFriend(jwtPayloadProto.getSequence(), friendDto))
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new IllegalArgumentException("Duplicate key, Friend sequence")
                )
                .flatMap(friendList ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(friendList)
                );
    }

    public Mono<ServerResponse> updateFriend(ServerRequest request){
        JwtPayloadProto jwtPayloadProto = (JwtPayloadProto) request.attribute(attributeName).orElseThrow();
        String sequence = request.pathVariable("sequence");

        return request.bodyToMono(String.class)
                .flatMap(body -> ProtocolBufferUtil.<FriendUpdateDto>parse(body, FriendUpdateDto.newBuilder()))
                .log()
                .doOnNext(friendValidator::updateValidate)
                .flatMap(body -> friendService.updateFriend(jwtPayloadProto.getSequence(), sequence, body))
                .flatMap(friendDto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(friendDto)
                );
    }

    public Mono<ServerResponse> removeFriend(ServerRequest request){
        JwtPayloadProto jwtPayloadProto = (JwtPayloadProto) request.attribute(attributeName).orElseThrow();
        String sequence = request.pathVariable("sequence");

        return friendService.removeFriend(jwtPayloadProto.getSequence(), sequence)
                .log()
                .flatMap(friendDto ->
                        ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(isDeleted(friendDto))
                );
    }

    private Mono<FriendSearchCriteria> makeCriteriaParams(ServerRequest request){
        FriendSearchCriteria.Builder friendSearchCriteriaBuilder = FriendSearchCriteria
                .newBuilder();

        request.queryParams()
            .forEach((key, value) -> {
            if (!ObjectUtils.isEmpty(FriendSearchCriteria.getDescriptor().findFieldByName(key))) {
                friendSearchCriteriaBuilder.setField(
                    FriendSearchCriteria.getDescriptor().findFieldByName(key),
                    value.stream().findFirst().orElse(""));
            }
        });

        return Mono.just(friendSearchCriteriaBuilder.build());
    }

    private Boolean isDeleted(FriendDto friendDto){
        return !ObjectUtils.isEmpty(friendDto.getSequence())
                ? Boolean.TRUE
                : Boolean.FALSE;
    }
}
