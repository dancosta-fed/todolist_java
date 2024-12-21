package br.com.crznews.todolist.tasks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/tasks")
public class TaskController {
  
  @Autowired
  private ITaskRepository taskRepository;

  @SuppressWarnings("rawtypes")
  @PostMapping("/")  
  public ResponseEntity createTask(@RequestBody TaskModel task, HttpServletRequest request) {
    Object userId = request.getAttribute("userId");
    task.setUserId((UUID) userId);

    LocalDateTime currentDate = LocalDateTime.now();

    if (currentDate.isAfter(task.getStartAt()) || task.getStartAt().isAfter(task.getEndAt())) {
      String startAtBeforeCurrentDate = "The date of the task cannot be in the past.";
      String endAtBeforeStartAt = "The end date cannot be before the start date.";

      String message = currentDate.isAfter(task.getStartAt()) ? startAtBeforeCurrentDate : endAtBeforeStartAt;

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(message);
    }

    TaskModel taskSaved = this.taskRepository.save(task);
    return ResponseEntity.status(HttpStatus.OK).body(taskSaved);
  }

  @GetMapping("/")
  public List<TaskModel> listTasks(HttpServletRequest request) {
    Object userId = request.getAttribute("userId");

    if (userId == null) {
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      return null;
    }
    return this.taskRepository.findByUserId((UUID) userId);
  }
}
