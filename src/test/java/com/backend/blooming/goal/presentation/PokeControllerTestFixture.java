package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.goal.application.dto.PokeDto;

@SuppressWarnings("NonAsciiCharacters")
public class PokeControllerTestFixture {

    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 유효한_골_아이디 = 1L;
    protected Long 콕_찌르기_요청자 = 1L;
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(콕_찌르기_요청자);
    protected Long 콕_찌르기_수신자 = 2L;
    protected Long 존재하지_않는_골_아이디 = 999L;
    protected Long 존재하지_않는_사용자_아이디 = 999L;
    protected AuthClaims 존재하지_않는_사용자_토큰_정보 = new AuthClaims(존재하지_않는_사용자_아이디);
    protected Long 팀원이_아닌_사용자_아이디 = 3L;
    protected AuthClaims 팀원이_아닌_사용자_토큰_정보 = new AuthClaims(팀원이_아닌_사용자_아이디);
    protected PokeDto 콕_찌르기_요청_dto = new PokeDto(유효한_골_아이디, 콕_찌르기_요청자, 콕_찌르기_수신자);
    protected PokeDto 존재하지_않은_골에_콕_찌르기_요청_dto = new PokeDto(존재하지_않는_골_아이디, 콕_찌르기_요청자, 콕_찌르기_수신자);
    protected PokeDto 존재하지_않은_사용자가_콕_찌르기_요청_dto = new PokeDto(유효한_골_아이디, 존재하지_않는_사용자_아이디, 콕_찌르기_수신자);
    protected PokeDto 존재하지_않은_사용자에게_콕_찌르기_요청_dto = new PokeDto(유효한_골_아이디, 콕_찌르기_요청자, 존재하지_않는_사용자_아이디);
    protected PokeDto 팀원이_아닌_사용자가_콕_찌르기_요청_dto = new PokeDto(유효한_골_아이디, 팀원이_아닌_사용자_아이디, 콕_찌르기_수신자);
    protected PokeDto 팀원이_아닌_사용자에게_콕_찌르기_요청_dto = new PokeDto(유효한_골_아이디, 콕_찌르기_요청자, 팀원이_아닌_사용자_아이디);
}
