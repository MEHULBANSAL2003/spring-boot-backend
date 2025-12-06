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

import java.util.UUID;

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

    long start = System.currentTimeMillis();

    try {
      response = joinPoint.proceed();
    } catch (Exception e) {
      status = 500;
      throw e;
    } finally {
      LogEvent event = LogEvent.builder()
        .serviceName("AUTH-SERVICE")
        .endpoint(request.getRequestURI())
        .method(request.getMethod())
        .request(joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : null)
        .response(response)
        .statusCode(status)
        .traceId(traceId)
        .timestamp(start)
        .build();

      loggingService.publish(event);
    }

    return response;
  }
}

