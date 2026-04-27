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

// TODO 12: Test Validation Logic. Write tests to verify that invalid inputs (e.g., empty title) are rejected.
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
//public class TaskValidationTest {
//    private Validator validator;
//
//    @BeforeEach
//    void setUp() {
//        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
//            validator = factory.getValidator();
//        } catch (Exception exception) {
//            System.err.println("Error setting up validator: " + exception.getMessage());
//        }
//    }
//
//    @Test
//    void validateTask_Success() {
//        // TODO 12: Create a valid Task object and assert no violations
//        Task validTask = new Task("My Task", "Some description", false, LocalDate.now()); // learner should initialize this
//        Set<ConstraintViolation<Task>> violations = validator.validate(validTask);
//
//        assertTrue(violations.isEmpty());
//    }
//
//    @Test
//    void validateTask_Failure_EmptyTitle() {
//        // TODO 12: Create a Task object with empty title and assert violations exist
//        Task invalidTask = new Task("", "Some description", false, LocalDate.now()); // learner should initialize this
//        Set<ConstraintViolation<Task>> violations = validator.validate(invalidTask);
//
//        assertFalse(violations.isEmpty());
//        assertEquals("Title cannot be empty", violations.iterator().next().getMessage());
//    }
//
//}