package com.backend.blooming.user.application.fixture;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    protected User 사용자;
    protected Long 사용자_아이디;
    protected Long 삭제한_사용자_아아디;
    protected Long 존재하지_않는_사용자_아아디 = 9999L;

    @BeforeEach
    void setUpFixture() {
        사용자 = User.builder()
                  .oAuthId("12345")
                  .oAuthType(OAuthType.KAKAO)
                  .name("사용자")
                  .email("test@email.com")
                  .build();
        final User 삭제한_사용자 = User.builder()
                                     .oAuthId("12346")
                                     .oAuthType(OAuthType.KAKAO)
                                     .name("삭제한 사용자")
                                     .email("test2@email.com")
                                     .build();
        삭제한_사용자.delete();

        userRepository.saveAll(List.of(사용자, 삭제한_사용자));

        사용자_아이디 = 사용자.getId();
        삭제한_사용자_아아디 = 삭제한_사용자.getId();
    }
}
