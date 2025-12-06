package com.springbootBackend.backend.dto.kafkaLogEvent;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LogEvent {
  private String serviceName;
  private String endpoint;
  private String fullUrl;
  private String requestMethod;
  private Map<String, String> queryParams;
  private Map<String, String> headers;
  private Object requestBody;
  private Object responseBody;
  private int statusCode;
  private String error;
  private String exceptionStackTrace;
  private String message;
  private long requestTime;
  private long responseTime;
  private long duration;
  private String traceId;
  private String requestUserId;
  private String clientIp;
  private String userAgent;
  private String sessionId;
  private String contentType;
  private String acceptType;
  private boolean isErrorHandled;
}
