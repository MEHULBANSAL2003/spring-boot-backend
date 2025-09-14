package com.springbootBackend.backend.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

}
