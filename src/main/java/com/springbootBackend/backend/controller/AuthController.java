package com.springbootBackend.backend.controller;

import com.springbootBackend.backend.constants.ApiConstants;
import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordRequestDto;
import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordResponseDto;
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

    @PostMapping(ApiConstants.LOGIN_BY_IDENTIFIER)
    public ResponseEntity<LoginByUserNamePasswordResponseDto> loginUserByCredentials(@Valid @RequestBody LoginByUserNamePasswordRequestDto requestDto){
        LoginByUserNamePasswordResponseDto response = authService.loginUserByCredentials(requestDto.getIdentifier(), requestDto.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/user")
  public String Hello(){
      return "hellop";
    }

//    @PostMapping("/create/bulk/users")
//    public void createBulkUsers() {
//
//        List<UserDataEntity> users = new ArrayList<>();
//
//        for(int i = 101; i <= 1000; i++) {
//            UserDataEntity user = new UserDataEntity();
//
//            // Basic details
//            user.setUserName("user" + i);
//            user.setEmail("user" + i + "@example.com");
//            user.setHashedEmail(passwordEncoder.encode(user.getEmail())); // optional hashed email
//
//            // Hash password
//            String rawPassword = "Password@" + i; // unique password for demo
//            user.setHashedPassword(passwordEncoder.encode(rawPassword));
//
//            // Optional fields
//            user.setAge(20 + (i % 30)); // random-ish age
//            user.setDateOfBirth(LocalDate.now().minusYears(user.getAge()));
//            user.setProfilePicUrl("https://example.com/profile/" + i + ".png");
//            user.setCountryCode("+91");
//            user.setPhoneNumber("9000000" + String.format("%03d", i));
//
//            // Status
//            user.setCurrStatus(UserDataEntity.userStatus.ACTIVE);
//            user.setIncorrectAttempts(0);
//            user.setBlockedCount(0);
//
//            users.add(user);
//        }
//
//        // Save all at once for batch insert
//        userDataRepository.saveAll(users);
//        System.out.println("500 users inserted successfully!");
//    }


}
