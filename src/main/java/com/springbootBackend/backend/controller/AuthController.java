package com.springbootBackend.backend.controller;

import com.springbootBackend.backend.constants.ApiConstants;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE)
public class AuthController {

    @PostMapping(ApiConstants.MOBILE_SIGNUP)
    public String signup(){
         return "hello";
    }
}
