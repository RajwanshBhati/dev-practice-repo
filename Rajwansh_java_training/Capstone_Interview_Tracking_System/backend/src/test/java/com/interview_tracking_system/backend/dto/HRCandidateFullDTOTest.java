package com.interview_tracking_system.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for HRCandidateFullDTO.
 */
class HRCandidateFullDTOTest {

    /**
     * Tests setter and getter methods.
     */
    @Test
    void shouldSetAndGetAllFields() {
        HRCandidateFullDTO dto = new HRCandidateFullDTO();

        dto.setId(1L);
        dto.setName("Raj");
        dto.setEmail("raj@test.com");
        dto.setMobileNumber("9876543210");
        dto.setCurrentCompany("ABC Corp");
        dto.setCurrentCtc(5.5);
        dto.setExpectedCtc(8.0);
        dto.setTotalExperience(5);
        dto.setRelevantExperience(4);
        dto.setPreferredLocation("Pune");
        dto.setNoticePeriod(30);
        dto.setSource("LinkedIn");
        dto.setJobTitle("Java Developer");
        dto.setStatus("PROFILING");
        dto.setResumeUrl("/api/resumes/raj.pdf");

        assertEquals(1L, dto.getId());
        assertEquals("Raj", dto.getName());
        assertEquals("raj@test.com", dto.getEmail());
        assertEquals("9876543210", dto.getMobileNumber());
        assertEquals("ABC Corp", dto.getCurrentCompany());
        assertEquals(5.5, dto.getCurrentCtc());
        assertEquals(8.0, dto.getExpectedCtc());
        assertEquals(5, dto.getTotalExperience());
        assertEquals(4, dto.getRelevantExperience());
        assertEquals("Pune", dto.getPreferredLocation());
        assertEquals(30, dto.getNoticePeriod());
        assertEquals("LinkedIn", dto.getSource());
        assertEquals("Java Developer", dto.getJobTitle());
        assertEquals("PROFILING", dto.getStatus());
        assertEquals("/api/resumes/raj.pdf", dto.getResumeUrl());
    }
}
