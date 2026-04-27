package com.todo.rails.elite.starter.code.service;

import com.todo.rails.elite.starter.code.model.Task;
import com.todo.rails.elite.starter.code.repository.TaskRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing {@link Task} business logic in the application.
 *
 * <p>Acts as a intermediary between the presentation layer and the {@link TaskRepository},
 * providing operations for creating, retrieving, updating, and deleting tasks,
 * as well as filtering tasks by completion status and due date.
 * </p>
 *
 * @see Task
 * @see TaskRepository
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Creates and persists a new task.
     *
     * @param task the task to add; must not be {@code null}
     * @return the saved task {@link Task}
     * @throws RuntimeException if a task with the same title already exists
     */
    public Task addTask(@NotNull(message = "Task cannot be null") Task task) throws RuntimeException {
        logger.info("Attempting to add task with title '{}'",task.getTitle());
        if (taskRepository.findByTitle(task.getTitle()).isPresent()) {
            logger.warn("Task creation failed - title already exists: '{}'", task.getTitle());
            throw new RuntimeException("Task already exists");
        }
        Task savedTask = taskRepository.save(task);
        logger.info("Task successfully added with id: {}", savedTask.getId());
        return savedTask;
    }

    /**
     * Retrieves a task by its unique id.
     *
     * @param id the id of the task; must not be {@code null}
     * @return the matching {@link Task}
     * @throws RuntimeException if no task is found with the given id
     */
    public Task getTaskById(@NotNull(message = "Id cannot be null") Long id) throws RuntimeException {
        logger.info("Fetching task with id: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(
                        () -> {
                                logger.warn("Task not found with id: {}", id);
                            return new RuntimeException("Task not found");
                        }
                );
    }

    /**
     * Retrieves a task by its unique title.
     *
     * @param title the title of the task; must not be {@code null} or {@code blank}
     * @return the matching {@link Task}
     * @throws RuntimeException if no task is found with the given title
     */
    public Task getTaskByTitle(
            @NotNull(message = "Title cannot be null")
            @NotBlank(message = "Title cannot be blank")
            String title
    ) throws RuntimeException {
        logger.info("Fetching task with title '{}'",title);
        return taskRepository.findByTitle(title)
                .orElseThrow(
                        () -> {
                            logger.warn("Task not found with title '{}'", title);
                            return new RuntimeException("Task not found");
                        }
                );
    }

    /**
     * Retrieves all tasks in the repository.
     *
     * @return a {@link List} of all tasks, or an empty list if none exist
     */
    public List<Task> getAllTasks() {
        logger.info("Retrieving all tasks in repository");

        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            logger.warn("No tasks found in repository");
            return List.of();
        }
        logger.info("Retrieved {} task(s) from repository", tasks.size());
        return tasks;
    }

    /**
     * Updates the fields of  an existing task matched by title.
     * @param task the task containing the fields to be updated; must not be {@code null}
     * @return the updated and persisted {@link Task}
     * @throws RuntimeException if no task is found with the given title
     */
    public Task updateTask(@NotNull(message = "Task cannot be null") Task task) throws RuntimeException {
        logger.info("Retrieving task with id: '{}' for update", task.getId());

        Optional<Task> existingTask = taskRepository.findById(task.getId());
        if (existingTask.isEmpty()) {
            logger.warn("Task with id '{}' is not found", task.getId());
            throw new RuntimeException("Task not found");
        }
        Task taskToUpdate = existingTask.get();
        logger.debug("Updating fields -title: '{}', description: '{}', completed: {}, dueDate: {}", task.getTitle(),
                task.getDescription(), task.isCompleted(), task.getDueDate());
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setCompleted(task.isCompleted());
        taskToUpdate.setDueDate(task.getDueDate());

        Task savedTask = taskRepository.save(taskToUpdate);
        logger.info("Task with id: '{}' successfully saved", task.getId());
        return savedTask;
    }

    /**
     * Deletes an existing task matched by title.
     *
     * @param task the task to be deleted; must not be {@code null}
     * @throws RuntimeException if no task is found with the given title
     */
    public void deleteTask(@NotNull(message = "Task cannot be null") Task task) throws RuntimeException {
        logger.info("Retrieving task with title '{}' for deletion", task.getTitle());

        Optional<Task> existingTask = taskRepository.findByTitle(task.getTitle());
        if (existingTask.isEmpty()) {
            logger.warn("Task with title '{}' not found", task.getTitle());
            throw new RuntimeException("Task not found");
        }
        taskRepository.delete(task);
        logger.info("Task with title '{}' deleted successfully", task.getTitle());
    }

    /**
     * Retrieve all tasks that have not been completed yet.
     *
     * @return {@link List} of pending tasks, or an empty list if none exist
     */
        public List<Task> getPendingTasks() {
            logger.info("Retrieving tasks that have not been completed.");
            List<Task> allTasks = getAllTasks();
            if (allTasks.isEmpty()) {
                logger.warn("No tasks found in repository");
                return List.of();
            }

            List<Task> pendingTasks = allTasks.stream()
                    .filter(task -> !task.isCompleted())
                    .toList();
            if (pendingTasks.isEmpty()) {
                logger.warn("No pending tasks found");
                return List.of();
            }
            logger.info("Retrieved {} pending tasks", pendingTasks.size());
            return pendingTasks;
        }

    /**
     * Retrieve all tasks that have been marked as completed.
     *
     * @return a {@link List} of tasks that have been completed or an empty list if none exist
     */
    public List<Task> getCompletedTasks() {
        logger.info("Retrieving all completed tasks");

        List<Task> allTasks = getAllTasks();
        if (allTasks.isEmpty()) {
            logger.warn("No tasks found in repository");
            return List.of();
        }

        List<Task> completedTasks = allTasks.stream()
                .filter(Task::isCompleted)
                .toList();

        if (completedTasks.isEmpty()) {
            logger.warn("No completed tasks found");
            return List.of();
        }
        logger.info("Retrieved {} completed tasks", completedTasks.size());
        return completedTasks;
    }

    /**
     * Retrieve all incomplete tasks that is due today.
     *
     * @return a {@link List} of incomplete tasks whose due date is today or an empty list if none exist
     */
    public List<Task> getTodayTasks() {
        logger.info("Retrieving all incomplete tasks due today");
        List<Task> allTasks = getAllTasks();
        if (allTasks.isEmpty()) {
            logger.warn("No tasks found in repository");
            return List.of();
        }
        List<Task> todayTasks = allTasks.stream()
                .filter(
                        task -> !task.isCompleted()
                )
                .filter(
                        task -> task.getDueDate()
                                .isEqual(LocalDate.now())
                )
                .toList();

        if (todayTasks.isEmpty()) {
            logger.warn("No incomplete tasks due today found");
            return List.of();
        }

        logger.info("Retrieved {} incomplete tasks due today", todayTasks.size());
        return todayTasks;
    }
}
