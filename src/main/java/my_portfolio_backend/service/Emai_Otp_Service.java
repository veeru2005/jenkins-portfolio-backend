package my_portfolio_backend.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class Emai_Otp_Service {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true enables HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendOtp(String to, String otp) {
    	String htmlBody = """
    			<div style="font-family: 'Outfit', Arial, sans-serif; background-color: #f3f4f6; padding: 20px;">
              <div style="max-width: 500px; margin: 20px auto; border: 1px solid #e5e7eb; border-radius: 18px; box-shadow: 0 8px 24px rgba(0,0,0,0.08); overflow: hidden;">
                <div style="background-color: #312e81; color: #ffffff; padding: 24px; text-align: center;">
                  <h2 style="font-size: 24px; font-weight: 700; margin: 0;">
                    🔐 My Portfolio - OTP Verification
                  </h2>
                </div>
                <div style="background-color: #ffffff; padding: 36px;">
                  <p style="font-size: 16px; color: #374151; text-align: center; line-height: 1.6; margin-top: 0; margin-bottom: 26px;">
                    Enter the following One-Time Password (OTP) to continue:
                  </p>
                  <div style="text-align: center; font-size: 36px; font-weight: 700; color: #312e81; letter-spacing: 10px; margin: 28px 0; background: #eef2ff; padding: 16px 5px 16px 15px; border-radius: 12px;">
                    %s
                  </div>
                  <p style="font-size: 15px; color: #4b5563; text-align: center; margin-bottom: 28px;">
                    This code will expire in <strong style="color:#11127;">5 minutes</strong>. Keep it confidential.
                  </p>
                  <div style="height:1px; background-color: #e5e7eb; margin: 28px 0;"></div>
                  <p style="font-size: 13px; color: #6b7280; text-align: center; margin: 0;">
                    &copy; 2025 <strong style="color:#312e81;">Sunkavalli Veerendra Chowdary</strong> · All Rights Reserved
                  </p>
                </div>
              </div>
            </div>
    			""".formatted(otp);
    	String uniqueSubject = "Your OTP Code [" + new Random().nextInt(10000) + "]";


        sendEmail(to, uniqueSubject, htmlBody);
    }
}