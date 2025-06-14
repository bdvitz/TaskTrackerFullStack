package org.todo.todorails.service;

// Import statements
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.todo.todorails.model.Task;
import org.todo.todorails.model.User;
import org.todo.todorails.repository.TaskRepository;
import org.todo.todorails.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @Service marks this class as a Spring Service component.
 * It tells Spring that this class contains business logic and should be managed by the Spring container.
 * Services are typically used to define operations that interact with repositories (data access) and control flow.
 */
@Service
public class TaskServiceAnnotated {

    /**
     * @Autowired tells Spring to automatically inject the required dependencies (beans) into this class.
     * In this case, Spring will inject instances of TaskRepository and UserRepository.
     */
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Saves a new task for the currently logged-in user.
     */
    public Task saveTask(Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        task.setUser(user);                    // Assign the task to the current user
        task.setDateAdded(LocalDate.now());   // Set current date
        return taskRepository.save(task);     // Save to database
    }

    /**
     * Returns today's tasks for the current user that are not marked as completed.
     */
    public List<Task> getTodayTasksForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        LocalDate currentDate = LocalDate.now();

        // Query for tasks due today and not completed
        return taskRepository.findByUserAndDueDateAndCompleted(user, currentDate, false);
    }

    /**
     * Returns all tasks for the current user.
     */
    public List<Task> getAllTasksForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        return taskRepository.findByUser(user);
    }

    /**
     * Marks the given task as completed, if it belongs to the current user and isn't already done.
     */
    public boolean markTaskAsDone(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Task task = taskRepository.findByUserAndId(user, taskId);

        if (task != null && !task.isCompleted()) {
            task.setCompleted(true);                        // Mark as done
            task.setCompletionDate(LocalDate.now());        // Set completion date
            taskRepository.save(task);
            return true;
        }

        return false; // Task doesn't exist or already completed
    }

    /**
     * Retrieves a task by its ID, only if it's incomplete and belongs to the current user.
     */
    public Task getTaskById(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Task task = taskRepository.findByUserAndId(user, taskId);

        return (task != null && !task.isCompleted()) ? task : null;
    }

    /**
     * Retrieves a task by ID for the current user, regardless of whether it's completed or not.
     */
    public Task getTaskByIdAny(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        return taskRepository.findByUserAndId(user, taskId);
    }

    /**
     * Updates a task's details, only if the logged-in user is the owner of the task.
     */
    public boolean updateTaskForUser(Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Task taskInDb = taskRepository.getById(task.getId());

        // Ensure the user updating is the task owner
        if (user != null && !user.getUsername().equals(taskInDb.getUser().getUsername())) {
            return false;
        }

        Task existingTask = taskRepository.findByUserAndId(user, task.getId());
        if (existingTask != null) {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setPriority(task.getPriority());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setType(task.getType());
            existingTask.setDateAdded(LocalDate.now());

            return taskRepository.save(existingTask) != null;
        }

        return false;
    }

    /**
     * Deletes a task if it belongs to the currently authenticated user.
     */
    public boolean deleteTask(Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Task taskInDb = taskRepository.getById(task.getId());

        // Check if task belongs to user
        if (user != null && !user.getUsername().equals(taskInDb.getUser().getUsername())) {
            return false;
        }

        Task existingTask = taskRepository.findByUserAndId(task.getUser(), task.getId());
        if (existingTask != null) {
            taskRepository.delete(existingTask);
            return true;
        }

        return false;
    }

    /**
     * Counts the number of tasks based on whether they are completed or not.
     */
    public int countByCompleted(boolean completedStatus) {
        return this.taskRepository.countByCompleted(completedStatus);
    }
}
