package com.app.blog.controller;

import com.app.blog.exception.ResourceAlreadyExistsException;
import com.app.blog.exception.ResourceNotFoundException;
import com.app.blog.model.User;
import com.app.blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public ResponseEntity<List<User>> retrieveAllUsers() throws ResourceNotFoundException{
		logger.info("retrieve all users controller...!");
		List<User> users = userService.getAllUsers();
		return new ResponseEntity<>(users, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> retrieveUserById(@PathVariable long id) throws ResourceNotFoundException{

		logger.info("retrieve user controller...!");
		User user = userService.getUser(id);
		return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) throws ResourceAlreadyExistsException{

		logger.info("create user controller...!");
		User savedUser = userService.createUser(user);
		return new ResponseEntity<>(savedUser, new HttpHeaders(), HttpStatus.CREATED);
	}

	@DeleteMapping("/users/{id}")
	public HttpStatus deleteUser(@PathVariable long id) throws ResourceNotFoundException{

		logger.info("delete user controller...!");
		userService.deleteUser(id);
		return HttpStatus.ACCEPTED;
	}

	@PutMapping("/users")
	public ResponseEntity<User> updateUser(@RequestBody User user) throws ResourceNotFoundException{

		logger.info("update user controller...!");
		User updatedUser = userService.updateUser(user);
		return new ResponseEntity<>(updatedUser, new HttpHeaders(), HttpStatus.ACCEPTED);
	}
}
