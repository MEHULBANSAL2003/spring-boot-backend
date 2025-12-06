package com.springbootBackend.backend.service.loggingService;


import com.springbootBackend.backend.dto.kafkaLogEvent.LogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

  @Value("${app.logs.topic}")
  private String topic;

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  public void publish(LogEvent event) {
    kafkaTemplate.send(topic, event.getTraceId(), event);
  }
}
