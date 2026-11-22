package com.student.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * ValidationUtils provides static validation helper methods for student registration,
 * phone formatting, email format, and date checking.
 */
public class ValidationUtils {

    // RFC 5322 compliant regex pattern for email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // 10-digit mobile number pattern (supports optional leading +91 or 0)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(?:\\+91|0)?[6-9]\\d{9}$"
    );

    // Alphanumeric student ID pattern (e.g. STU1001, BCA2024, S-101)
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_-]{3,20}$"
    );

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Checks if string is null, empty, or whitespace-only.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validates email address format.
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates 10-digit mobile phone number.
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    /**
     * Validates alphanumeric student ID format.
     */
    public static boolean isValidStudentId(String studentId) {
        if (isEmpty(studentId)) return false;
        return STUDENT_ID_PATTERN.matcher(studentId.trim()).matches();
    }

    /**
     * Validates date string in format YYYY-MM-DD and ensures date is in the past.
     */
    public static boolean isValidPastDate(String dateStr) {
        if (isEmpty(dateStr)) return false;
        try {
            LocalDate date = LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
            return date.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
