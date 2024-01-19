package com.moddy.server.config.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String START_LOG = "================================================NEW===============================================\n";
    private static final String END_LOG = "================================================END===============================================\n";
    private static final String MULTI_PART_FORM_DATA = "multipart/form-data";

    @Pointcut("execution(* com.moddy.server.controller..*(..)) || ( execution(* com.moddy.server.common.exception..*(..)) && !execution(* com.moddy.server.common.exception.GlobalControllerExceptionAdvice.handleException*(..)))")
    public void controllerInfoLevelExecute() {
    }

    @Pointcut("execution(* com.moddy.server.common.exception.GlobalControllerExceptionAdvice.handleException*(..))")
    public void controllerErrorLevelExecute() {
    }

    @Around("com.moddy.server.config.aspect.LoggingAspect.controllerInfoLevelExecute()")
    public Object requestInfoLevelLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        long startAt = System.currentTimeMillis();
        Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long endAt = System.currentTimeMillis();

        log.info(getCommunicationData(request, cachingRequest, startAt, endAt, returnValue));
        return returnValue;
    }

    @Around("com.moddy.server.config.aspect.LoggingAspect.controllerErrorLevelExecute()")
    public Object requestErrorLevelLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        long startAt = System.currentTimeMillis();
        Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long endAt = System.currentTimeMillis();

        log.error(getCommunicationData(request, cachingRequest, startAt, endAt, returnValue));
        return returnValue;
    }

    private String getCommunicationData(
            HttpServletRequest request,
            ContentCachingRequestWrapper cachingRequest,
            long startAt,
            long endAt,
            Object returnValue
    ) throws IOException, ServletException {
        StringBuilder sb = new StringBuilder();

        sb.append(START_LOG);
        sb.append(String.format("====> Request: %s %s ({%d}ms)\n====> *Header = {%s}\n", request.getMethod(), request.getRequestURL(), endAt - startAt, getHeaders(request)));
        sb.append("=================> content type is ").append(request.getContentType()).append("\n");
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getContentType() != null && request.getContentType().contains(MULTI_PART_FORM_DATA)) {
            sb.append("====> form-data Body: ");
            cachingRequest.getParts().stream().map(Part::getName).forEach(n -> {
                String keyValue = String.format("%s = %s", n, request.getParameter(n));
                sb.append(keyValue).append(", ");
            });
            sb.append("\n");
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            sb.append(String.format("====> application/json Body: {%s}\n", objectMapper.readTree(cachingRequest.getContentAsByteArray())));
        }
        if (returnValue != null) {
            sb.append(String.format("====> Response: {%s}\n", returnValue));
        }
        sb.append(END_LOG);
        return sb.toString();
    }

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }
}
