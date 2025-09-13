package com.springbootBackend.backend.service.AuthControllerService;

import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationResponseDto;


public interface AuthService {

    public MobileSignUpResponseDto mobileSignupGetOtp(String phoneNumber, String userName, String password);

    public UserMobileSignupVerificationResponseDto mobileSignUpVerifyOtp(String userName, String otp);
}
