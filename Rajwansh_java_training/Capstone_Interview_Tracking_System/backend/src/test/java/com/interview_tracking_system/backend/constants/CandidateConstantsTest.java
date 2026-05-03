package com.interview_tracking_system.backend.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class CandidateConstantsTest {

    @Test
    void shouldHaveExpectedConstantValues() {
        assertEquals("Candidate registered successfully.", CandidateConstants.MSG_REGISTER_SUCCESS);
        assertEquals("Login successful.", CandidateConstants.MSG_LOGIN_SUCCESS);
        assertEquals("Logged out successfully.", CandidateConstants.MSG_LOGOUT_SUCCESS);
        assertEquals("Email already registered.", CandidateConstants.ERROR_EMAIL_EXISTS);
        assertEquals("Passwords do not match.", CandidateConstants.ERROR_PASSWORD_MISMATCH);
        assertEquals("Invalid email or password.", CandidateConstants.ERROR_INVALID_CREDENTIALS);
        assertEquals("You have already applied.", CandidateConstants.ERROR_ALREADY_APPLIED);
        assertEquals("User not found.", CandidateConstants.ERROR_NOT_FOUND);
        assertEquals("User not logged in.", CandidateConstants.ERROR_NOT_LOGGED_IN);
        assertEquals("No application found.", CandidateConstants.ERROR_NO_APPLICATION);
    }

    @Test
    void constructorShouldThrowUnsupportedOperationException() throws Exception {
        Constructor<CandidateConstants> constructor = CandidateConstants.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance);

        assertEquals(
                UnsupportedOperationException.class,
                exception.getCause().getClass());

        assertEquals(
                "Cannot instantiate constants class",
                exception.getCause().getMessage());
    }
}
