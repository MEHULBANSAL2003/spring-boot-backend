package com.springbootBackend.backend.service.emailService;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;


    public void sendOtpEmail(String toEmail, String otp) {
        Email from = new Email(fromEmail);
        String subject = "Your OTP Code";
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "Your Otp code for signup is: " + otp + ". Please enter this otp to successfully signup to create account at company@mehul.(Valid only for 10 minutes)");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("Failed to send OTP email: " + response.getBody());
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error sending OTP email", ex);
        }
    }

}



