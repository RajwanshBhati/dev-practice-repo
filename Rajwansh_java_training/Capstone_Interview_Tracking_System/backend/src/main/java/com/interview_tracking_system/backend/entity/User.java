package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Index;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.interview_tracking_system.backend.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email")
})
public class User {

    /**
     * Unique identifier for the user (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the user.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Unique email used for login.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Date BOB
     */
    @Column
    private LocalDate dateOfBirth;

    /**
     * Gender
     */

    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * Mobile number of the user (optional but unique if provided).
     */
    @Column(unique = true)
    private String mobile;

    /**
     * Gets the gender
     *
     * @return gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the Gender
     *
     * @param gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Encrypted password of the user.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role of the user in the system (HR, PANEL, CANDIDATE).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Current status of the user account.
     * Possible values: ACTIVE, INACTIVE, LOCKED.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * Organisation the user belongs to.
     */
    @Column
    private String organisation;

    /**
     * Job designation of the user.
     */
    @Column
    private String designation;

    /**
     * Activation token for account setup or verification.
     */
    @Column(name = "activation_token")
    private String activationToken;

    /**
     * Expiry time of activation token.
     */
    @Column(name = "activation_token_expiry")
    private LocalDateTime activationTokenExpiry;

    /**
     * Timestamp when user was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when user was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Default constructor for JPA.
     */

    public User() {
    }

    public User(String name, String email, String mobile, String password,
            Role role, UserStatus status, String organisation,
            String designation, String activationToken,
            LocalDateTime activationTokenExpiry) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.role = role;
        this.status = (status != null) ? status : UserStatus.ACTIVE;
        this.organisation = organisation;
        this.designation = designation;
        this.activationToken = activationToken;
        this.activationTokenExpiry = activationTokenExpiry;
    }

    /**
     *
     * @return
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public LocalDateTime getActivationTokenExpiry() {
        return activationTokenExpiry;
    }

    public void setActivationTokenExpiry(LocalDateTime activationTokenExpiry) {
        this.activationTokenExpiry = activationTokenExpiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
