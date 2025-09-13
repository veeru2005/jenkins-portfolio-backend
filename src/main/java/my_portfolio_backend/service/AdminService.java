package my_portfolio_backend.service;

import my_portfolio_backend.entity.Admin;
import my_portfolio_backend.repository.AdminRepository;
import my_portfolio_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final Emai_Otp_Service emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Using Constructor Injection (recommended best practice)
    @Autowired
    public AdminService(AdminRepository adminRepository, Emai_Otp_Service emailService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public CommandLineRunner initDefaultAdmin() {
        return args -> {
            String adminEmail = "sunkavalli.veerendra1973@gmail.com";
            if (adminRepository.findByEmail(adminEmail).isEmpty()) {
                Admin admin = new Admin();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("Veeru@2005#"));
                adminRepository.save(admin);
                System.out.println("\n--- DEFAULT ADMIN CREATED ---\n");
            } else {
                System.out.println("\n--- ADMIN ALREADY EXISTS ---\n");
            }
        };
    }

    public String handleLogin(String email) {
        Optional<Admin> optAdmin = adminRepository.findByEmail(email);
        if (optAdmin.isEmpty()) return "Admin not found";

        Admin admin = optAdmin.get();
        String otp = String.format("%06d", new Random().nextInt(999999));
        admin.setOtp(otp);
        admin.setOtpGeneratedAt(LocalDateTime.now());
        adminRepository.save(admin);

        emailService.sendOtp(admin.getEmail(), otp);
        return "OTP sent to your email";
    }

    // --- THIS METHOD IS NOW CORRECTED ---
    /**
     * Verifies the OTP. If valid, it clears the OTP fields and returns a JWT token.
     * If invalid, expired, or not found, it returns null.
     */
    public String verifyOtp(String email, String otp) {
        Optional<Admin> optAdmin = adminRepository.findByEmail(email);
        
        // 1. Check if admin exists
        if (optAdmin.isEmpty()) {
            return null;
        }

        Admin admin = optAdmin.get();
        
        // 2. Check if an OTP was ever generated
        if (admin.getOtp() == null || admin.getOtpGeneratedAt() == null) {
            return null;
        }

        // 3. Check if the OTP has expired (5-minute validity)
        boolean isExpired = admin.getOtpGeneratedAt().plusMinutes(5).isBefore(LocalDateTime.now());
        if (isExpired) {
            return null;
        }

        // 4. Check if the provided OTP matches the one in the database
        if (otp.equals(admin.getOtp())) {
            // Success! Clear the OTP and generate the token
            admin.setOtp(null);
            admin.setOtpGeneratedAt(null);
            adminRepository.save(admin);

            return jwtUtil.generateToken(admin.getEmail());
        }

        // 5. If OTPs don't match, return null
        return null;
    }
}