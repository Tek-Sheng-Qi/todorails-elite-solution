package com.todo.rails.elite.starter.code.controller;

import com.todo.rails.elite.starter.code.model.Task;
import com.todo.rails.elite.starter.code.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * REST controller for handling HTTP requests related to {@link Task} management
 *
 * <p>Delegates business logic to {@link TaskService} and returns appropriate HTTP responses or views.</p>
 *
 * @see TaskService
 * @see Task
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	/**
	 * Retrieves all tasks in the repository
	 *
	 * @return {@code 200 OK} with all tasks, or {@code 500} if an error occurs
	 */
	@GetMapping("/all")
	public ResponseEntity<List<Task>> getAllTasks() {
		logger.info("GET /all - request received to fetch all tasks");
		try {
			return ResponseEntity.ok(taskService.getAllTasks());
		} catch (Exception exception) {
			logger.error("GET /all - failed to retrieve tasks: {}", exception.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Retrieves a task by its unique ID
	 *
	 * @param id the ID of the task to retrieve
	 * @return {@code 200 OK} with the matching task, or {@code 404} if not found
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable(name = "id") Long id) {
		logger.info("GET /{} - request received to fetch task by id", id);
		try {
			return ResponseEntity.ok(taskService.getTaskById(id));
		} catch (Exception exception) {
			logger.error("GET /{} - failed to retrieve task by id: {}", id, exception.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Retrieves a task by its unique title
	 *
	 * @param title the title of the task to retrieve
	 * @return {@code 200 OK} with the matching task, or {@code 404} if not found
	 */
	@GetMapping("/title/{title}")
	public ResponseEntity<Task> getTaskByTitle(@PathVariable(name = "title") String title) {
		logger.info("GET /title/{} - request received to fetch task by title", title);
		try {
			return ResponseEntity.ok(taskService.getTaskByTitle(title));
		} catch (Exception exception) {
			logger.error("GET /title/{} - failed to retrieve task by title: {}", title, exception.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Creates a new task.
	 *
	 * @param task the task to add
	 * @return redirects to {@code /tasks} on success, or back to the add form on failure
	 */
		@RequestMapping(value = "/add", method = RequestMethod.POST)
		public ModelAndView addTask(@ModelAttribute Task task) {
			logger.info("POST /add - request received to add task with title '{}'", task.getTitle());
			try {
				Task addedTask = taskService.addTask(task);
				logger.info("POST /add - task '{}' successfully added with id: {}", addedTask.getTitle(), addedTask.getId());
				return new ModelAndView("redirect:/tasks");
			} catch (Exception exception) {
				logger.error("POST /add - failed to add task with title '{}': {}", task.getTitle(), exception.getMessage());
				return new ModelAndView("redirect:/tasks/add", "task", task);
			}
		}

	/**
	 * Fetches a existing task by ID and returns it in the edit view.
	 *
	 * @param id the ID of the task to update
	 * @return the edit view populated with the task, or throws {@link RuntimeException} if not found
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView updateTask(@PathVariable(name = "id") Long id) {
		logger.info("GET /update/{} - request received to find existing task with id for update", id);
		try {
			Task existingTask = taskService.getTaskById(id);
			return new ModelAndView("edit", "task", existingTask);
		} catch (Exception exception) {
			logger.error("GET /update/{} - failed to fetch task with id {}: {}", id, exception.getMessage());
			throw new RuntimeException("Task not found");
		}
	}

	/**
	 * Updates the fields of an existing task.
	 *
	 * @param task the task containing updated values
	 * @return redirects to {@code /tasks} on success or throws {@link RuntimeException} if not found
	 */
	@PostMapping("/update")
	public ModelAndView updateTask(@ModelAttribute Task task) {
		logger.info("POST /update - request received to update task with title '{}'", task.getTitle());
		try {
			Task updatedTask = taskService.updateTask(task);
			return new ModelAndView("redirect:/tasks", "task", updatedTask);
		} catch (Exception exception) {
			logger.error("POST /update - failed to update task with title {} : {}", task.getTitle(), exception.getMessage());
			throw new RuntimeException("Task not found");
		}
	}

	/**
	 * Marks an existing task as completed.
	 *
	 * @param id the ID of the task to mark as completed
	 * @return redirects to {@code /} on both success and failure
	 */
	@PostMapping("/complete/{id}")
	public ModelAndView completeTask(@PathVariable Long id) {
		logger.info("POST /complete/{} - request received to mark task to completed", id);
		try {
			Task taskById = taskService.getTaskById(id);
			taskById.setCompleted(true);
			taskService.updateTask(taskById);
			return new ModelAndView("redirect:/");
		} catch (Exception exception) {
			logger.error("POST /complete/{} - failed to update task to completed: {}",id, exception.getMessage());
			return new ModelAndView("redirect:/");
		}
	}

	/**
	 * Deletes a task by its unique ID.
	 *
	 * @param id the id of the task to delete
	 * @return redirects to {@code /} on success and failure
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ModelAndView deleteTask(@PathVariable Long id) {
		logger.info("DELETE /delete/{} - request received to delete task by id", id);
		try {
			Task taskById = taskService.getTaskById(id);
			taskService.deleteTask(taskById);
			return new ModelAndView("redirect:/");
		} catch (Exception exception) {
			logger.error("DELETE /delete/{} - failed to delete task with id: {}", id, exception.getMessage());
			return new ModelAndView("redirect:/");
		}
	}

	/**
	 * Retrieves all incomplete tasks
	 * @return {@code 200 OK} with pending tasks or {@code 404} if not found
	 */
	@GetMapping("/pending")
	public ResponseEntity<List<Task>> getPendingTasks() {
		logger.info("GET /pending - request received to retrieve pending tasks");
		try {
			return ResponseEntity.ok(taskService.getPendingTasks());
		} catch (Exception exception) {
			logger.error("GET /pending - failed to retrieve pending tasks: {}", exception.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Retrieves all completed tasks.
	 *
	 * @return {@code 200 OK} with completed tasks or {@code 404} if not found
	 */
	@GetMapping("/completed")
	public ResponseEntity<List<Task>> getCompletedTasks() {
		logger.info("GET /completed - request received to retrieve completed tasks");
		try {
			return ResponseEntity.ok(taskService.getCompletedTasks());
		} catch (Exception exception) {
			logger.error("GET /completed - failed to retrieve completed tasks: {}", exception.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Retrieves all incomplete tasks due today.
	 *
	 * @return {@code 200 OK} with today's pending tasks, or {@code 404} if not found
	 */
	@GetMapping("/today")
	public ResponseEntity<List<Task>> getTodayTasks() {
		logger.info("GET /today - request received to retrieve pending tasks due today");
		try {
			return ResponseEntity.ok(taskService.getTodayTasks());
		} catch (Exception exception) {
			logger.error("GET /today - failed to retrieve pending tasks due today: {}", exception.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
