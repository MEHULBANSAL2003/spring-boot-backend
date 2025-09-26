package com.springbootBackend.backend.service.smsService;

import com.springbootBackend.backend.config.TwilioConfig;
import com.twilio.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

    @Autowired
    private TwilioConfig twilioConfig;

    public boolean sendSms(String toPhoneNumber, String otp) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(twilioConfig.getFromPhoneNumber()),
                    "Your Otp code for signup is: " + otp +". Please enter this otp to successfully signup to create account at company@mehul.(Valid only for 10 minutes)"
            ).create();

            return true;
        } catch (ApiException e) {
            System.err.println("Twilio API error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
        }
        return false;
    }

  public boolean sendResetPassworSms(String toPhoneNumber, String otp) {
    try {
      Message message = Message.creator(
        new PhoneNumber(toPhoneNumber),
        new PhoneNumber(twilioConfig.getFromPhoneNumber()),
        "Your Otp code for reset password is: " + otp +". Please enter this otp to change your password.(Valid only for 10 minutes)"
      ).create();

      return true;
    } catch (ApiException e) {
      System.err.println("Twilio API error: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Failed to send SMS: " + e.getMessage());
    }
    return false;
  }
}
