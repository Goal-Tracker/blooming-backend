package com.backend.blooming.stamp.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.presentation.dto.request.CreateStampRequest;

@SuppressWarnings("NonAsciiCharacters")
public class StampControllerTestFixture {

    private Long 골_관리자_아이디 = 1L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(골_관리자_아이디);
    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 유효한_스탬프_아이디 = 1L;

    protected CreateStampDto 유효한_스탬프_생성_dto = new CreateStampDto(
            1L,
            골_관리자_아이디,
            1,
            "스탬프 메시지"
    );
    protected CreateStampRequest 유효한_스탬프_생성_요청_dto = new CreateStampRequest(
            1L,
            1,
            "스탬프 메시지"
    );
    protected CreateStampDto 존재하지_않는_골에서_생성한_스탬프_dto = new CreateStampDto(
            999L,
            사용자_토큰_정보.userId(),
            1,
            "스탬프 메시지"
    );
    protected CreateStampRequest 존재하지_않는_골에서_요청한_스탬프_생성_dto = new CreateStampRequest(
            999L,
            1,
            "스탬프 메시지"
    );
    protected CreateStampDto 권한이_없는_사용자가_생성한_스탬프_dto = new CreateStampDto(
            1L,
            사용자_토큰_정보.userId(),
            1,
            "스탬프 메시지"
    );
    protected CreateStampRequest 권한이_없는_사용자가_생성_요청한_스탬프_dto = new CreateStampRequest(
            1L,
            1,
            "스탬프 메시지"
    );
    protected CreateStampDto 이미_존재하는_스탬프_생성_dto = new CreateStampDto(
            1L,
            사용자_토큰_정보.userId(),
            1,
            "스탬프 메시지"
    );
    protected CreateStampRequest 이미_존재하는_스탬프_생성_요청_dto = new CreateStampRequest(
            1L,
            1,
            "스탬프 메시지"
    );
}
