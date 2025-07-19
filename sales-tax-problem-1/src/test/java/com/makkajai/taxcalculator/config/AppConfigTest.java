package com.makkajai.taxcalculator.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    @DisplayName("Should prevent instantiation of AppConfig")
    void constructor_shouldBePrivate() throws NoSuchMethodException {
        Constructor<AppConfig> constructor = AppConfig.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true); // Allow access for testing private constructor
        assertThrows(InvocationTargetException.class, constructor::newInstance); // Should ideally not be invokable
    }

    @Test
    @DisplayName("Should verify BASIC_TAX_RATE constant")
    void basicTaxRate_shouldBeCorrect() {
        assertEquals(0.10, AppConfig.BASIC_TAX_RATE, 0.001);
    }

    @Test
    @DisplayName("Should verify IMPORT_TAX_RATE constant")
    void importTaxRate_shouldBeCorrect() {
        assertEquals(0.05, AppConfig.IMPORT_TAX_RATE, 0.001);
    }

    @Test
    @DisplayName("Should verify EXIT_CODE_SUCCESS constant")
    void exitCodeSuccess_shouldBeCorrect() {
        assertEquals(0, AppConfig.EXIT_CODE_SUCCESS);
    }

    @Test
    @DisplayName("Should verify BOOK_KEYWORDS constant")
    void bookKeywords_shouldContainExpected() {
        assertArrayEquals(new String[]{"book"}, AppConfig.BOOK_KEYWORDS);
    }

    @Test
    @DisplayName("Should verify FOOD_KEYWORDS constant")
    void foodKeywords_shouldContainExpected() {
        assertArrayEquals(new String[]{"chocolate", "food"}, AppConfig.FOOD_KEYWORDS);
    }

    @Test
    @DisplayName("Should verify MEDICAL_KEYWORDS constant")
    void medicalKeywords_shouldContainExpected() {
        assertArrayEquals(new String[]{"pill", "headache"}, AppConfig.MEDICAL_KEYWORDS);
    }
}