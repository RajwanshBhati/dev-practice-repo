package com.interview_tracking_system.backend.dto;

import java.time.LocalDate;
import com.interview_tracking_system.backend.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CandidateOnboardRequest {

    /**
     * Full name of the candidate.
     */
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 80, message = "Full name must be between 2 and 80 characters")
    private String fullName;

    /**
     * Email address of the candidate.
     * This is used as a unique identifier for login and communication.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * Mobile number of the candidate.
     * Used for contact and verification purposes.
     */
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit mobile number")
    private String mobileNumber;

    /**
     * Date of birth of the candidate.
     */
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    /**
     * Gender of the candidate
     */
    @NotNull(message = "Gender is required")
    private Gender gender;

    /**
     * Gets the gender of the candidate
     *
     * @return gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * sets the gender
     *
     * @param gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Gets the full name of the candidate.
     *
     * @return full name of the candidate
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the candidate.
     *
     * @param fullName candidate's full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the email address of the candidate.
     *
     * @return candidate email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the candidate.
     *
     * @param email candidate email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the mobile number of the candidate.
     *
     * @return candidate mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the mobile number of the candidate.
     *
     * @param mobileNumber candidate mobile number
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * Gets the date of birth of the candidate.
     *
     * @return candidate date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the candidate.
     *
     * @param dob candidate date of birth
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
