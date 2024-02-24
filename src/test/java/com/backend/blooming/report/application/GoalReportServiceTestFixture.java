package com.backend.blooming.report.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.report.application.dto.CreateGoalReportDto;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.GoalReport;
import com.backend.blooming.report.infrastructure.repository.GoalReportRepository;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalReportServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalReportRepository goalReportRepository;

    protected CreateGoalReportDto 골_신고_요청_dto;
    protected CreateGoalReportDto 이미_신고한_사용자가_다시_신고_요청_dto;
    protected CreateGoalReportDto 존재하지_않는_사람의_골_신고_요청_dto;
    protected CreateGoalReportDto 존재하지_않는_골_신고_요청_dto;
    protected CreateGoalReportDto 관리자가_골_신고_요청_dto;
    protected CreateGoalReportDto 골_참여자가_아닌_사람을_골_신고_요청_dto;

    @BeforeEach
    void setUpFixture() {
        final User 사용자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자"))
                             .email(new Email("test@email.com"))
                             .build();
        final User 사용자2 = User.builder()
                              .oAuthId("12346")
                              .oAuthType(OAuthType.KAKAO)
                              .name(new Name("사용자2"))
                              .email(new Email("test2@email.com"))
                              .build();
        final User 이미_신고한_팀원 = User.builder()
                                    .oAuthId("12347")
                                    .oAuthType(OAuthType.KAKAO)
                                    .name(new Name("사용자3"))
                                    .email(new Email("test3@email.com"))
                                    .build();
        final User 팀원이_아닌_사용자 = User.builder()
                                    .oAuthId("12348")
                                    .oAuthType(OAuthType.KAKAO)
                                    .name(new Name("사용자4"))
                                    .email(new Email("test4@email.com"))
                                    .build();
        userRepository.saveAll(List.of(사용자, 사용자2, 이미_신고한_팀원, 팀원이_아닌_사용자));

        final Goal 골 = Goal.builder()
                           .name("골 제목")
                           .memo("골 메모")
                           .startDate(LocalDate.now())
                           .endDate(LocalDate.now().plusDays(1L))
                           .managerId(사용자.getId())
                           .users(List.of(사용자, 사용자2, 이미_신고한_팀원))
                           .build();
        goalRepository.save(골);

        final String 신고_내용 = "신고합니다";
        final GoalReport 골_신고 = new GoalReport(이미_신고한_팀원, 골, new Content(신고_내용));
        goalReportRepository.save(골_신고);

        final Long 존재하지_않는_사용자_아이디 = 999L;
        final Long 존재하지_않는_골_아이디 = 999L;
        골_신고_요청_dto = new CreateGoalReportDto(사용자2.getId(), 골.getId(), 신고_내용);
        이미_신고한_사용자가_다시_신고_요청_dto = new CreateGoalReportDto(이미_신고한_팀원.getId(), 골.getId(), 신고_내용);
        존재하지_않는_사람의_골_신고_요청_dto = new CreateGoalReportDto(존재하지_않는_사용자_아이디, 골.getId(), 신고_내용);
        존재하지_않는_골_신고_요청_dto = new CreateGoalReportDto(사용자2.getId(), 존재하지_않는_골_아이디, 신고_내용);
        관리자가_골_신고_요청_dto = new CreateGoalReportDto(사용자.getId(), 골.getId(), 신고_내용);
        골_참여자가_아닌_사람을_골_신고_요청_dto = new CreateGoalReportDto(팀원이_아닌_사용자.getId(), 골.getId(), 신고_내용);
    }
}
