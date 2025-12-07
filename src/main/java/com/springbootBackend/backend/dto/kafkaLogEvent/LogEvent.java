package com.springbootBackend.backend.dto.kafkaLogEvent;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class LogEvent {
  // Required fields
  private String serviceName;
  private String fullUrl;
  private String apiEndpoint;
  private String requestMethod;
  private Map<String, String> requestHeaders;
  private Map<String, String> requestQueryParams;
  private long requestTime; // epoch milliseconds
  private long responseTime; // epoch milliseconds
  private String requestUserId; // extracted from JWT or null
  private long apiResponseTime; // duration in milliseconds
  private String apiResponseTimeUnits; // e.g., "ms"
  private String apiResponseStatus; // "SUCCESS" or "FAILURE"

  // Success-specific fields
  private Integer responseStatusCode;
  private String responseStatusCodeType; // "OK", "CREATED", etc.

  // Error-specific fields (also includes responseStatusCode)
  private String errorMessage;
  private String exceptionType; // class name of exception
  private String exceptionStackTrace;

  // Additional important fields
  private String traceId; // for distributed tracing
  private String clientIp;
  private String userAgent;
  private String contentType;
  private String acceptType;
  private Object requestBody; // optional: may want to exclude for sensitive data
  private Object responseBody; // optional: may want to exclude for large responses
  private String httpVersion;
  private Integer requestBodySize; // in bytes
  private Integer responseBodySize; // in bytes
}
