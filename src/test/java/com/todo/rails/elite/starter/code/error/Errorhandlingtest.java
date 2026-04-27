package com.todo.rails.elite.starter.code.error;

import com.todo.rails.elite.starter.code.model.Task;
import com.todo.rails.elite.starter.code.repository.TaskRepository;
import com.todo.rails.elite.starter.code.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class Errorhandlingtest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // TODO: Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
        assertEquals("Task not found", exception.getMessage());
        // TODO: Mock repository to simulate task not found
        // TODO: Assert that appropriate exception is thrown
    }

    @Test
    void addTask_AlreadyExists() {
        Task sampleTask = new Task("Some title", "Some description", false, LocalDate.now());
        when(taskRepository.findByTitle("Some title")).thenReturn(Optional.of(sampleTask));
        Exception exception = assertThrows(RuntimeException.class, () -> taskService.addTask(sampleTask));
        assertEquals("Task already exists", exception.getMessage());
    }

    @Test
    void getTaskByTitle_NotFound() {
        when(taskRepository.findByTitle("Some title")).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> taskService.getTaskByTitle("Some title"));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void updateTask_NotFound() {
        Task sampleTask = new Task("Some title", "Some description", false, LocalDate.now());
        sampleTask.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> taskService.updateTask(sampleTask));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void deleteTask_NotFound() {
        Task sampleTask = new Task("Some title", "Some description", false, LocalDate.now());
        when(taskRepository.findByTitle("Some title")).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> taskService.deleteTask(sampleTask));
        assertEquals("Task not found", exception.getMessage());
    }

    // TODO: Add more tests for other error scenarios
}