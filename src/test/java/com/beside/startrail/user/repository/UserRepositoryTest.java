package com.beside.startrail.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.type.OauthServiceType;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {
  @Autowired
  UserRepository userRepository;

  @Test
  void test() {
    // given
    User user = User.builder()
        .userId(
            UserId.builder()
                .oauthServiceType(OauthServiceType.KAKAO)
                .serviceUserId("serviceUserId")
                .build()
        )
        .sequence(UUID.randomUUID().toString())
        .useYn(YnType.N)
        .build();

    // when
    // save
    User savedUser = userRepository.save(user).block();
    // find
    User foundUser =
        userRepository.findUserByUserId_OauthServiceType_AndUserId_ServiceUserIdAndUseYn(
            user.getUserId().getOauthServiceType(),
            user.getUserId().getServiceUserId(),
            user.getUseYn()
        ).block();
    // exists
    Boolean isExists = userRepository.existsBySequenceAndUseYn(
        user.getSequence(),
        user.getUseYn()
    ).block();

    // then
    assertThat(user)
        .isEqualTo(savedUser)
        .isEqualTo(foundUser);
    assertTrue(isExists);
  }
}