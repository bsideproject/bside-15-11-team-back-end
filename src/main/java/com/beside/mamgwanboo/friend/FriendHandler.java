package com.beside.mamgwanboo.friend;

import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.friend.*;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RequiredArgsConstructor
public class FriendHandler {

    private final FriendService friendService;
    private final FriendValidator friendValidator;

    public Mono<ServerResponse> getFriend(ServerRequest request){
        String sequence = request.pathVariable("sequence");

        return friendService.findFriend(sequence)
                .log()
                .flatMap(friend ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(friend)
                );
    }

    public Mono<ServerResponse> getFriends(ServerRequest request){
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

        return friendService.searchFriend(friendSearchCriteriaBuilder.build())
                .collectList()
                .log()
                .flatMap(friendDtoList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(friendDtoList)
                );
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

    public Mono<ServerResponse> deleteFriend(ServerRequest request){
        String sequence = request.pathVariable("sequence");

        return friendService.deleteFriend(sequence)
                .log()
                .flatMap(friendDto ->
                        ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(!ObjectUtils.isEmpty(friendDto.getSequence())? Boolean.TRUE : Boolean.FALSE)
                );
    }

}
