package my_portfolio_backend.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String otp;

    private LocalDateTime otpGeneratedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public LocalDateTime getOtpGeneratedAt() { return otpGeneratedAt; }
    public void setOtpGeneratedAt(LocalDateTime otpGeneratedAt) { this.otpGeneratedAt = otpGeneratedAt; }
}