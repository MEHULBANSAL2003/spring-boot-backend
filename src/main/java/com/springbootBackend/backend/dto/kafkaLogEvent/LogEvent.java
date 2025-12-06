package com.springbootBackend.backend.dto.kafkaLogEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogEvent {

  private String serviceName;
  private String endpoint;
  private String method;
  private Object request;
  private Object response;
  private long timestamp;
  private int statusCode;
  private String traceId;
}
