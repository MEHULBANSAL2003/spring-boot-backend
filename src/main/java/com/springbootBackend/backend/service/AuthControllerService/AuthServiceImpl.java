package com.springbootBackend.backend.service.AuthControllerService;


import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.dto.userMobileSignUpVerificationDto.UserMobileSignupVerificationResponseDto;
import com.springbootBackend.backend.entity.UserPendingVerification;
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
        pendingUser.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));

        userPendingVerificationRepository.save(pendingUser);

        MobileSignUpResponseDto response = new MobileSignUpResponseDto("success", 6, true,"Otp sent successfully", 300,true);
        return response;
    }


    @Override
    @Transactional
    public UserMobileSignupVerificationResponseDto mobileSignUpVerifyOtp(String userName, String otp) {
        return null;
    }
}
