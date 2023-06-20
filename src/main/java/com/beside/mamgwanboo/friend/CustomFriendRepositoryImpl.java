package com.beside.mamgwanboo.friend;

import com.beside.mamgwanboo.common.type.YnType;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ObjectUtils;
import protobuf.friend.FriendSearchCriteria;
import reactor.core.publisher.Flux;

public class CustomFriendRepositoryImpl implements CustomFriendRepository{

    private final String NICK_NAME_FILED = "nickname";
    private final String RELATION_FIELD = "relationship";
    private final String RELATION_LEVEL_FILED = "levelInformation.level_";
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CustomFriendRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<Friend> findFriendsByCriteria(FriendSearchCriteria friendSearchCriteria) {
        String keywordReg = ".*" + friendSearchCriteria.getKeyword() + ".*";

        Query query = new Query(
            Criteria.where("useYn").is(YnType.Y)
                    // Todo: usersequence operator
//                    .andOperator(Criteria.where("usersequence").is("TEST"))
                    .orOperator(
                            Criteria.where(NICK_NAME_FILED).regex(keywordReg),
                            Criteria.where(RELATION_FIELD).regex(keywordReg)
                    )
        ).with(Sort.by(getSortByName(friendSearchCriteria.getSort())));

        if(!ObjectUtils.isEmpty(friendSearchCriteria.getRelFilter())){
            query.addCriteria(Criteria.where(RELATION_FIELD).is(friendSearchCriteria.getRelFilter()));
        }

        Flux<Friend> friendFlux = reactiveMongoTemplate.find(query, Friend.class);
        return friendFlux;
    }

    // 이름/호칭 (가나다), 관계별, 관계레벨
    private Sort.Order getSortByName(String sort){
        switch (sort){
            case "level":
                return Sort.Order.desc(RELATION_LEVEL_FILED);
            case "relationship":
                return Sort.Order.asc(RELATION_FIELD);
            default:
                return Sort.Order.asc(NICK_NAME_FILED);
        }
    }

}
