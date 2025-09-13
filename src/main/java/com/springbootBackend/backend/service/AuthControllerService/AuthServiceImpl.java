package com.springbootBackend.backend.service.AuthControllerService;


import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationResponseDto;
import com.springbootBackend.backend.entity.UserDataEntity;
import com.springbootBackend.backend.entity.UserPendingVerification;
import com.springbootBackend.backend.exceptions.customExceptions.IncorrectOtpException;
import com.springbootBackend.backend.exceptions.customExceptions.OtpExpiresException;
import com.springbootBackend.backend.exceptions.customExceptions.PhoneNumberAlreadyExistsException;
import com.springbootBackend.backend.exceptions.customExceptions.UserNameExistsException;
import com.springbootBackend.backend.helper.OtpGenerator;
import com.springbootBackend.backend.repository.UserDataRepository;
import com.springbootBackend.backend.repository.UserPendingVerificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    UserPendingVerificationRepository userPendingVerificationRepository;


    @Override
    @Transactional
    public MobileSignUpResponseDto mobileSignupGetOtp(String phoneNumber, String userName, String password) {

        if(userDataRepository.findByPhoneNumber(phoneNumber).isPresent()){
            throw new PhoneNumberAlreadyExistsException("Phone number already registered: "+ phoneNumber);
        }
        if (userDataRepository.findByUserName(userName).isPresent()) {
            throw new UserNameExistsException("Username already taken: "+ userName);
        }

        int otp = new OtpGenerator().generateOtp();

        UserPendingVerification pendingUser = userPendingVerificationRepository
                .findByUserName(userName)
                .orElse(null);

        if(pendingUser == null){
            pendingUser = new UserPendingVerification();
            pendingUser.setPhoneNumber(phoneNumber);
           pendingUser.setCountryCode("+91");
           pendingUser.setPassword(password);
            pendingUser.setUserName(userName);
            pendingUser.setOtp(String.valueOf(otp));
            pendingUser.setIncorrectAttempts(0);
            pendingUser.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
            pendingUser.setIsTwilioActive(true);

        }
        pendingUser.setOtp(String.valueOf(otp));
        pendingUser.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));

        userPendingVerificationRepository.save(pendingUser);

        MobileSignUpResponseDto response = new MobileSignUpResponseDto("success", 6, true,"Otp sent successfully", 300,true);
        return response;
    }


    @Override
    @Transactional
    public UserMobileSignupVerificationResponseDto mobileSignUpVerifyOtp(String userName, String otp) {

        UserPendingVerification pendingUser = userPendingVerificationRepository.findByUserName(userName).orElse(null);
        UserDataEntity verifiedUser = userDataRepository.findByUserName(userName).orElse(null);

        if(verifiedUser!=null){
            throw new UserNameExistsException("Username already taken: "+ userName);
        }

        if(pendingUser==null && verifiedUser==null){
            throw new UserNameExistsException("No such username exists"+ userName);
        }
        if(pendingUser!=null && pendingUser.getOtpExpiryTime().isBefore(LocalDateTime.now())){
          throw new OtpExpiresException("Otp expired!! Resend otp to continue!!");
        }

        if(pendingUser!=null && !pendingUser.getOtp().equals(otp)){
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
}
