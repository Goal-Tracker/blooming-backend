package com.backend.blooming.stamp.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.application.dto.ReadStampDto;
import com.backend.blooming.stamp.application.dto.ReadAllStampDto;
import com.backend.blooming.stamp.presentation.dto.request.CreateStampRequest;
import com.backend.blooming.stamp.presentation.dto.response.ReadStampResponse;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.stamp.presentation.dto.response.ReadAllStampResponse;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class StampControllerTestFixture {

    private Long 골_관리자_아이디 = 1L;
    protected Long 골_참여자가_아닌_사용자_아이디 = 999L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(골_관리자_아이디);
    protected AuthClaims 골_참여자가_아닌_사용자_토큰_정보 = new AuthClaims(골_참여자가_아닌_사용자_아이디);
    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 유효한_골_아이디 = 1L;
    protected Long 존재하지_않는_골_아이디 = 999L;

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
    protected ReadStampDto 추가한_스탬프_dto = new ReadStampDto(
            1L,
            "스탬프 추가한 사용자",
            ThemeColor.BABY_BLUE,
            1,
            "스탬프 메시지"
    );
    private ReadAllStampDto.StampDto 유효한_스탬프_dto = new ReadAllStampDto.StampDto(
            1L,
            "스탬프 추가한 사용자",
            ThemeColor.BABY_BLUE,
            "스탬프 메시지1",
            1
    );
    private ReadAllStampDto.StampDto 유효한_스탬프_dto2 = new ReadAllStampDto.StampDto(
            1L,
            "스탬프 추가한 사용자",
            ThemeColor.BABY_BLUE,
            "스탬프 메시지2",
            2
    );
    private ReadAllStampDto.StampDto 유효한_스탬프_dto3 = new ReadAllStampDto.StampDto(
            2L,
            "스탬프 추가한 사용자2",
            ThemeColor.INDIGO,
            "스탬프 메시지3",
            2
    );
    protected ReadStampResponse 유효한_스탬프_응답_dto = new ReadStampResponse(
            1L,
            "스탬프 추가한 사용자",
            ThemeColor.BABY_BLUE.getCode(),
            1,
            "스탬프 메시지"
    );
    protected ReadAllStampDto 유효한_스탬프_목록_dto = new ReadAllStampDto(List.of(유효한_스탬프_dto, 유효한_스탬프_dto2, 유효한_스탬프_dto3));
    protected ReadAllStampResponse.StampInfoResponse 유효한_스탬프_응답_정보1 = new ReadAllStampResponse.StampInfoResponse(
            1L,
            "스탬프 추가한 사용자",
            ThemeColor.BABY_BLUE.getCode(),
            "스탬프 메시지1",
            1
    );
    protected ReadAllStampResponse.StampInfoResponse 유효한_스탬프_응답_정보2 = new ReadAllStampResponse.StampInfoResponse(
            1L,
            "스탬프 추가한 사용자",
            ThemeColor.BABY_BLUE.getCode(),
            "스탬프 메시지2",
            2
    );
    protected ReadAllStampResponse.StampInfoResponse 유효한_스탬프_응답_정보3 = new ReadAllStampResponse.StampInfoResponse(
            2L,
            "스탬프 추가한 사용자2",
            ThemeColor.INDIGO.getCode(),
            "스탬프 메시지3",
            2
    );
    protected ReadAllStampResponse 유효한_스탬프_목록_응답 = new ReadAllStampResponse(
            new HashMap<>(
                    Map.of(1, List.of(유효한_스탬프_응답_정보1),
                            2, List.of(유효한_스탬프_응답_정보2, 유효한_스탬프_응답_정보3)
                    )
            )
    );
}
