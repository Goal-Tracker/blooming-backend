package com.backend.blooming.user.infrastructure.repository.fixture;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserRepositoryTestFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    protected User 사용자;
    protected String 유효한_oAuth_아이디;
    protected OAuthType 유효한_oAuth_타입;
    protected String 유효하지_않은_oAuth_아이디 = "invalid";
    protected Long 사용자_아이디;
    protected Long 삭제된_사용자_아이디;
    protected Long 존재하지_않는_사용자_아이디 = 9999L;

    @BeforeEach
    void setUpFixture() {
        사용자 = User.builder()
                  .oAuthId("12345")
                  .oAuthType(OAuthType.KAKAO)
                  .name("사용자")
                  .email("test@email.com")
                  .build();
        final User 삭제된_사용자 = User.builder()
                                 .oAuthId("12346")
                                 .oAuthType(OAuthType.KAKAO)
                                 .name("삭제된 사용자")
                                 .email("test2@email.com")
                                 .build();
        삭제된_사용자.delete();
        userRepository.saveAll(List.of(사용자, 삭제된_사용자));

        유효한_oAuth_아이디 = 사용자.getOAuthId();
        유효한_oAuth_타입 = 사용자.getOAuthType();

        사용자_아이디 = 사용자.getId();
        삭제된_사용자_아이디 = 삭제된_사용자.getId();

        em.flush();
        em.clear();
    }
}