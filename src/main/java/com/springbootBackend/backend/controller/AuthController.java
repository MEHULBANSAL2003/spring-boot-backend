package com.springbootBackend.backend.controller;

import com.springbootBackend.backend.dto.UserAuthRequestDto;
import jakarta.persistence.Entity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/signup")
    public String signup(@RequestBody UserAuthRequestDto userRequest){
        System.out.println(userRequest);
         return "signup successfull";
    }
}
