package com.springbootBackend.backend.service.UserService;

import com.springbootBackend.backend.dto.userChangePasswordDto.UserChangePasswordResponseDto;

public interface UserService {

  public UserChangePasswordResponseDto changeUserPassword(String oldPassword, String newPassword);

}
