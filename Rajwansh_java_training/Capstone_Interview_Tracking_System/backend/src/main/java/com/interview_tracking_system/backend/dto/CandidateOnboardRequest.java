package com.interview_tracking_system.backend.dto;

import java.time.LocalDate;

import com.interview_tracking_system.backend.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for candidate onboarding request.
 * Contains personal details required for candidate registration.
 */
public class CandidateOnboardRequest {

    /** Maximum allowed length for name fields. */
    private static final int MAX_NAME_LENGTH = 80;

    /**
     * Full name of the candidate.
     */
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = MAX_NAME_LENGTH, message = "Full name must be between 2 and 80 characters")
    private String fullName;

    /**
     * Email address of the candidate.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * Mobile number of the candidate.
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
     * Gender of the candidate.
     */
    @NotNull(message = "Gender is required")
    private Gender gender;

    /**
     * Returns the gender of the candidate.
     *
     * @return gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the gender of the candidate.
     *
     * @param candidateGender gender value
     */
    public void setGender(final Gender candidateGender) {
        this.gender = candidateGender;
    }

    /**
     * Returns the full name of the candidate.
     *
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the candidate.
     *
     * @param candidateFullName full name
     */
    public void setFullName(final String candidateFullName) {
        this.fullName = candidateFullName;
    }

    /**
     * Returns the email of the candidate.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the candidate.
     *
     * @param candidateEmail email
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }

    /**
     * Returns the mobile number of the candidate.
     *
     * @return mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the mobile number of the candidate.
     *
     * @param candidateMobileNumber mobile number
     */
    public void setMobileNumber(final String candidateMobileNumber) {
        this.mobileNumber = candidateMobileNumber;
    }

    /**
     * Returns the date of birth of the candidate.
     *
     * @return date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the candidate.
     *
     * @param candidateDob date of birth
     */
    public void setDob(final LocalDate candidateDob) {
        this.dob = candidateDob;
    }
}
