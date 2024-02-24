package com.backend.blooming.report.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.report.application.dto.CreateStampReportDto;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.StampReport;
import com.backend.blooming.report.infrastructure.repository.StampReportRepository;
import com.backend.blooming.stamp.domain.Day;
import com.backend.blooming.stamp.domain.Message;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.stamp.infrastructure.repository.StampRepository;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class StampReportServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private StampRepository stampRepository;

    @Autowired
    private StampReportRepository stampReportRepository;

    protected CreateStampReportDto 스탬프_신고_요청_dto;
    protected CreateStampReportDto 이미_신고한_사용자가_스탬프_신고_요청_dto;
    protected CreateStampReportDto 존재하지_않는_사용자가_스탬프_신고_요청_dto;
    protected CreateStampReportDto 존재하지_않는_스탬프_신고_요청_dto;
    protected CreateStampReportDto 본인의_스탬프_신고_요청_dto;
    protected CreateStampReportDto 팀원이_아닌_사용자가_스탬프_신고_요청_dto;

    @BeforeEach
    void setUpFixture() {
        final User 사용자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자"))
                             .email(new Email("test@email.com"))
                             .build();
        final User 스탬프_작성자 = User.builder()
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
        userRepository.saveAll(List.of(사용자, 스탬프_작성자, 이미_신고한_팀원, 팀원이_아닌_사용자));

        final Goal 골 = Goal.builder()
                           .name("골 제목")
                           .memo("골 메모")
                           .startDate(LocalDate.now())
                           .endDate(LocalDate.now().plusDays(1L))
                           .managerId(사용자.getId())
                           .users(List.of(사용자, 스탬프_작성자, 이미_신고한_팀원))
                           .build();
        goalRepository.save(골);

        final Stamp 스탬프 = Stamp.builder()
                               .goal(골)
                               .user(스탬프_작성자)
                               .day(new Day(골.getGoalTerm(), 1))
                               .message(new Message("스탬프 메시지"))
                               .build();
        stampRepository.save(스탬프);

        final String 신고_내용 = "신고합니다.";
        final StampReport 스탬프_신고 = new StampReport(이미_신고한_팀원, 스탬프, new Content(신고_내용));
        stampReportRepository.save(스탬프_신고);

        final Long 존재하지_않는_사용자_아이디 = 999L;
        final Long 존재하지_않는_스탬프_아이디 = 999L;
        스탬프_신고_요청_dto = new CreateStampReportDto(사용자.getId(), 스탬프.getId(), 신고_내용);
        이미_신고한_사용자가_스탬프_신고_요청_dto = new CreateStampReportDto(이미_신고한_팀원.getId(), 스탬프.getId(), 신고_내용);
        존재하지_않는_사용자가_스탬프_신고_요청_dto = new CreateStampReportDto(존재하지_않는_사용자_아이디, 스탬프.getId(), 신고_내용);
        존재하지_않는_스탬프_신고_요청_dto = new CreateStampReportDto(사용자.getId(), 존재하지_않는_스탬프_아이디, 신고_내용);
        본인의_스탬프_신고_요청_dto = new CreateStampReportDto(스탬프_작성자.getId(), 스탬프.getId(), 신고_내용);
        팀원이_아닌_사용자가_스탬프_신고_요청_dto = new CreateStampReportDto(팀원이_아닌_사용자.getId(), 스탬프.getId(), 신고_내용);
    }
}
