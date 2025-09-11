package my_portfolio_backend.dto;

public class OtpVerificationRequest {
    private String email;
    private String otp;

    // --- Getters and Setters (Now correctly implemented) ---

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        // This was the bug. The old method returned null.
        // This now correctly returns the OTP from the request.
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}