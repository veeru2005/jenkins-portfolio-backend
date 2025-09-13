package my_portfolio_backend.controller;

import my_portfolio_backend.dto.LoginRequest;
import my_portfolio_backend.dto.OtpVerificationRequest;
import my_portfolio_backend.entity.Admin;
import my_portfolio_backend.repository.AdminRepository;
import my_portfolio_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // Using Constructor Injection (recommended best practice)
    @Autowired
    public AdminController(AdminService adminService, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Admin admin = adminRepository.findByEmail(req.getEmail()).orElse(null);

        if (admin != null && passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
            String message = adminService.handleLogin(req.getEmail());
            return ResponseEntity.ok(Map.of("message", message));
        }

        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest req) {
        // Call the service, which returns the token string on success or null on failure
        String token = adminService.verifyOtp(req.getEmail(), req.getOtp());

        // If the token is not null, verification was successful
        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        }

        // If the token is null, verification failed
        return ResponseEntity.status(401).body(Map.of("message", "The OTP you entered is incorrect or expired."));
    }
}