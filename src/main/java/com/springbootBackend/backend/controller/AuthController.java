package com.springbootBackend.backend.controller;

import com.springbootBackend.backend.constants.ApiConstants;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpRequestDto;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
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
}
