package com.springbootBackend.backend.helper;

import java.util.Random;

public class OtpGenerator {

public int generateOtp(){
    Random random = new Random();
    int otp = 100000 + random.nextInt(900000);
    return otp;
}
}
