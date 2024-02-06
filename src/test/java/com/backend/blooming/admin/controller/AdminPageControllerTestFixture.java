package com.backend.blooming.admin.controller;

import com.backend.blooming.admin.controller.dto.CreateUserRequest;
import com.backend.blooming.admin.controller.dto.RequestFriendRequest;
import com.backend.blooming.themecolor.domain.ThemeColor;

@SuppressWarnings("NonAsciiCharacters")
public class AdminPageControllerTestFixture {

    protected CreateUserRequest 사용자_생성_요청 = new CreateUserRequest(
            "사용자",
            "test@email.com",
            ThemeColor.INDIGO.name(),
            "상태 메시지"
    );
    protected Long 사용자_아아디 = 1L;
    protected Long 친구_생성_아이디 = 1L;
    protected Long 친구_요청_하는_사용자_아이디 = 1L;
    protected Long 친구_요청_받는_사용자_아이디 = 2L;
    protected RequestFriendRequest 친구_요청 = new RequestFriendRequest(친구_요청_하는_사용자_아이디, 친구_요청_받는_사용자_아이디);
}
