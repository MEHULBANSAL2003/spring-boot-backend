package com.springbootBackend.backend.service.AuthControllerService;

import com.springbootBackend.backend.dto.loginByUsernameAndPasswordDto.LoginByUserNamePasswordResponseDto;
import com.springbootBackend.backend.dto.userEmailSignUpDto.EmailSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationResponseDto;
import com.springbootBackend.backend.entity.UserDataEntity;
import com.springbootBackend.backend.entity.UserPendingVerification;
import com.springbootBackend.backend.exceptions.customExceptions.*;
import com.springbootBackend.backend.helper.OtpGenerator;
import com.springbootBackend.backend.repository.UserDataRepository;
import com.springbootBackend.backend.repository.UserPendingVerificationRepository;
import com.springbootBackend.backend.service.emailService.EmailService;
import com.springbootBackend.backend.service.smsService.SmsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        newUser.setCurrStatus("ACTIVE");
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
    @Transactional(dontRollbackOn = IncorrectPassword.class)
    public LoginByUserNamePasswordResponseDto loginUserByCredentials(String identifier, String password){

        UserDataEntity user = userDataRepository.findByIdentifier(identifier).orElse(null);

        if(user == null){
            throw new IdentifierNotFound("Invalid credentials.Please provide correct credentials");
        }
       boolean isPasswordCorrect =  passwordEncoder.matches(password, user.getHashedPassword());

      if(user.getIncorrectAttempts()>5 && user.getIncorrectAttemptTimeWindowStart().plusMinutes(5).isAfter(LocalDateTime.now())){
          int noOfTimesUserIsblocked = user.getBlockedCount();
          if(noOfTimesUserIsblocked>5){
              user.setBlockForMin(1440);
              user.setBlockedStartTime(LocalDateTime.now());
          }
          else{
              user.setBlockedCount(user.getBlockedCount()+1);
              user.setBlockForMin(user.getBlockForMin()+30);
              user.setBlockedStartTime(LocalDateTime.now());

          }
            throw new Error("");
      }


        if(!isPasswordCorrect){
             user.setIncorrectAttempts(user.getIncorrectAttempts()+1);
             user.setIncorrectAttemptTimeWindowStart(LocalDateTime.now());
            userDataRepository.save(user);
            throw new IncorrectPassword("You have entered incorrect Password.");
        }

        // handle the case of no of times incorrect otp entered.. if entered 5 times incorrect in timespan of 5 min.. block for 30 minutes by curr state inactive for 30 min;

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
                user.getUpdatedAt()
        );

        // generate jwt token..

        return response;

    }
}
