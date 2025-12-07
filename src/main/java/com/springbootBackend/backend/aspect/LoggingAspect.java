package com.springbootBackend.backend.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootBackend.backend.ErrorResponse.CustomErrorMsgFormat;
import com.springbootBackend.backend.dto.kafkaLogEvent.LogEvent;
import com.springbootBackend.backend.service.loggingService.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

  @Autowired
  private LoggingService loggingService;

  @Value("${spring.application.name:AUTH-SERVICE}")
  private String serviceName;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void controllerMethods() {}

  @Around("controllerMethods()")
  public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
    HttpServletRequest request = getHttpServletRequest();
    if (request == null) {
      return joinPoint.proceed();
    }

    String traceId = UUID.randomUUID().toString();
    long requestTime = System.currentTimeMillis();

    Object response = null;
    Throwable caughtException = null;

    try {
      response = joinPoint.proceed();
      return response;
    } catch (Throwable e) {
      caughtException = e;
      throw e;
    } finally {
      long responseTime = System.currentTimeMillis();
      long duration = responseTime - requestTime;

      try {
        LogEvent event = buildLogEvent(
          request,
          joinPoint,
          response,
          caughtException,
          traceId,
          requestTime,
          responseTime,
          duration
        );
        loggingService.publish(event);
      } catch (Exception loggingException) {
        // Don't let logging errors break the application
        System.err.println("Failed to log event: " + loggingException.getMessage());
        loggingException.printStackTrace();
      }
    }
  }

  private LogEvent buildLogEvent(
    HttpServletRequest request,
    ProceedingJoinPoint joinPoint,
    Object response,
    Throwable exception,
    String traceId,
    long requestTime,
    long responseTime,
    long duration
  ) {
    // Extract headers
    Map<String, String> headers = extractHeaders(request);

    // Extract query params
    Map<String, String> queryParams = extractQueryParams(request);

    // Get user ID from request attribute (set by JWT filter)
    String requestUserId = extractUserIdFromRequest(request);

    // Client IP
    String clientIp = getClientIp(request);

    // User agent
    String userAgent = request.getHeader("User-Agent");

    // Content type / accept
    String contentType = request.getContentType();
    String acceptType = request.getHeader("Accept");

    // HTTP version
    String httpVersion = request.getProtocol();

    // Request body
    Object requestBody = joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : null;
    Integer requestBodySize = calculateBodySize(requestBody);

    // Determine response details
    int statusCode;
    String statusCodeType;
    String apiResponseStatus;
    Object responseBody = null;
    Integer responseBodySize = null;
    String errorMessage = null;
    String exceptionType = null;
    String exceptionStackTrace = null;

    if (exception != null) {
      // Exception was thrown - will be handled by GlobalExceptionHandler
      // But we still need to log it
      exceptionType = exception.getClass().getSimpleName();
      errorMessage = exception.getMessage();
      exceptionStackTrace = getStackTraceAsString(exception);

      // Default values - actual response will be created by GlobalExceptionHandler
      statusCode = 500;
      statusCodeType = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
      apiResponseStatus = "FAILURE";

    } else if (response instanceof ResponseEntity) {
      // ResponseEntity case - includes responses from GlobalExceptionHandler
      ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
      statusCode = responseEntity.getStatusCode().value();
      statusCodeType = HttpStatus.valueOf(statusCode).getReasonPhrase();
      apiResponseStatus = statusCode >= 200 && statusCode < 300 ? "SUCCESS" : "FAILURE";

      Object body = responseEntity.getBody();
      responseBody = body;
      responseBodySize = calculateBodySize(body);

      // Extract error details from CustomErrorMsgFormat if present
      if (body instanceof CustomErrorMsgFormat) {
        CustomErrorMsgFormat errorResponse = (CustomErrorMsgFormat) body;
        errorMessage = errorResponse.getMessage();
        statusCodeType = errorResponse.getError(); // This contains the reason phrase
        // Note: exception type and stack trace won't be available here
        // as they weren't thrown in the aspect's scope
      }

    } else {
      // Normal successful response (not wrapped in ResponseEntity)
      statusCode = 200;
      statusCodeType = HttpStatus.OK.getReasonPhrase();
      apiResponseStatus = "SUCCESS";
      responseBody = response;
      responseBodySize = calculateBodySize(responseBody);
    }

    // Build full URL
    String fullUrl = request.getRequestURL().toString();
    if (request.getQueryString() != null) {
      fullUrl += "?" + request.getQueryString();
    }

    return LogEvent.builder()
      // Required fields
      .serviceName("AUTH_LOGS")
      .fullUrl(fullUrl)
      .apiEndpoint(request.getRequestURI())
      .requestMethod(request.getMethod())
      .requestHeaders(headers)
      .requestQueryParams(queryParams)
      .requestTime(requestTime)
      .responseTime(responseTime)
      .requestUserId(requestUserId)
      .apiResponseTime(duration)
      .apiResponseTimeUnits("ms")
      .apiResponseStatus(apiResponseStatus)

      // Response details
      .responseStatusCode(statusCode)
      .responseStatusCodeType(statusCodeType)

      // Error details (if applicable)
      .errorMessage(errorMessage)
      .exceptionType(exceptionType)
      .exceptionStackTrace(exceptionStackTrace)

      // Additional fields
      .traceId(traceId)
      .clientIp(clientIp)
      .userAgent(userAgent)
      .contentType(contentType)
      .acceptType(acceptType)
      .requestBody(requestBody)
      .responseBody(responseBody)
      .httpVersion(httpVersion)
      .requestBodySize(requestBodySize)
      .responseBodySize(responseBodySize)
      .build();
  }

  private HttpServletRequest getHttpServletRequest() {
    try {
      ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
      return attributes.getRequest();
    } catch (IllegalStateException e) {
      return null;
    }
  }

  private Map<String, String> extractHeaders(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames())
      .stream()
      .collect(Collectors.toMap(
        h -> h,
        request::getHeader,
        (v1, v2) -> v1 // in case of duplicates, keep first
      ));
  }

  private Map<String, String> extractQueryParams(HttpServletRequest request) {
    return request.getParameterMap()
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> String.join(",", e.getValue())
      ));
  }

  /**
   * Extract user ID from request attribute (set by JWT filter)
   * Falls back to X-User-Id header if attribute is not set
   */
  private String extractUserIdFromRequest(HttpServletRequest request) {
    // First, try to get from request attribute (set by JWT filter)
    Object userIdAttr = request.getAttribute("userId");
    if (userIdAttr != null) {
      return userIdAttr.toString();
    }

    // Try alternative attribute names that might be used
    userIdAttr = request.getAttribute("user_id");
    if (userIdAttr != null) {
      return userIdAttr.toString();
    }

    userIdAttr = request.getAttribute("USER_ID");
    if (userIdAttr != null) {
      return userIdAttr.toString();
    }

    // Fallback to X-User-Id header
    String headerUserId = request.getHeader("X-User-Id");
    if (headerUserId != null && !headerUserId.isEmpty()) {
      return headerUserId;
    }

    // No user ID found
    return null;
  }

  private String getClientIp(HttpServletRequest request) {
    String clientIp = request.getHeader("X-Forwarded-For");
    if (clientIp == null || clientIp.isEmpty()) {
      clientIp = request.getHeader("X-Real-IP");
    }
    if (clientIp == null || clientIp.isEmpty()) {
      clientIp = request.getRemoteAddr();
    }
    // X-Forwarded-For can contain multiple IPs, get the first one
    if (clientIp != null && clientIp.contains(",")) {
      clientIp = clientIp.split(",")[0].trim();
    }
    return clientIp;
  }

  private String getStackTraceAsString(Throwable e) {
    return Arrays.stream(e.getStackTrace())
      .map(StackTraceElement::toString)
      .limit(10) // Limit to first 10 lines to avoid huge logs
      .collect(Collectors.joining("\n"));
  }

  private Integer calculateBodySize(Object body) {
    if (body == null) return 0;

    try {
      if (body instanceof String) {
        return ((String) body).getBytes().length;
      } else {
        String json = objectMapper.writeValueAsString(body);
        return json.getBytes().length;
      }
    } catch (Exception e) {
      return null; // Unable to calculate size
    }
  }
}
