package com.app.blog.service;

import com.app.blog.exception.ResourceAlreadyExistsException;
import com.app.blog.exception.ResourceNotFoundException;
import com.app.blog.model.Post;
import com.app.blog.model.User;
import com.app.blog.repository.CommentRepository;
import com.app.blog.repository.PostRepository;
import com.app.blog.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;


	public List<User> getAllUsers() throws ResourceNotFoundException{
		List<User> users = userRepository.findAll();
		if(users != null) {
			logger.info("getAllUsers {}", this.getClass().getName());
			return users;
		} else {
			logger.error("getAllUsers {} User not Found!", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	public User getUser(Long id) throws ResourceNotFoundException{
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			logger.info("getUser {}", this.getClass().getName());
			return user.get();
		} else {
			logger.error("getUser {} User not Found!", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	public User createUser(User user) throws ResourceAlreadyExistsException {
		if (user.getId() != null) {
			Optional<User> u = userRepository.findById(user.getId());
			if (u.isPresent()) {
				logger.error("createUser {} User already exists!", this.getClass().getName());
				throw new ResourceAlreadyExistsException();
			}
		}
		logger.info("createUser {}", this.getClass().getName());
		return userRepository.save(user);
	}

	@Transactional
	public void deleteUser(Long id) throws ResourceNotFoundException{
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			logger.info("deleteUser {}", this.getClass().getName());
			List<Post> posts = postRepository.findByUser(user.get());
			for(Post post: posts) {
				commentRepository.deleteByPost(post);
			}
			postRepository.deleteByUser(user.get());
			userRepository.deleteById(id);
		} else {
			throw new ResourceNotFoundException();
		}
	}

	@Transactional
	public User updateUser(User user) throws ResourceNotFoundException{
		Optional<User> u = userRepository.findById(user.getId());
		if(u.isPresent()) {
			logger.info("updateUser {}", this.getClass().getName());
			return userRepository.save(user);
		} else {
			logger.error("updateUser {} User not Found!", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}
	
}
