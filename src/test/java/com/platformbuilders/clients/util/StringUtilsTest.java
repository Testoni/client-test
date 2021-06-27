package com.platformbuilders.clients.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("String Utils Test")
class StringUtilsTest {

    @Test
    @DisplayName("Should return true to string")
    void shouldReturnValidString() {
        assertFalse(StringUtils.isEmptyString("TEST"));
    }

    @Test
    @DisplayName("Should return false to empty string")
    void shouldReturnFalseToEmptyString() {
        assertTrue(StringUtils.isEmptyString(""));
    }

    @Test
    @DisplayName("Should return false to nullable string")
    void shouldReturnFalseToNullableString() {
        assertTrue(StringUtils.isEmptyString(null));
    }

    @Test
    @DisplayName("Should return true to valid email")
    void shouldReturnTrueToValidEmail() {
        assertTrue(StringUtils.isValidEmailAddress("gabriel@gabriel.com"));
    }

    @Test
    @DisplayName("Should return false to invalid email")
    void shouldReturnFalseToInvalidEmail() {
        assertFalse(StringUtils.isValidEmailAddress("gabriel"));
    }

}