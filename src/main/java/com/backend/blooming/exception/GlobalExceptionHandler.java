package com.backend.blooming.exception;

import com.backend.blooming.authentication.application.exception.UnauthorizedAccessException;
import com.backend.blooming.authentication.infrastructure.exception.InvalidTokenException;
import com.backend.blooming.authentication.infrastructure.exception.OAuthException;
import com.backend.blooming.exception.dto.ExceptionResponse;
import com.backend.blooming.user.application.exception.NotFoundUserException;
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
    private ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        logger.error(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));

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

        final String message = exception.getFieldErrors().get(METHOD_ARGUMENT_FIRST_ERROR_INDEX).getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(message));
    }

    @ExceptionHandler(OAuthException.InvalidAuthorizationTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidAuthorizationTokenExceptionException(
            final OAuthException.InvalidAuthorizationTokenException exception
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(OAuthException.KakaoServerException.class)
    public ResponseEntity<ExceptionResponse> handleKakaoServerExceptionException(
            final OAuthException.KakaoServerException exception
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidTokenExceptionException(
            final InvalidTokenException exception
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedAccessException(
            final UnauthorizedAccessException exception
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundUserException(
            final NotFoundUserException exception
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, exception.getClass().getSimpleName(), exception.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(exception.getMessage()));
    }
}
