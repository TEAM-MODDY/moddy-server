package com.moddy.server.common.exception;

import com.moddy.server.common.dto.ErrorDataResponse;
import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.common.exception.model.BadRequestException;
import com.moddy.server.common.exception.model.ConflictException;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.common.exception.model.NotFoundUserException;
import com.moddy.server.common.exception.model.UnAuthorizedException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.moddy.server.common.exception.enums.ErrorCode.INTERNAL_SERVER_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.INVALID_TOKEN_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.INVALID_VALUE_TYPE_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.METHOD_NOT_ALLOWED_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionAdvice {
    /**
     * 400 Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    protected ErrorResponse handleBadRequestException(final BadRequestException e) {
        return ErrorResponse.error(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ErrorResponse handleTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        return ErrorResponse.error(INVALID_VALUE_TYPE_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeignException.class)
    protected ErrorResponse handleFeignException(final FeignException e) {
        log.info(e.getMessage());
        return ErrorResponse.error(INVALID_TOKEN_EXCEPTION);
    }

    /**
     * 401 UnAuthorization
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    protected ErrorResponse handleUnAuthorizedException(final UnAuthorizedException e) {
        return ErrorResponse.error(e.getErrorCode());
    }

    /**
     * 404 Not Found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    protected ErrorResponse handleNotFoundException(final NotFoundException e) {
        return ErrorResponse.error(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundUserException.class)
    protected ErrorDataResponse<TokenPair> handleNotFoundUserException(final NotFoundUserException e) {
        return ErrorDataResponse.error(e.getErrorCode(), e.getTokenPair());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    protected ErrorResponse handleNoResourceFoundException(final NoResourceFoundException e) {
        return ErrorResponse.error(NOT_FOUND_RESOURCE_EXCEPTION);
    }

    /**
     * 405 Method Not Allowed
     * 지원하지 않은 HTTP method 호출 할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return ErrorResponse.error(METHOD_NOT_ALLOWED_EXCEPTION);
    }

    /**
     * 409 Conflict
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    protected ErrorResponse handleConflictException(final ConflictException e) {
        return ErrorResponse.error(e.getErrorCode());
    }

    /**
     * 500 Internal Server
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(final Exception error) {
        log.error(error.getMessage(), error);
        return ErrorResponse.error(INTERNAL_SERVER_EXCEPTION);
    }
}
