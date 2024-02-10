package com.backend.blooming.admin.controller;

import com.backend.blooming.admin.controller.dto.CreateGoalRequest;
import com.backend.blooming.admin.controller.dto.CreateUserRequest;
import com.backend.blooming.admin.controller.dto.FriendStatus;
import com.backend.blooming.admin.controller.dto.RequestFriendRequest;
import com.backend.blooming.admin.controller.dto.UpdateFriendRequest;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.time.LocalDate;
import java.util.List;

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
    protected static Long 친구_요청_하는_사용자_아이디 = 1L;
    protected static Long 친구_요청_받는_사용자_아이디 = 2L;
    protected RequestFriendRequest 친구_요청 = new RequestFriendRequest(친구_요청_하는_사용자_아이디, 친구_요청_받는_사용자_아이디);
    protected static UpdateFriendRequest 친구_상태_친구로_수정_요청 = new UpdateFriendRequest(
            친구_요청_하는_사용자_아이디,
            친구_요청_받는_사용자_아이디,
            FriendStatus.FRIEND.name()
    );
    protected static UpdateFriendRequest 친구_상태_요청_취소로_수정_요청 = new UpdateFriendRequest(
            친구_요청_하는_사용자_아이디,
            친구_요청_받는_사용자_아이디,
            FriendStatus.DELETE_BY_REQUEST.name()
    );
    protected static UpdateFriendRequest 친구_상태_거절로_수정_요청 = new UpdateFriendRequest(
            친구_요청_하는_사용자_아이디,
            친구_요청_받는_사용자_아이디,
            FriendStatus.DELETE_BY_REQUESTED.name()
    );
    protected Long 친구_아이디 = 1L;
    protected CreateGoalRequest 골_생성_요청 = new CreateGoalRequest(
            "골 이름",
            "메모",
            LocalDate.now(),
            LocalDate.now().plusDays(2),
            1L,
            2L
    );
    protected CreateGoalDto 골_생성_요청_dto = new CreateGoalDto(
            골_생성_요청.name(),
            골_생성_요청.memo(),
            골_생성_요청.startDate(),
            골_생성_요청.endDate(),
            골_생성_요청.manager(),
            List.of(골_생성_요청.manager(), 골_생성_요청.team())
    );
    protected Long 골_아이디 = 1L;
}
