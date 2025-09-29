package com.springbootBackend.backend.controller;


import com.springbootBackend.backend.constants.ApiConstants;
import com.springbootBackend.backend.dto.userChangePasswordDto.UserChangePasswordRequestDto;
import com.springbootBackend.backend.dto.userChangePasswordDto.UserChangePasswordResponseDto;
import com.springbootBackend.backend.service.UserService.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.USER_BASE)
public class UserController {

  @Autowired
  UserServiceImpl userService;

  @PostMapping(ApiConstants.USER_CHANGE_PASSWORD)
  public ResponseEntity<UserChangePasswordResponseDto> userChangePassword(@Valid @RequestBody UserChangePasswordRequestDto requestDto){
    UserChangePasswordResponseDto response =  userService.changeUserPassword(requestDto.getOldPassword(), requestDto.getNewPassword());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
