package com.springbootBackend.backend.service.AuthControllerService;

public class AuthServiceHelper {

    public static boolean isPhoneNumber(String param) {
    return param != null && param.matches("^[0-9]{10}$");
  }

  public static boolean isEmail(String param) {
    return param != null && param.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
  }

}
