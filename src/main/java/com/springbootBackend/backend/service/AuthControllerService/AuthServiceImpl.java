package com.springbootBackend.backend.service.AuthControllerService;


import com.springbootBackend.backend.dto.userMobileSignUpDto.MobileSignUpResponseDto;
import com.springbootBackend.backend.exceptions.customExceptions.PhoneNumberAlreadyExistsException;
import com.springbootBackend.backend.exceptions.customExceptions.UserNameExistsException;
import com.springbootBackend.backend.repository.UserDataRepository;
import com.springbootBackend.backend.repository.UserPendingVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    UserPendingVerificationRepository userPendingVerificationRepository;


    @Override
    public MobileSignUpResponseDto mobileSignupGetOtp(String phoneNumber, String userName, String password) {

        if(userDataRepository.findByPhoneNumber(phoneNumber).isPresent()){
            throw new PhoneNumberAlreadyExistsException("Phone number already registered: "+ phoneNumber);
        }
        if (userDataRepository.findByUserName(userName).isPresent()) {
            throw new UserNameExistsException("Username already taken: "+ userName);
        }

        MobileSignUpResponseDto response = new MobileSignUpResponseDto("success", 4, true,"Otp sent successfully",300,60);
        return response;
    }
}
