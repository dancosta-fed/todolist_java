package br.com.crznews.todolist.user;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;
  
  @GetMapping("/")
  public List<UserModel> getAllUsers() {
    return this.userRepository.findAll();
  }

  @SuppressWarnings("rawtypes")
  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel user) {
    UserModel byUserName = this.userRepository.findByUserName(user.getUserName());
    
    if (byUserName != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
    }

    String passwordHashToString = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
    user.setPassword(passwordHashToString);
    
    UserModel createdUser = this.userRepository.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }
}
