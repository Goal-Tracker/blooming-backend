package com.backend.blooming.report.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.report.application.dto.CreateGoalReportDto;
import com.backend.blooming.report.application.dto.CreateStampReportDto;
import com.backend.blooming.report.application.dto.CreateUserReportDto;
import com.backend.blooming.report.presentation.dto.request.CreateReportRequest;

@SuppressWarnings("NonAsciiCharacters")
public class ReportControllerTestFixture {

    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 사용자_아이디 = 1L;
    protected Long 신고_대상_아이디 = 2L;
    protected Long 팀원이_아닌_사용자_아이디 = 3L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(사용자_아이디);
    protected AuthClaims 팀원이_아닌_사용자_토큰_정보 = new AuthClaims(팀원이_아닌_사용자_아이디);
    protected Long 생성된_신고_아이디 = 1L;


    private String 신고_내용 = "신고합니다.";
    protected CreateReportRequest 신고_요청 = new CreateReportRequest(신고_내용);
    protected CreateReportRequest 내용이_없는_신고_요청 = new CreateReportRequest(null);
    protected CreateUserReportDto 사용자_신고_요청_dto = new CreateUserReportDto(사용자_아이디, 신고_대상_아이디, 신고_내용);
    protected CreateUserReportDto 본인_신고_요청_dto = new CreateUserReportDto(사용자_아이디, 사용자_아이디, 신고_내용);
    protected CreateGoalReportDto 골_신고_요청_dto = new CreateGoalReportDto(사용자_아이디, 신고_대상_아이디, 신고_내용);
    protected CreateGoalReportDto 관리자인_골_신고_요청_dto = new CreateGoalReportDto(사용자_아이디, 신고_대상_아이디, 신고_내용);
    protected CreateGoalReportDto 팀원이_아닌_사용자의_골_신고_요청_dto = new CreateGoalReportDto(팀원이_아닌_사용자_아이디, 신고_대상_아이디, 신고_내용);
    protected CreateStampReportDto 스탬프_신고_요청_dto = new CreateStampReportDto(사용자_아이디, 신고_대상_아이디, 신고_내용);
    protected CreateStampReportDto 본인의_스탬프_신고_요청_dto = new CreateStampReportDto(사용자_아이디, 신고_대상_아이디, 신고_내용);
    protected CreateStampReportDto 팀원이_아닌_사용자의_스탬프_신고_요청_dto = new CreateStampReportDto(팀원이_아닌_사용자_아이디, 신고_대상_아이디, 신고_내용);
}
