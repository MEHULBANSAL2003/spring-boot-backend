package com.springbootBackend.backend.controller;

import com.springbootBackend.backend.constants.ApiConstants;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpRequestDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE)
public class AuthController {

    @PostMapping(ApiConstants.MOBILE_SIGNUP)
    public String signup(@Valid @RequestBody MobileSignUpRequestDto requestDto){
         return "hello";
    }
}
