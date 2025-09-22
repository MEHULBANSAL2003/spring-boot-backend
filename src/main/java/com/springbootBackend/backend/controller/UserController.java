package com.springbootBackend.backend.controller;


import com.springbootBackend.backend.constants.ApiConstants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.API_BASE)
public class UserController {

  @PostMapping("/user")
  public String Hello(){
    return "hello";
  }
}
