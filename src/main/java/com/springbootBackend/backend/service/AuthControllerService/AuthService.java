package com.springbootBackend.backend.service.AuthControllerService;

import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;

public interface AuthService {

    public MobileSignUpResponseDto mobileSignupGetOtp(String phoneNumber, String userName, String password);
}
