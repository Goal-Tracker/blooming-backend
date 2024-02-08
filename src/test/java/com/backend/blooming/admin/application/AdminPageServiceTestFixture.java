package com.backend.blooming.admin.application;

import com.backend.blooming.admin.controller.dto.CreateUserRequest;
import com.backend.blooming.themecolor.domain.ThemeColor;

@SuppressWarnings("NonAsciiCharacters")
public class AdminPageServiceTestFixture {

    protected CreateUserRequest 사용자_생성_요청 = new CreateUserRequest(
            "사용자",
            "test@email.com",
            ThemeColor.INDIGO.name(),
            "상태 메시지"
    );
    protected CreateUserRequest 이메일_없이_사용자_생성_요청 = new CreateUserRequest(
            "사용자",
            null,
            ThemeColor.INDIGO.name(),
            "상태 메시지"
    );
}
