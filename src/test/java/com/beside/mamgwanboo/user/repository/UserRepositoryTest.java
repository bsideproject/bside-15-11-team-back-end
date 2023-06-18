package com.beside.mamgwanboo.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.document.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import protobuf.common.type.OauthServiceType;

@SpringBootTest
class UserRepositoryTest {
  @Autowired
  UserRepository userRepository;

  @Disabled
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
        .sequence(UUID.randomUUID())
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