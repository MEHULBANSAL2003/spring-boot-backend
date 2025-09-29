package com.springbootBackend.backend.controller;

import com.springbootBackend.backend.constants.ApiConstants;
import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordFinalRequestDto;
import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordFinalResponseDto;
import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordOtpVerifyRequestDto;
import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordOtpVerifyResponseDto;
import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordRequestDto;
import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordResponseDto;
import com.springbootBackend.backend.dto.getNewTokenFromRefreshTokenDto.NewAccessTokenFromRefreshTokenResponseDto;
import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordRequestDto;
import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordResponseDto;
import com.springbootBackend.backend.dto.userEmailSignUpDto.EmailSignUpRequestDto;
import com.springbootBackend.backend.dto.userEmailSignUpDto.EmailSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpRequestDto;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationRequestDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationResponseDto;
import com.springbootBackend.backend.exceptions.customExceptions.InvalidRefreshToken;
import com.springbootBackend.backend.service.AuthControllerService.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
    public ResponseEntity<LoginByUserNamePasswordResponseDto> loginUserByCredentials(@Valid @RequestBody LoginByUserNamePasswordRequestDto requestDto, HttpServletRequest request){
        LoginByUserNamePasswordResponseDto response = authService.loginUserByCredentials(requestDto.getIdentifier(), requestDto.getPassword(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(ApiConstants.GENERATE_NEW_ACCESS_TOKEN)
  public ResponseEntity<NewAccessTokenFromRefreshTokenResponseDto> generateNewAccessTokenFromRefreshToken(@RequestBody Map<String, String> requestBody){

      String refreshToken = requestBody.get("refreshToken");
      if (refreshToken == null || refreshToken.isEmpty()) {
        NewAccessTokenFromRefreshTokenResponseDto FailedResponse = new NewAccessTokenFromRefreshTokenResponseDto("FAIL","token cannot be empty");
        return new ResponseEntity<>(FailedResponse, HttpStatus.BAD_REQUEST);
      }

      NewAccessTokenFromRefreshTokenResponseDto response  = authService.generateAccessTokenFromRefreshToken(refreshToken);


      return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @PostMapping(ApiConstants.USER_RESET_PASSWORD_CRED_VERIFY)
  public ResponseEntity<ResetPasswordResponseDto> resetUserPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto){

           if(requestDto.getEmail()==null && requestDto.getPhoneNumber()==null && requestDto.getUserName()==null){
                throw new InvalidRefreshToken("Please provide any one parameter");
           }
           String parameter = "";
           String sendOtpTo = "";
           if(requestDto.getUserName()!=null){
             parameter = requestDto.getUserName();
           }
           else if(requestDto.getEmail()!=null){
             parameter  =requestDto.getEmail();
             sendOtpTo = "email";
           }
           else{
             parameter = requestDto.getPhoneNumber();
             sendOtpTo = "phone";
           }

       ResetPasswordResponseDto response = authService.resetUserPassword(parameter, sendOtpTo);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(ApiConstants.USER_RESET_PASSWORD_OTP_VERIFY)
      public ResponseEntity<ResetPasswordOtpVerifyResponseDto> resetUserPasswordOtpVerify(@Valid @RequestBody ResetPasswordOtpVerifyRequestDto requestDto){
      ResetPasswordOtpVerifyResponseDto response = authService.resetUserPasswordOtpVerify(requestDto.getIdentifier(), requestDto.getOtp());
      return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(ApiConstants.USER_RESET_PASSWORD)
  public ResponseEntity<ResetPasswordFinalResponseDto> resetUserPassword(@Valid @RequestBody ResetPasswordFinalRequestDto requestDto){

      ResetPasswordFinalResponseDto response = authService.resetUserPasswordFinal(requestDto.getIdentifier(), requestDto.getNewPassword());

      return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
