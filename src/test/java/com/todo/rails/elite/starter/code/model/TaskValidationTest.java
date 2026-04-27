package com.todo.rails.elite.starter.code.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TaskValidationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception exception) {
            System.err.println("Error setting up validator: " + exception.getMessage());
        }
    }

    @Test
    void validateTask_Success() {
        Task validTask = new Task("My Task", "Some description", false, LocalDate.now());
        Set<ConstraintViolation<Task>> violations = validator.validate(validTask);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validateTask_EmptyTitle() {
        Task invalidTask = new Task("", "Some description", false, LocalDate.now());
        Set<ConstraintViolation<Task>> violations = validator.validate(invalidTask);

        assertFalse(violations.isEmpty());
        assertEquals("Title cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validateTask_EmptyDescription() {
        Task invalidTask = new Task("Some title", "", false, LocalDate.now());
        Set<ConstraintViolation<Task>> violations = validator.validate(invalidTask);

        assertFalse(violations.isEmpty());
        assertEquals("Description cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void validateTask_NullDueDate() {
        Task invalidTask = new Task("Some title", "Some description", false, null);
        Set<ConstraintViolation<Task>> violations = validator.validate(invalidTask);

        assertFalse(violations.isEmpty());
        assertEquals("Due date cannot be null", violations.iterator().next().getMessage());
    }
}