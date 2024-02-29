package com.backend.blooming.exception;

import com.backend.blooming.authentication.application.exception.AlreadyRegisterBlackListTokenException;
import com.backend.blooming.authentication.infrastructure.exception.InvalidTokenException;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.authentication.infrastructure.exception.UnsupportedOAuthTypeException;
import com.backend.blooming.exception.dto.ExceptionResponse;
import com.backend.blooming.friend.application.exception.DeleteFriendForbiddenException;
import com.backend.blooming.friend.application.exception.FriendAcceptanceForbiddenException;
import com.backend.blooming.friend.application.exception.FriendRequestNotAllowedException;
import com.backend.blooming.friend.application.exception.NotFoundFriendRequestException;
import com.backend.blooming.goal.application.exception.DeleteGoalForbiddenException;
import com.backend.blooming.goal.application.exception.ForbiddenGoalToPokeException;
import com.backend.blooming.goal.application.exception.InvalidGoalException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.application.exception.UpdateGoalForbiddenException;
import com.backend.blooming.notification.application.exception.NotFoundGoalManagerException;
import com.backend.blooming.stamp.application.exception.CreateStampForbiddenException;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import com.backend.blooming.report.application.exception.InvalidGoalReportException;
import com.backend.blooming.report.application.exception.InvalidStampReportException;
import com.backend.blooming.report.application.exception.InvalidUserReportException;
import com.backend.blooming.report.application.exception.ReportForbiddenException;
import com.backend.blooming.stamp.application.exception.CreateStampForbiddenException;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import com.backend.blooming.themecolor.domain.exception.UnsupportedThemeColorException;
import com.backend.blooming.user.application.exception.DuplicateUserNameException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_MESSAGE_FORMAT = "%s : %s";
    private static final int METHOD_ARGUMENT_FIRST_ERROR_INDEX = 0;

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ExceptionResponse> handleException(
            final Exception exception,
            final HttpServletRequest request
    ) {
        logError(exception, request);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ExceptionResponse("예상치 못한 문제가 발생했습니다."));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception,
            final HttpHeaders ignoredHeaders,
            final HttpStatusCode ignoredStatus,
            final WebRequest ignoredRequest
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));

        final String message = exception.getFieldErrors()
                                        .get(METHOD_ARGUMENT_FIRST_ERROR_INDEX)
                                        .getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(message));
    }

    @ExceptionHandler(OAuthException.InvalidAuthorizationTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidAuthorizationTokenExceptionException(
            final OAuthException.InvalidAuthorizationTokenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(OAuthException.KakaoServerUnavailableException.class)
    public ResponseEntity<ExceptionResponse> handleKakaoServerExceptionException(
            final OAuthException.KakaoServerUnavailableException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidTokenExceptionException(
            final InvalidTokenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(UnsupportedOAuthTypeException.class)
    public ResponseEntity<ExceptionResponse> handleUnsupportedOAuthTypeException(
            final UnsupportedOAuthTypeException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(UnsupportedThemeColorException.class)
    public ResponseEntity<ExceptionResponse> handleUnsupportedThemeColorException(
            final UnsupportedThemeColorException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundUserException(
            final NotFoundUserException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidGoalException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidGoalException(
            final InvalidGoalException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundGoalException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundGoalException(
            final NotFoundGoalException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(FriendRequestNotAllowedException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyRequestedFriendException(
            final FriendRequestNotAllowedException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundFriendRequestException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundFriendRequestException(
            final NotFoundFriendRequestException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(FriendAcceptanceForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleFriendAcceptanceForbiddenException(
            final FriendAcceptanceForbiddenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(DeleteFriendForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleDeleteFriendForbiddenException(
            final DeleteFriendForbiddenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(DeleteGoalForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleDeleteGoalForbiddenException(
            final DeleteGoalForbiddenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(UpdateGoalForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleUpdateGoalForbiddenException(
            final UpdateGoalForbiddenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(AlreadyRegisterBlackListTokenException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyRegisterBlackListTokenException(
            final AlreadyRegisterBlackListTokenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenGoalToPokeException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenGoalToPokeException(
            final ForbiddenGoalToPokeException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundGoalManagerException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundGoalManagerException(
            final NotFoundGoalManagerException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicateUserNameException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateUserNameException(
            final DuplicateUserNameException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidStampException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidStampException(
            final InvalidStampException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(CreateStampForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleCreateStampForbiddenException(
            final CreateStampForbiddenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidUserReportException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidUserReportException(
            final InvalidUserReportException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidGoalReportException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidGoalReportException(
            final InvalidGoalReportException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidStampReportException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidStampReportException(
            final InvalidStampReportException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(ReportForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleReportForbiddenException(
            final ReportForbiddenException exception, final HttpServletRequest request
    ) {
        logWarn(exception, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    private void logError(final Exception exception, final HttpServletRequest request) {
        setMDC(request);

        logger.error(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));
    }

    private void logWarn(final Exception exception, final HttpServletRequest request) {
        setMDC(request);

        logger.warn(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));
    }

    private void setMDC(final HttpServletRequest request) {
        MDC.put("version", request.getHeader("X-API-VERSION"));
        MDC.put("method", request.getMethod());
        MDC.put("uri", getRequestURI(request));
    }

    private static String getRequestURI(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        final String queryString = request.getQueryString();

        if (queryString != null) {
            requestURI = requestURI + "?" + queryString;
        }

        return requestURI;
    }
}
