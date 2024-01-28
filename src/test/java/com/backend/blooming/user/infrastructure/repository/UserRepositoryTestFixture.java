package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    protected User 사용자;
    protected User 사용자2;
    protected User 삭제된_사용자;
    protected String 유효한_oAuth_아이디;
    protected OAuthType 유효한_oAuth_타입;
    protected String 유효하지_않은_oAuth_아이디 = "invalid";
    protected Long 사용자_아이디;
    protected Long 삭제된_사용자_아이디;
    protected Long 존재하지_않는_사용자_아이디 = 9999L;
    protected List<Long> 사용자_아이디_목록 = new ArrayList<>();

    @BeforeEach
    void setUpFixture() {
        사용자 = User.builder()
                  .oAuthId("12345")
                  .oAuthType(OAuthType.KAKAO)
                  .name(new Name("사용자"))
                  .email(new Email("test@email.com"))
                  .build();
        사용자2 = User.builder()
                   .oAuthId("12346")
                   .oAuthType(OAuthType.KAKAO)
                   .name(new Name("사용자2"))
                   .email(new Email("test2@email.com"))
                   .build();
        삭제된_사용자 = User.builder()
                      .oAuthId("12348")
                      .oAuthType(OAuthType.KAKAO)
                      .name(new Name("삭제된 사용자"))
                      .email(new Email("test4@email.com"))
                      .build();
        삭제된_사용자.delete();
        userRepository.saveAll(List.of(사용자, 사용자2, 삭제된_사용자));

        유효한_oAuth_아이디 = 사용자.getOAuthId();
        유효한_oAuth_타입 = 사용자.getOAuthType();

        사용자_아이디 = 사용자.getId();
        삭제된_사용자_아이디 = 삭제된_사용자.getId();
        사용자_아이디_목록.addAll(List.of(사용자_아이디, 사용자2.getId()));
    }
}
