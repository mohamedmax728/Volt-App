package Volt.example.Volt.CustomerManagement.Application.Services.Shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendVerificationEmail(String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mohamedmax728@gmail.com");
            message.setTo(email);
            message.setSubject("Please confirm your email");
            message.setText(String.format("Your OTP for verify your account is %s", token));
            emailSender.send(message);
        } catch (Exception ex) {
            // Exception handling
            ex.printStackTrace();
        }
    }

    public void sendOtp(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mohamedmax728@gmail.com");
            message.setTo(email);
            message.setSubject("Forget Password");
            message.setText(String.format("Your OTP for forget password is %s", otp));
            emailSender.send(message);
        } catch (Exception ex) {
            // Exception handling
            ex.printStackTrace();
        }
    }
}
