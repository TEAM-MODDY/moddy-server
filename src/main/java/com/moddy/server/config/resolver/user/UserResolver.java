package com.moddy.server.config.resolver.user;

import com.moddy.server.common.exception.model.BadRequestException;
import com.moddy.server.common.exception.model.UnAuthorizedException;
import com.moddy.server.config.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.moddy.server.common.exception.enums.ErrorCode.INVALID_TOKEN_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.TOKEN_NOT_CONTAINED_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.TOKEN_TIME_EXPIRED_EXCEPTION;

@Component
@RequiredArgsConstructor
public class UserResolver implements HandlerMethodArgumentResolver {
    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class) && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = request.getHeader("Authorization");
        if (token == null || token.isBlank() || !token.startsWith("Bearer ")) {
            throw new UnAuthorizedException(TOKEN_NOT_CONTAINED_EXCEPTION);
        }
        final String encodedUserId = token.substring("Bearer ".length());
        if (!jwtService.verifyToken(encodedUserId)) {
            throw new UnAuthorizedException(TOKEN_TIME_EXPIRED_EXCEPTION);
        }
        final String decodedUserId = jwtService.getUserIdInToken(encodedUserId);
        try {
            return Long.parseLong(decodedUserId);
        } catch (NumberFormatException e) {
            throw new BadRequestException(INVALID_TOKEN_EXCEPTION);
        }
    }
}
