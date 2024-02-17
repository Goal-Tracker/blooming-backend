package com.backend.blooming.goal.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.application.dto.PokeDto;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class PokeServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    protected PokeDto 콕_찌르기_요청_dto;
    protected Long 콕_찌르기를_받은_사용자_아이디;
    protected Long 콕_찌르기를_요청한_사용자_아이디;
    protected PokeDto 존재하지_않는_골에_대한_콕_찌르기_요청_dto;
    protected PokeDto 존재하지_않는_사용자가_콕_찌르기_요청_dto;
    protected PokeDto 존재하지_않는_사용자에게_콕_찌르기_요청_dto;
    protected PokeDto 골에_포함되어_있지_않는_사용자가_콕_찌르기_요청_dto;
    protected PokeDto 골에_포함되어_있지_않는_사용자에게_콕_찌르기_요청_dto;

    @BeforeEach
    void setUpFixture() {
        final User 콕_찌르기_요청자 = User.builder()
                                   .oAuthId("12345")
                                   .oAuthType(OAuthType.KAKAO)
                                   .name(new Name("사용자1"))
                                   .email(new Email("test1@email.com"))
                                   .build();
        final User 콕_찌르기_수신자 = User.builder()
                                   .oAuthId("12346")
                                   .oAuthType(OAuthType.KAKAO)
                                   .name(new Name("사용자2"))
                                   .email(new Email("test2@email.com"))
                                   .build();
        final User 팀원이_아닌_사용자 = User.builder()
                                    .oAuthId("12347")
                                    .oAuthType(OAuthType.KAKAO)
                                    .name(new Name("사용자3"))
                                    .email(new Email("test3@email.com"))
                                    .build();
        userRepository.saveAll(List.of(콕_찌르기_요청자, 콕_찌르기_수신자, 팀원이_아닌_사용자));

        콕_찌르기를_요청한_사용자_아이디 = 콕_찌르기_요청자.getId();
        콕_찌르기를_받은_사용자_아이디 = 콕_찌르기_수신자.getId();
        final List<User> 골_참여_사용자_목록 = List.of(콕_찌르기_요청자, 콕_찌르기_수신자);

        final Goal 골 = Goal.builder()
                           .name("골 제목")
                           .memo("골 메모")
                           .startDate(LocalDate.now())
                           .endDate(LocalDate.now().plusDays(20))
                           .managerId(콕_찌르기를_요청한_사용자_아이디)
                           .users(골_참여_사용자_목록)
                           .build();
        goalRepository.save(골);
        final Long 골_아이디 = 골.getId();

        final Long 존재하지_않는_골_아이디 = 999L;
        final Long 존재하지_않는_사용자_아이디 = 999L;
        final Long 팀원이_아닌_사용자_아이디 = 팀원이_아닌_사용자.getId();
        콕_찌르기_요청_dto = new PokeDto(골_아이디, 콕_찌르기를_요청한_사용자_아이디, 콕_찌르기를_받은_사용자_아이디);
        존재하지_않는_골에_대한_콕_찌르기_요청_dto = new PokeDto(존재하지_않는_골_아이디, 콕_찌르기를_요청한_사용자_아이디, 콕_찌르기를_받은_사용자_아이디);
        존재하지_않는_사용자가_콕_찌르기_요청_dto = new PokeDto(골_아이디, 존재하지_않는_사용자_아이디, 콕_찌르기를_받은_사용자_아이디);
        존재하지_않는_사용자에게_콕_찌르기_요청_dto = new PokeDto(골_아이디, 콕_찌르기를_요청한_사용자_아이디, 존재하지_않는_사용자_아이디);
        골에_포함되어_있지_않는_사용자가_콕_찌르기_요청_dto = new PokeDto(골_아이디, 팀원이_아닌_사용자_아이디, 콕_찌르기를_받은_사용자_아이디);
        골에_포함되어_있지_않는_사용자에게_콕_찌르기_요청_dto = new PokeDto(골_아이디, 콕_찌르기를_요청한_사용자_아이디, 팀원이_아닌_사용자_아이디);
    }
}
