package com.springbootBackend.backend.constants;

public class ApiConstants {

    public static final String API_BASE = "/api";
    public static final String AUTH_BASE = API_BASE + "/auth";
    public static final String USER_BASE = API_BASE + "/profile";


    public static final String EMAIL_SIGNUP ="/user/signup/email/get-otp";
    public static final String MOBILE_SIGNUP ="/user/signup/mobile/get-otp";
    public static final String MOBILE_SIGNUP_OTP_VERIFY = "/user/signup/mobile/otp/verify";
    public static final String EMAIL_SIGNUP_OTP_VERIFY = "/user/signup/email/otp/verify";
    public static final String LOGIN_BY_IDENTIFIER = "/user/login/by/identifier";
    public static final String GENERATE_NEW_ACCESS_TOKEN = "/user/get/new/access/token";
    public static final String USER_RESET_PASSWORD_CRED_VERIFY = "/user/reset/password/credentials/verify";
    public static final String USER_RESET_PASSWORD_OTP_VERIFY = "/user/reset/password/otp/verify";
    public static final String USER_RESET_PASSWORD = "/user/reset/password";

    public static final String USER_CHANGE_PASSWORD = "/change/password";
}
