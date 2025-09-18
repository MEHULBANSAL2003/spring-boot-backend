package com.springbootBackend.backend.controller;

import com.springbootBackend.backend.constants.ApiConstants;
import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordRequestDto;
import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordResponseDto;
import com.springbootBackend.backend.dto.userEmailMobileSignUpVerificationDto.UserEmailSignupVerificationRequestDto;
import com.springbootBackend.backend.dto.userEmailMobileSignUpVerificationDto.UserEmailSignupVerificationResponseDto;
import com.springbootBackend.backend.dto.userEmailSignUpDto.EmailSignUpRequestDto;
import com.springbootBackend.backend.dto.userEmailSignUpDto.EmailSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpRequestDto;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationRequestDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationResponseDto;
import com.springbootBackend.backend.service.AuthControllerService.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE)
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping(ApiConstants.MOBILE_SIGNUP)
    public ResponseEntity<MobileSignUpResponseDto> userMobileSignupGetOtp(@Valid @RequestBody MobileSignUpRequestDto requestDto){
        MobileSignUpResponseDto response = authService.mobileSignupGetOtp(requestDto.getPhoneNumber(), requestDto.getUserName(), requestDto.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(ApiConstants.MOBILE_SIGNUP_OTP_VERIFY)
    public  ResponseEntity<UserMobileSignupVerificationResponseDto> userMobileSignupVerifyOtp(@Valid @RequestBody UserMobileSignupVerificationRequestDto requestDto){
        UserMobileSignupVerificationResponseDto response = authService.mobileSignUpVerifyOtp(requestDto.getUserName(),requestDto.getOtp());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(ApiConstants.EMAIL_SIGNUP)
    public ResponseEntity<EmailSignUpResponseDto> userEmailSignupGetOtp(@Valid @RequestBody EmailSignUpRequestDto requestDto){
        EmailSignUpResponseDto response = authService.emailSignupGetOtp(requestDto.getEmail(), requestDto.getUserName(), requestDto.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(ApiConstants.EMAIL_SIGNUP_OTP_VERIFY)
    public  ResponseEntity<UserMobileSignupVerificationResponseDto> userEmailSignupVerifyOtp(@Valid @RequestBody UserMobileSignupVerificationRequestDto requestDto){
        UserMobileSignupVerificationResponseDto response = authService.mobileSignUpVerifyOtp(requestDto.getUserName(),requestDto.getOtp());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(ApiConstants.USERNAME_LOGIN)
    public ResponseEntity<LoginByUserNamePasswordResponseDto> loginByUserNameAndPassword(@Valid @RequestBody LoginByUserNamePasswordRequestDto requestDto){
        LoginByUserNamePasswordResponseDto response = authService.loginByUsernameAndPassword(requestDto.getUserName(), requestDto.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
