package com.beside.startrail.friend;

import com.beside.startrail.common.util.ProtocolBufferUtil;
import com.mongodb.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.friend.FriendCreateDto;
import protobuf.friend.FriendDto;
import protobuf.friend.FriendSearchCriteria;
import protobuf.friend.FriendUpdateDto;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class FriendHandler {

    private final FriendService friendService;
    private final FriendRequestValidator friendValidator;

    public FriendHandler(FriendService friendService, FriendRequestValidator friendValidator) {
        this.friendService = friendService;
        this.friendValidator = friendValidator;
    }

    public Mono<ServerResponse> getFriend(ServerRequest request){
        String sequence = request.pathVariable("sequence");

        return friendService.getFriend(sequence)
                .log()
                .flatMap(friend ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(friend)
                );
    }

    public Mono<ServerResponse> getFriendsByCriteria(ServerRequest request){
        return makeCriteriaParams(request)
                .flatMapMany(friendService::getFriendsByCriteria)
                .collectList()
                .log()
                .flatMap(friendDtoList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(friendDtoList)
                );
    }

    private Mono<FriendSearchCriteria> makeCriteriaParams(ServerRequest request){
        FriendSearchCriteria.Builder friendSearchCriteriaBuilder = FriendSearchCriteria
                .newBuilder();

        request.queryParams().entrySet()
                .forEach(entry -> {
                    if (!ObjectUtils.isEmpty(FriendSearchCriteria.getDescriptor().findFieldByName(entry.getKey()))){
                        friendSearchCriteriaBuilder.setField(
                                FriendSearchCriteria.getDescriptor().findFieldByName(entry.getKey()),
                                entry.getValue().stream().findFirst().orElse(""));
                    }
                });

        return Mono.just(friendSearchCriteriaBuilder.build());
    }

    public Mono<ServerResponse> createFriend(ServerRequest request){
        return request.bodyToMono(String.class)
                .flatMap(body -> ProtocolBufferUtil.<FriendCreateDto>parse(body, FriendCreateDto.newBuilder()))
                .log()
                .doOnNext(friendValidator::createValidate)
                .flatMap(friendService::createFriend)
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
        String sequence = request.pathVariable("sequence");
        return request.bodyToMono(String.class)
                .flatMap(body -> ProtocolBufferUtil.<FriendUpdateDto>parse(body, FriendUpdateDto.newBuilder()))
                .log()
                .doOnNext(friendValidator::updateValidate)
                .flatMap(body -> friendService.updateFriend(sequence, body))
                .flatMap(friendDto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(friendDto)
                );
    }

    public Mono<ServerResponse> removeFriend(ServerRequest request){
        String sequence = request.pathVariable("sequence");

        return friendService.removeFriend(sequence)
                .log()
                .flatMap(friendDto ->
                        ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(isDeleted(friendDto))
                );
    }

    private Boolean isDeleted(FriendDto friendDto){
        return !ObjectUtils.isEmpty(friendDto.getSequence())
                ? Boolean.TRUE
                : Boolean.FALSE;
    }
}
