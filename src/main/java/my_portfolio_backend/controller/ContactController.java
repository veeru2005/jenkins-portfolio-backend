package my_portfolio_backend.controller;

import my_portfolio_backend.entity.ContactMessage;
import my_portfolio_backend.service.Emai_Otp_Service;
import my_portfolio_backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private Emai_Otp_Service emailService; // Inject the email service

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ContactMessage message) {
        try {
            // Step 1: Save the message to the database
            portfolioService.saveContactMessage(message);

            // Step 2: Send the email notification with the new grid layout
            String to = "sunkavalli.veerendra1973@gmail.com";
            String subject = "New Message From Your Portfolio: " + message.getSubject();
            
            // MODIFIED: Using a table for a clean grid layout.
            String body = String.format("""
                <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="color-scheme" content="light dark">
    <meta name="supported-color-schemes" content="light dark">
    <title>New Portfolio Message</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        :root {
            color-scheme: light dark;
            supported-color-schemes: light dark;
        }
        @media (max-width: 600px) {
            .container {
                padding: 15px !important;
            }
            .content {
                padding: 20px !important;
            }
        }
        @media (prefers-color-scheme: dark) {
            .body-bg { background-color: #121212 !important; }
            .card { background-color: #1e1e1e !important; box-shadow: 0 10px 30px rgba(0,0,0,0.5) !important; border-color: #333333 !important; }
            .header-text { color: #ffffff !important; }
            .label-text { color: #9e9e9e !important; }
            .value-text, .message-content { color: #e0e0e0 !important; }
            .accent-text { color: #bb86fc !important; }
            .message-box { background-color: #2c2c2c !important; }
            .divider { border-bottom: 1px solid #333333 !important; }
            .footer-text { color: #757575 !important; }
        }
    </style>
</head>
<body style="margin: 0; padding: 0; width: 100%% !important; background-color: #f7f9fc;" class="body-bg">
    <div class="container" style="font-family: 'Outfit', Arial, sans-serif; background-color: #f7f9fc; padding: 40px;">
        <div class="card" style="max-width: 600px; width: 100%%; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.07); border: 1px solid #e0e0e0;">
            <div style="background: linear-gradient(45deg, #3f51b5 30%%, #1a237e 90%%); color: white; padding: 24px;">
                <h2 class="header-text" style="margin: 0; font-size: 24px; font-weight: 700;">New Portfolio Message</h2>
            </div>
            <div class="content" style="padding: 32px;">
                <table style="width: 100%%; border-collapse: collapse;">
                    <tr class="divider" style="border-bottom: 1px solid #e0e0e0;">
                        <td class="label-text" style="padding: 12px 0; color: #555; font-size: 14px; width: 100px;">Subject:</td>
                        <td class="accent-text" style="padding: 12px 0; font-size: 16px; color: #1a237e; font-weight: 500;">%s</td>
                    </tr>
                    <tr class="divider" style="border-bottom: 1px solid #e0e0e0;">
                        <td class="label-text" style="padding: 12px 0; color: #555; font-size: 14px;">From Name:</td>
                        <td class="value-text" style="padding: 12px 0; font-size: 16px; color: #333;">%s</td>
                    </tr>
                    <tr>
                        <td class="label-text" style="padding: 12px 0; color: #555; font-size: 14px;">Email:</td>
                        <td class="value-text" style="padding: 12px 0; font-size: 16px; color: #333;">%s</td>
                    </tr>
                </table>
                <div style="margin-top: 28px;">
                    <p class="label-text" style="color: #555; margin: 0 0 8px 0; font-size: 14px;">Message:</p>
                    <div class="message-box" style="background-color: #f7f9fc; border-radius: 8px; padding: 16px;">
                        <p class="message-content" style="font-size: 16px; color: #333; line-height: 1.6; margin: 0;">%s</p>
                    </div>
                </div>
                  <div style="max-width: 600px; margin: 20px auto 0 auto; text-align: center;">
            <p class="footer-text" style="font-size: 12px; color: #999999; margin: 0;">
                © 2025 Sunkavalli Veerendra Chowdary. All rights reserved.
            </p>
        </div>
            </div>
            
        </div>
    </div>
</body>
</html>
            """, message.getSubject(), message.getName(), message.getEmail(), message.getMessage());

            emailService.sendEmail(to, subject, body);

            return ResponseEntity.ok("Message sent and saved successfully!");

        } catch (Exception e) {
            // If anything fails, log the error and inform the frontend.
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }
}

