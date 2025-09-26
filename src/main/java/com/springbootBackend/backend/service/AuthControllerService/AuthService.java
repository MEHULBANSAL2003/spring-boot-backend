package com.springbootBackend.backend.service.AuthControllerService;

import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordResponseDto;
import com.springbootBackend.backend.dto.getNewTokenFromRefreshTokenDto.NewAccessTokenFromRefreshTokenResponseDto;
import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordResponseDto;
import com.springbootBackend.backend.dto.userEmailSignUpDto.EmailSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationResponseDto;
import jakarta.servlet.http.HttpServletRequest;


public interface AuthService {

    public MobileSignUpResponseDto mobileSignupGetOtp(String phoneNumber, String userName, String password);

    public UserMobileSignupVerificationResponseDto mobileSignUpVerifyOtp(String userName, String otp);

    public EmailSignUpResponseDto emailSignupGetOtp(String email, String userName, String password);

    public LoginByUserNamePasswordResponseDto loginUserByCredentials(String identifier, String password, HttpServletRequest request);

    public NewAccessTokenFromRefreshTokenResponseDto generateAccessTokenFromRefreshToken(String refreshToken);

    public ResetPasswordResponseDto  resetUserPassword(String parameter, String sendOtpTo);
}
