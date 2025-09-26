package com.springbootBackend.backend.service.AuthControllerService;

import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordFinalResponseDto;
import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordOtpVerifyResponseDto;
import com.springbootBackend.backend.dto.ResetPassword.ResetPasswordResponseDto;
import com.springbootBackend.backend.dto.getNewTokenFromRefreshTokenDto.NewAccessTokenFromRefreshTokenResponseDto;
import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordResponseDto;
import com.springbootBackend.backend.dto.userEmailSignUpDto.EmailSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationResponseDto;
import com.springbootBackend.backend.entity.RefreshToken;
import com.springbootBackend.backend.entity.ResetPassword;
import com.springbootBackend.backend.entity.UserDataEntity;
import com.springbootBackend.backend.entity.UserPendingVerification;
import com.springbootBackend.backend.exceptions.customExceptions.*;
import com.springbootBackend.backend.helper.ExtractClientIp;
import com.springbootBackend.backend.helper.OtpGenerator;
import com.springbootBackend.backend.repository.RefreshTokenRepository;
import com.springbootBackend.backend.repository.ResetPasswordRepository;
import com.springbootBackend.backend.repository.UserDataRepository;
import com.springbootBackend.backend.repository.UserPendingVerificationRepository;
import com.springbootBackend.backend.service.JwtService.JwtService;
import com.springbootBackend.backend.service.emailService.EmailService;
import com.springbootBackend.backend.service.smsService.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    UserPendingVerificationRepository userPendingVerificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    JwtService jwtService;

    @Autowired
  RefreshTokenRepository refreshTokenRepository;

    @Autowired
  ResetPasswordRepository resetPasswordRepository;

  @Value("${jwt.refresh-token-exp-ms}")
  private long refreshTokenExpiration;


    @Override
    @Transactional
    public MobileSignUpResponseDto mobileSignupGetOtp(String phoneNumber, String userName, String password) {

        UserDataEntity existing = userDataRepository.findByUserNameOrPhoneNumber(userName,phoneNumber).orElse(null);

        if(existing!=null){
            if(existing.getPhoneNumber().equals(phoneNumber)){
                throw new PhoneNumberAlreadyExistsException("Phone number already registered: "+ phoneNumber);
            }
            if(existing.getUserName().equals(userName)){
                throw new UserNameExistsException("Username already taken: "+ userName);
            }
        }

        int otp = new OtpGenerator().generateOtp();
        String hashedPassword = passwordEncoder.encode(password);
        String hashedOtp = passwordEncoder.encode(String.valueOf(otp));

        UserPendingVerification pendingUser = userPendingVerificationRepository
                .findByUserName(userName)
                .orElse(new UserPendingVerification());

        LocalDateTime now = LocalDateTime.now();
        if (pendingUser.getOtpRequestWindowStart() != null && pendingUser.getOtpRequestWindowStart().isAfter(now.minusMinutes(10))) {

            if (pendingUser.getOtpRequestCount() >= 5) {
                throw new IncorrectOtpLimitReachException("You have exceeded the maximum number of OTP attempts. Please try again later.");
            }
            pendingUser.setOtpRequestCount(pendingUser.getOtpRequestCount() + 1);
        } else {
            pendingUser.setOtpRequestWindowStart(now);
            pendingUser.setOtpRequestCount(1);
        }
        pendingUser.setEmail(null);
        pendingUser.setPhoneNumber(phoneNumber);
        pendingUser.setCountryCode("+91");
        pendingUser.setPassword(hashedPassword);
        pendingUser.setUserName(userName);
        pendingUser.setOtp(hashedOtp);
        pendingUser.setIncorrectAttempts(0);
        pendingUser.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));
        pendingUser.setIsTwilioActive(true);

        String number = "+91"+phoneNumber;
        boolean smsSent = smsService.sendSms(number,String.valueOf(otp));
        if(!smsSent){
            throw new IncorrectOtpException("Error sending sms. Please try again later");
        }

        userPendingVerificationRepository.save(pendingUser);

        MobileSignUpResponseDto response = new MobileSignUpResponseDto("success", 6, true,"Otp sent successfully", 300,true);
        return response;
    }


    @Override
    @Transactional(dontRollbackOn = IncorrectOtpException.class)
    public UserMobileSignupVerificationResponseDto mobileSignUpVerifyOtp(String userName, String otp) {
        UserDataEntity verifiedUser = userDataRepository.findByUserName(userName).orElse(null);

        if(verifiedUser!=null){
            throw new UserNameExistsException("Username already taken: "+ userName);
        }
        UserPendingVerification pendingUser = userPendingVerificationRepository.findByUserName(userName).orElse(null);

        if(pendingUser==null){
            throw new UserNameExistsException("No such username exists"+ userName);
        }
        if(pendingUser!=null && pendingUser.getOtpExpiryTime().isBefore(LocalDateTime.now())){
          throw new OtpExpiresException("Otp expired!! Resend otp to continue!!");
        }
        if(pendingUser.getIncorrectAttempts()>5){
            throw new IncorrectOtpLimitReachException("You have exceeded the maximum number of OTP attempts. Please try again later.");
        }

        if(pendingUser!=null &&  !passwordEncoder.matches(otp,pendingUser.getOtp())){
            pendingUser.setIncorrectAttempts(pendingUser.getIncorrectAttempts() + 1);
            userPendingVerificationRepository.save(pendingUser);
            throw new IncorrectOtpException("Incorrect otp.Please enter the correct otp");
        }

        UserDataEntity newUser = new UserDataEntity();

        newUser.setUserName(pendingUser.getUserName());
        newUser.setPhoneNumber(pendingUser.getPhoneNumber());
        newUser.setCountryCode(pendingUser.getCountryCode());
        newUser.setHashedPassword(pendingUser.getPassword()); // ideally already hashed
        newUser.setEmail(pendingUser.getEmail());
        newUser.setCurrStatus(UserDataEntity.userStatus.ACTIVE);
        newUser.setLastLogin(LocalDateTime.now());
        newUser.setHashedEmail(pendingUser.getEmail());
        newUser.setCountryCode("+91");

        UserDataEntity savedUser = userDataRepository.save(newUser);
        userPendingVerificationRepository.delete(pendingUser);

        return new UserMobileSignupVerificationResponseDto("success",savedUser.getUserId(),savedUser.getEmail(),savedUser.getUserName(),savedUser.getCountryCode(),savedUser.getPhoneNumber(), savedUser.getCreatedAt(),savedUser.getUpdatedAt());

    }

    @Override
    @Transactional
    public EmailSignUpResponseDto emailSignupGetOtp(String email, String userName, String password) {
        UserDataEntity existing = userDataRepository.findByUserNameOrEmail(userName, email).orElse(null);

        if(existing!=null){
            if(existing.getEmail().equals(email)){
                throw new PhoneNumberAlreadyExistsException("Email already registered");
            }
            if(existing.getUserName().equals(userName)){
                throw new UserNameExistsException("Username already taken");
            }
        }

        int otp = new OtpGenerator().generateOtp();
        String hashedPassword = passwordEncoder.encode(password);
        String hashedOtp = passwordEncoder.encode(String.valueOf(otp));

        UserPendingVerification pendingUser = userPendingVerificationRepository
                .findByUserName(userName)
                .orElse(new UserPendingVerification());
        LocalDateTime now = LocalDateTime.now();
        if (pendingUser.getOtpRequestWindowStart() != null && pendingUser.getOtpRequestWindowStart().isAfter(now.minusMinutes(10))) {

            if (pendingUser.getOtpRequestCount() >= 5) {
                throw new IncorrectOtpLimitReachException("You have exceeded the maximum number of OTP attempts. Please try again later.");
            }
            pendingUser.setOtpRequestCount(pendingUser.getOtpRequestCount() + 1);
        } else {
            pendingUser.setOtpRequestWindowStart(now);
            pendingUser.setOtpRequestCount(1);
        }

        pendingUser.setEmail(email);
        pendingUser.setPassword(hashedPassword);
        pendingUser.setUserName(userName);
        pendingUser.setOtp(hashedOtp);
        pendingUser.setIncorrectAttempts(0);
        pendingUser.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));
        pendingUser.setIsTwilioActive(false);
        pendingUser.setPhoneNumber(null);

        try {
            emailService.sendOtpEmail(email, String.valueOf(otp));
        } catch (Exception e) {
            throw new IncorrectOtpException("Error sending sms. Please try again later");
        }

        userPendingVerificationRepository.save(pendingUser);

        EmailSignUpResponseDto response = new EmailSignUpResponseDto("success", 6, true,"Otp sent successfully", 300,true);
        return response;
    }

    @Override
    @Transactional(dontRollbackOn = {UserBlockedException.class, IncorrectPassword.class})
    public LoginByUserNamePasswordResponseDto loginUserByCredentials(String identifier, String password, HttpServletRequest request){

        UserDataEntity user = userDataRepository.findByIdentifier(identifier)
                                                .orElseThrow(() -> new IdentifierNotFound("Invalid credentials.Please provide correct credentials"));

        if(user.getCurrStatus() == UserDataEntity.userStatus.BLOCKED){
            throw new UserBlockedException("Your account has been disabled due to unusual activities.Please verify yourself for access your account.");
        }
        if(user.getCurrStatus()== UserDataEntity.userStatus.TEMP_BLOCKED && user.getBlockedEndTime().isAfter(LocalDateTime.now())){
            long timeLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(),user.getBlockedEndTime());
            throw new UserBlockedException(
                    "Your account is temporarily blocked due to repeated incorrect attempts. " +
                            "Please try again after " + timeLeft + " minute" + (timeLeft > 1 ? "s" : "") + "."
            );
        }

       boolean isPasswordCorrect =  passwordEncoder.matches(password, user.getHashedPassword());

        if(!isPasswordCorrect){
            if(user.getIncorrectAttempts()>=5 && user.getIncorrectAttemptTimeWindowStart().plusMinutes(10).isAfter(LocalDateTime.now())){
                user.setBlockedCount(user.getBlockedCount()+1);
                String message = "";
                if(user.getBlockedCount()>=5){
                    user.setCurrStatus(UserDataEntity.userStatus.BLOCKED);
                    message = "Your account has been disabled due to unusual activities.Please verify yourself for access your account.";
                }
                else{
                    user.setCurrStatus(UserDataEntity.userStatus.TEMP_BLOCKED);
                    user.setBlockedStartTime(LocalDateTime.now());
                    user.setBlockedEndTime(user.getBlockedStartTime().plusMinutes(10));
                    user.setBlockForMin(ChronoUnit.MINUTES.between(user.getBlockedStartTime(),user.getBlockedEndTime()));
                    message = "Your account is temporarily blocked due to repeated incorrect attempts.";
                }
                userDataRepository.save(user);
                throw new UserBlockedException(message);
            }
            else{
                if(user.getIncorrectAttempts()==5){
                    user.setIncorrectAttempts(1);
                }else {
                    user.setIncorrectAttempts(user.getIncorrectAttempts() + 1);
                }
                if(user.getIncorrectAttempts()== 1){
                    user.setIncorrectAttemptTimeWindowStart(LocalDateTime.now());
                }
                userDataRepository.save(user);
                throw new IncorrectPassword("Invalid credentials.!Please enter correct credentials");
            }
        }
        String access_token =  jwtService.generateAccessToken(user.getUserId());
        String refresh_token = jwtService.generateRefreshToken(user.getUserId());

      RefreshToken refreshToken = new RefreshToken(user,refresh_token, Instant.now(), Instant.now().plus(7,ChronoUnit.DAYS));

           user.setCurrStatus(UserDataEntity.userStatus.ACTIVE);
           user.setIncorrectAttempts(0);
           user.setIncorrectAttemptTimeWindowStart(null);
           user.setBlockForMin(0);
           user.setBlockedStartTime(null);
           user.setBlockedEndTime(null);
           user.setLastLogin(LocalDateTime.now());
           user.setBlockedCount(0);
           user.setIpAddress(ExtractClientIp.extractClientIp(request));
           user.setUserAgent(request.getHeader("User-Agent"));

          userDataRepository.save(user);
         refreshTokenRepository.save(refreshToken);
           LoginByUserNamePasswordResponseDto response = new LoginByUserNamePasswordResponseDto(
                "SUCCESS",
                user.getUserId(),
                user.getEmail(),
                user.getUserName(),
                user.getCountryCode(),
                user.getPhoneNumber(),
                user.getAge(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getProfilePicUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                access_token,
               refresh_token
        );

        return response;

    }


  @Override
  @Transactional
  public NewAccessTokenFromRefreshTokenResponseDto generateAccessTokenFromRefreshToken(String oldRefreshToken){

    if (!jwtService.validateToken(oldRefreshToken)) {
      throw new InvalidRefreshToken("Invalid or expired refresh token");
    }

    String tokenType = jwtService.getTokenType(oldRefreshToken);
    if (!"REFRESH".equals(tokenType)) {
      throw new InvalidRefreshToken("Invalid token type");
    }

    RefreshToken dbToken = refreshTokenRepository.findByRefreshToken(oldRefreshToken)
      .orElseThrow(() -> new InvalidRefreshToken("Refresh token not found"));

    if (dbToken.getExpiresAt().isBefore(Instant.now())) {
      throw new InvalidRefreshToken("Refresh token expired. Please login again");
    }

    if (dbToken.isRevoked()) {
      throw new InvalidRefreshToken("This refresh token has been revoked. Please login again.");
    }

    UserDataEntity user = dbToken.getUser();

    refreshTokenRepository.delete(dbToken);

    String newAccessToken = jwtService.generateAccessToken(user.getUserId());
    String newRefreshToken = jwtService.generateRefreshToken(user.getUserId());

    RefreshToken newDbToken = new RefreshToken(
      user,
      newRefreshToken,
      Instant.now(),
      Instant.now().plus(7, ChronoUnit.DAYS)
    );
    refreshTokenRepository.save(newDbToken);


      NewAccessTokenFromRefreshTokenResponseDto response = new NewAccessTokenFromRefreshTokenResponseDto("SUCCESS", "tokens generated successfully", newAccessToken,newRefreshToken);

      return response;
  }


  @Override
  @Transactional
  public ResetPasswordResponseDto resetUserPassword(String parameter, String sendOtpTo){

      UserDataEntity user = userDataRepository.findByIdentifier(parameter).orElseThrow(() -> new IdentifierNotFound("Credentials not registered.Please provide correct credentials"));

       ResetPasswordResponseDto response = new ResetPasswordResponseDto();
       int otp = new OtpGenerator().generateOtp();

    ResetPassword pendingUser = resetPasswordRepository.findByUserName(user.getUserName()).orElse(new ResetPassword());

      if(sendOtpTo.equals("email") || (sendOtpTo.isEmpty() && user.getEmail()!=null)){
        try {
          emailService.sendResetPasswordOtp(parameter, String.valueOf(otp));
        } catch (Exception e) {
          throw new IncorrectOtpException("Error sending sms. Please try again later");
        }
        pendingUser.setEmail(user.getEmail());
        pendingUser.setOtp(String.valueOf(otp));
        pendingUser.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));
        pendingUser.setUserName(user.getUserName());
        response.setStatus("success");
        response.setMessage("Otp has been sent to your registered email");
        resetPasswordRepository.save(pendingUser);
      }
      else {
        String number = "+91"+user.getPhoneNumber();
        boolean smsSent = smsService.sendResetPassworSms(number,String.valueOf(otp));
        if(!smsSent){
          throw new IncorrectOtpException("Error sending sms. Please try again later");
        }
        pendingUser.setPhoneNumber(user.getPhoneNumber());
        pendingUser.setOtp(String.valueOf(otp));
        pendingUser.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));
        pendingUser.setUserName(user.getUserName());
        response.setStatus("success");
        response.setMessage("Otp has been sent to your registered phoneNumber");
        resetPasswordRepository.save(pendingUser);
      }

      return response;
  }

  @Override
  @Transactional
  public ResetPasswordOtpVerifyResponseDto resetUserPasswordOtpVerify(String identifier,String otp){
      ResetPassword resetPasswordEntry = resetPasswordRepository.findByIdentifier(identifier).orElseThrow(() -> new IdentifierNotFound("invalid request!!"));

      if(resetPasswordEntry.getOtpExpiryTime().isBefore(LocalDateTime.now())){
        throw new OtpExpiresException("OTP has been expired!!.");
      }
      if(!resetPasswordEntry.getOtp().equals(otp)){
        throw new IncorrectOtpException("otp entered is incorrect");
      }
      ResetPasswordOtpVerifyResponseDto response = new ResetPasswordOtpVerifyResponseDto();
      response.setStatus("success");
      response.setMessage("Otp verified successfully");
      resetPasswordEntry.setOtpVerified(true);
      resetPasswordEntry.setOtpVerifiedBy(ResetPassword.OtpVerifiedBy.PHONE);
      resetPasswordRepository.save(resetPasswordEntry);

      return response;
  }

  @Override
  @Transactional
  public ResetPasswordFinalResponseDto resetUserPasswordFinal(String identifier, String password){

      UserDataEntity user = userDataRepository.findByIdentifier(identifier).orElseThrow(()-> new IdentifierNotFound("user not registered"));
      ResetPassword resetPassword = resetPasswordRepository.findByIdentifier(identifier).orElseThrow(()-> new IdentifierNotFound("user not found"));

      if(!resetPassword.getOtpVerified()){
        throw new IncorrectPassword("first verify your identity");
      }

    boolean isPasswordMatches =  passwordEncoder.matches(password, user.getHashedPassword());

      if(isPasswordMatches){
        throw new IncorrectPassword("password cannot be same as previous one");
      }

      String hashedPassword = passwordEncoder.encode(password);
      user.setHashedPassword(hashedPassword);
      user.setNoOfTimePasswordChanged(user.getNoOfTimePasswordChanged()+1);
      user.setLastPasswordChangeMethod(resetPassword.getOtpVerifiedBy());


      userDataRepository.save(user);
      ResetPasswordFinalResponseDto response = new ResetPasswordFinalResponseDto();
       response.setStatus("success");
       response.setMessage("password changed successfully");
       return response;
  }




}
