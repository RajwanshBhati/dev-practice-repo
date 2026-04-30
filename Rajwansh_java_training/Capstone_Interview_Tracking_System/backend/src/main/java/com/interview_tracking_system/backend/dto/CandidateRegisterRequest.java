package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO for candidate registration request.
 */
public class CandidateRegisterRequest {

    /** Full name of the candidate. */
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 80, message = "Full name must be between 2 and 80 characters")
    private String fullName;

    /** Email address of the candidate. */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** Mobile No. of the candidadte */
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit mobile number")
    private String mobileNumber;

    /* DOB of candidate */
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    /** Gender of candidate */
    @NotNull(message = "Gender is required")
    private Gender gender;

    /** Password chosen by the candidate. */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    /** Confirm password for validation. */
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    /**
     * Returns the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param name the full name to set
     */
    public void setFullName(final String name) {
        this.fullName = name;
    }

    /**
     * get the mobile number
     *
     * @return Mobile Number
     */

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * get the DOB
     *
     * @return DOB;
     */

    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets the dob.
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    /**
     * Gets the Gender
     *
     * @return gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the gender
     *
     * @param gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Returns the email.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param userEmail the email to set
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param userPassword the password to set
     */
    public void setPassword(final String userPassword) {
        this.password = userPassword;
    }

    /**
     * Returns the confirm password.
     *
     * @return the confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirm password.
     *
     * @param confirm the confirm password to set
     */
    public void setConfirmPassword(final String confirm) {
        this.confirmPassword = confirm;
    }
}
