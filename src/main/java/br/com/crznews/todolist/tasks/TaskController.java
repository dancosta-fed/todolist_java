package br.com.crznews.todolist.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/tasks")
public class TaskController {
  
  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")  
  public TaskModel createTask(@RequestBody TaskModel task) {
    TaskModel taskSaved = this.taskRepository.save(task);
    return taskSaved;
  }
}
