package com.springbootBackend.backend.controller;

import com.springbootBackend.backend.constants.ApiConstants;
import com.springbootBackend.backend.dto.UserAuthRequestDto;
import jakarta.persistence.Entity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE)
public class AuthController {

    @PostMapping(ApiConstants.SIGNUP)
    public String signup(@RequestBody UserAuthRequestDto userRequest){
        System.out.println(userRequest.getEmail());
         return userRequest.getEmail();
    }
}
