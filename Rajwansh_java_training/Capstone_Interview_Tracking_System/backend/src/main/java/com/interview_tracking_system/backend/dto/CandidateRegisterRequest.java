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

    /** Maximum allowed length for name fields. */
    private static final int MAX_NAME_LENGTH = 80;

    /** Full name of the candidate. */
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = MAX_NAME_LENGTH, message = "Full name must be between 2 and 80 characters")
    private String fullName;

    /** Email address of the candidate. */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** Mobile number of the candidate. */
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit mobile number")
    private String mobileNumber;

    /** Date of birth of the candidate. */
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    /** Gender of the candidate. */
    @NotNull(message = "Gender is required")
    private Gender gender;

    /**
     * Returns the full name.
     *
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param candidateFullName full name
     */
    public void setFullName(final String candidateFullName) {
        this.fullName = candidateFullName;
    }

    /**
     * Returns the mobile number.
     *
     * @return mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the mobile number.
     *
     * @param candidateMobileNumber mobile number
     */
    public void setMobileNumber(final String candidateMobileNumber) {
        this.mobileNumber = candidateMobileNumber;
    }

    /**
     * Returns the date of birth.
     *
     * @return date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets the date of birth.
     *
     * @param candidateDob date of birth
     */
    public void setDob(final LocalDate candidateDob) {
        this.dob = candidateDob;
    }

    /**
     * Returns the gender.
     *
     * @return gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the gender.
     *
     * @param candidateGender gender
     */
    public void setGender(final Gender candidateGender) {
        this.gender = candidateGender;
    }

    /**
     * Returns the email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param candidateEmail email
     */
    public void setEmail(final String candidateEmail) {
        this.email = candidateEmail;
    }
}
