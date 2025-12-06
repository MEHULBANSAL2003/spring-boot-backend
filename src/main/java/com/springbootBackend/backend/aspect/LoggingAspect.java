package com.springbootBackend.backend.aspect;

import com.springbootBackend.backend.dto.kafkaLogEvent.LogEvent;
import com.springbootBackend.backend.service.loggingService.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

  @Autowired
  private LoggingService loggingService;

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void controllerMethods() {}

  @Around("controllerMethods()")
  public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {

    HttpServletRequest request =
      ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    String traceId = UUID.randomUUID().toString();
    Object response = null;
    int status = 200;
    String errorMessage = null;
    String exceptionStackTrace = null;
    boolean isErrorHandled = false;

    long start = System.currentTimeMillis();

    try {
      response = joinPoint.proceed();
    } catch (Exception e) {
      status = 500;
      errorMessage = e.getMessage();
      exceptionStackTrace = Arrays.toString(e.getStackTrace());
      isErrorHandled = false;
      throw e; // rethrow so normal Spring exception handling occurs
    } finally {
      long end = System.currentTimeMillis();

      // Headers
      Map<String, String> headers = Collections.list(request.getHeaderNames())
        .stream()
        .collect(Collectors.toMap(h -> h, request::getHeader));

      // Query params
      Map<String, String> queryParams = request.getParameterMap()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())));

      // Client IP
      String clientIp = request.getHeader("X-FORWARDED-FOR");
      if (clientIp == null) clientIp = request.getRemoteAddr();

      // User agent
      String userAgent = request.getHeader("User-Agent");

      // Optional: User ID from JWT/header/session
      String requestUserId = request.getHeader("X-User-Id"); // adapt per your auth

      // Session ID
      String sessionId = request.getSession(false) != null ? request.getSession(false).getId() : null;

      // Content type / accept
      String contentType = request.getContentType();
      String acceptType = request.getHeader("Accept");

      // Message (generic or from response)
      String message = (response != null) ? "SUCCESS" : (status == 500 ? "FAILED" : null);

      LogEvent event = LogEvent.builder()
        .serviceName("AUTH-SERVICE")
        .endpoint(request.getRequestURI())
        .fullUrl(request.getRequestURL().toString() +
          (request.getQueryString() != null ? "?" + request.getQueryString() : ""))
        .requestMethod(request.getMethod())
        .queryParams(queryParams)
        .headers(headers)
        .requestBody(joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : null)
        .responseBody(response)
        .statusCode(status)
        .error(errorMessage)
        .exceptionStackTrace(exceptionStackTrace)
        .isErrorHandled(isErrorHandled)
        .message(message)
        .requestTime(start)
        .responseTime(end)
        .duration(end - start)
        .traceId(traceId)
        .requestUserId(requestUserId)
        .clientIp(clientIp)
        .userAgent(userAgent)
        .sessionId(sessionId)
        .contentType(contentType)
        .acceptType(acceptType)
        .build();

      loggingService.publish(event);
    }

    return response;
  }
}
