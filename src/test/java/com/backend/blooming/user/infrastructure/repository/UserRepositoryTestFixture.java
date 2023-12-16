package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.user.domain.User;
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
    protected User 사용자2;
    protected User 검색_키워드가_포함되지_않은_사용자;
    protected User 삭제된_사용자;
    protected String 유효한_oAuth_아이디;
    protected OAuthType 유효한_oAuth_타입;
    protected String 유효하지_않은_oAuth_아이디 = "invalid";
    protected Long 사용자_아이디;
    protected Long 삭제된_사용자_아이디;
    protected Long 존재하지_않는_사용자_아이디 = 9999L;
    protected String 검색_키워드 = "사용자";

    @BeforeEach
    void setUpFixture() {
        사용자 = User.builder()
                  .oAuthId("12345")
                  .oAuthType(OAuthType.KAKAO)
                  .name("사용자")
                  .email("test@email.com")
                  .build();
        사용자2 = User.builder()
                   .oAuthId("12346")
                   .oAuthType(OAuthType.KAKAO)
                   .name("사용자2")
                   .email("test2@email.com")
                   .build();
        검색_키워드가_포함되지_않은_사용자 = User.builder()
                                  .oAuthId("12347")
                                  .oAuthType(OAuthType.KAKAO)
                                  .name("테스트")
                                  .email("test3@email.com")
                                  .build();
        삭제된_사용자 = User.builder()
                      .oAuthId("12348")
                      .oAuthType(OAuthType.KAKAO)
                      .name("삭제된 사용자")
                      .email("test4@email.com")
                      .build();
        삭제된_사용자.delete();
        userRepository.saveAll(List.of(사용자, 사용자2, 검색_키워드가_포함되지_않은_사용자, 삭제된_사용자));

        유효한_oAuth_아이디 = 사용자.getOAuthId();
        유효한_oAuth_타입 = 사용자.getOAuthType();

        사용자_아이디 = 사용자.getId();
        삭제된_사용자_아이디 = 삭제된_사용자.getId();

        em.flush();
        em.clear();
    }
}
