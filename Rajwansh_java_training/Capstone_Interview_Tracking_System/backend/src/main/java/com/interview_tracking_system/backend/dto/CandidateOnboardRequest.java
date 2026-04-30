package com.interview_tracking_system.backend.dto;

import java.time.LocalDate;

public class CandidateOnboardRequest {

    /**
     * Full name of the candidate.
     */
    private String fullName;

    /**
     * Email address of the candidate.
     * This is used as a unique identifier for login and communication.
     */
    private String email;

    /**
     * Mobile number of the candidate.
     * Used for contact and verification purposes.
     */
    private String mobileNumber;

    /**
     * Date of birth of the candidate.
     */
    private LocalDate dob;

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
