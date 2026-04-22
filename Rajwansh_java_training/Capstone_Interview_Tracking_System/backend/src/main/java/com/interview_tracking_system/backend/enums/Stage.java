package com.interview_tracking_system.backend.enums;

/**
 * Represents different stages of an interview process.
 */
public enum Stage {

    /** Initial profiling of candidate */
    PROFILING,

    /** HR screening round */
    SCREENING,

    /** First technical interview round (L1) */
    L1_TECHNICAL,

    /** Second technical interview round (L2) */
    L2_TECHNICAL,

    /** Final HR discussion round */
    HR_ROUND
}