package com.springbootBackend.backend.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootBackend.backend.dto.kafkaLogEvent.LogEvent;
import com.springbootBackend.backend.service.loggingService.LoggingService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(2) // Run after JWT filter (which is usually Order 1)
public class LoggingAspect implements Filter {

  @Autowired
  private LoggingService loggingService;

  @Value("${spring.application.name:AUTH-SERVICE}")
  private String serviceName;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {

    if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
      chain.doFilter(request, response);
      return;
    }

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // Wrap request and response to cache their content
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

    String traceId = UUID.randomUUID().toString();
    long requestTime = System.currentTimeMillis();

    try {
      // Continue the filter chain
      chain.doFilter(requestWrapper, responseWrapper);
    } finally {
      long responseTime = System.currentTimeMillis();
      long duration = responseTime - requestTime;

      try {
        // Log the request/response AFTER everything is complete (including exception handling)
        LogEvent event = buildLogEvent(
          requestWrapper,
          responseWrapper,
          traceId,
          requestTime,
          responseTime,
          duration
        );
        loggingService.publish(event);
      } catch (Exception e) {
        System.err.println("Failed to log event: " + e.getMessage());
        e.printStackTrace();
      }

      // Copy the cached response content to the actual response
      responseWrapper.copyBodyToResponse();
    }
  }

  private LogEvent buildLogEvent(
    ContentCachingRequestWrapper request,
    ContentCachingResponseWrapper response,
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
    String requestBody = getRequestBody(request);
    Integer requestBodySize = requestBody != null ? requestBody.length() : 0;

    // Response body
    String responseBody = getResponseBody(response);
    Integer responseBodySize = responseBody != null ? responseBody.length() : 0;

    // Response status - THIS WILL NOW HAVE THE CORRECT VALUE FROM GlobalExceptionHandler!
    int statusCode = response.getStatus();
    String statusCodeType = getStatusCodeType(statusCode);
    String apiResponseStatus = statusCode >= 200 && statusCode < 300 ? "SUCCESS" : "FAILURE";

    // For error responses, try to extract error message from response body
    String errorMessage = null;
    String exceptionType = null;
    if (statusCode >= 400 && responseBody != null) {
      try {
        // Try to parse CustomErrorMsgFormat from response
        Map<String, Object> errorResponse = objectMapper.readValue(responseBody, Map.class);
        errorMessage = (String) errorResponse.get("message");
        exceptionType = (String) errorResponse.get("error");
      } catch (Exception e) {
        // Response is not JSON or not in expected format
        errorMessage = "Error occurred";
      }
    }

    // Build full URL
    String fullUrl = request.getRequestURL().toString();
    if (request.getQueryString() != null) {
      fullUrl += "?" + request.getQueryString();
    }

    return LogEvent.builder()
      // Required fields
      .serviceName("AUTH_LOG")
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
      .exceptionStackTrace(null) // Not available at this level

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

  private Map<String, String> extractHeaders(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames())
      .stream()
      .collect(Collectors.toMap(
        h -> h,
        request::getHeader,
        (v1, v2) -> v1
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

  private String extractUserIdFromRequest(HttpServletRequest request) {
    Object userIdAttr = request.getAttribute("userId");
    if (userIdAttr != null) {
      return userIdAttr.toString();
    }

    String headerUserId = request.getHeader("X-User-Id");
    if (headerUserId != null && !headerUserId.isEmpty()) {
      return headerUserId;
    }

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
    if (clientIp != null && clientIp.contains(",")) {
      clientIp = clientIp.split(",")[0].trim();
    }
    return clientIp;
  }

  private String getRequestBody(ContentCachingRequestWrapper request) {
    try {
      byte[] buf = request.getContentAsByteArray();
      if (buf.length > 0) {
        return new String(buf, request.getCharacterEncoding());
      }
    } catch (Exception e) {
      // Ignore
    }
    return null;
  }

  private String getResponseBody(ContentCachingResponseWrapper response) {
    try {
      byte[] buf = response.getContentAsByteArray();
      if (buf.length > 0) {
        return new String(buf, response.getCharacterEncoding());
      }
    } catch (Exception e) {
      // Ignore
    }
    return null;
  }

  private String getStatusCodeType(int statusCode) {
    try {
      return HttpStatus.valueOf(statusCode).getReasonPhrase();
    } catch (Exception e) {
      return "Unknown";
    }
  }
}
