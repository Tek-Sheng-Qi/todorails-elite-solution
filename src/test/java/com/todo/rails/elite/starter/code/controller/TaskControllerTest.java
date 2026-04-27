package com.todo.rails.elite.starter.code.controller;

import com.todo.rails.elite.starter.code.model.Task;
import com.todo.rails.elite.starter.code.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.print.attribute.standard.Media;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private TaskController taskController;

    private Task sampleTask;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        sampleTask = new Task("Some title", "Some description", false, LocalDate.now());
    }

    @Test
    void getAllTasks_Success() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(sampleTask));
        mockMvc.perform(get("/api/tasks/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Some title"))
                .andExpect(jsonPath("$[0].description").value("Some description"));
    }

    @Test
    void addTask_Success() throws Exception {
        when(taskService.addTask(any(Task.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks/add")
                .param("title", "Some title")
                .param("description", "Some description")
                .param("completed", "false")
                .param("dueDate", LocalDate.now().toString()))
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    void updateTask_Success() throws Exception {
        when(taskService.updateTask(any(Task.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks/update")
                .param("title", "Updated title")
                .param("description", "Updated description")
                .param("completed", "false")
                .param("dueDate", LocalDate.now().toString()))
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    void deleteTask_Success() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(sampleTask);
        doNothing().when(taskService).deleteTask(any(Task.class));

        mockMvc.perform(delete("/api/tasks/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
