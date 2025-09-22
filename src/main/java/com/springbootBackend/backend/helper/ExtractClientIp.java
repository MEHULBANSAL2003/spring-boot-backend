package com.springbootBackend.backend.helper;

import jakarta.servlet.http.HttpServletRequest;

public class ExtractClientIp {

  public static String extractClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr(); // fallback
    } else {
      ip = ip.split(",")[0]; // handle multiple IPs
    }
    return ip;
  }

}
