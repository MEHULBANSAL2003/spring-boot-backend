package com.springbootBackend.backend.service.UserService;

import com.springbootBackend.backend.Utils.SecurityUtil;
import com.springbootBackend.backend.dto.userChangePasswordDto.UserChangePasswordResponseDto;
import com.springbootBackend.backend.entity.UserDataEntity;
import com.springbootBackend.backend.exceptions.customExceptions.IdentifierNotFound;
import com.springbootBackend.backend.exceptions.customExceptions.IncorrectPassword;
import com.springbootBackend.backend.repository.UserDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  UserDataRepository userDataRepository;

  @Override
  @Transactional
  public UserChangePasswordResponseDto changeUserPassword(String oldPassword, String newPassword) {

    Long currentUserId = SecurityUtil.getCurrentUserId();

    if(currentUserId == null){
      throw new RuntimeException("user not authenticated");
    }
    UserDataEntity user = userDataRepository.findByUserId(currentUserId).orElseThrow(() -> new IdentifierNotFound("user not registered"));

    if(!passwordEncoder.matches(oldPassword, user.getHashedPassword())){
      throw new IncorrectPassword("old password is incorrect");
    }

    if(oldPassword.equals(newPassword)){
      throw new IncorrectPassword("New password cannot be same as that of old one");
    }

    String hashedPassword = passwordEncoder.encode(newPassword);
    user.setHashedPassword(hashedPassword);
    user.setNoOfTimePasswordChanged(user.getNoOfTimePasswordChanged()+1);
    userDataRepository.save(user);

    UserChangePasswordResponseDto response = new UserChangePasswordResponseDto();
    response.setStatus("success");
    response.setMessage("password changed successfully");
    return response;
  }


}



