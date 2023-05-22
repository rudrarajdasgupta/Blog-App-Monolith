package com.app.blog.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.app.blog.exception.ResourceAlreadyExistsException;
import com.app.blog.model.User;
import com.app.blog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.blog.exception.ResourceNotFoundException;
import com.app.blog.model.Post;
import com.app.blog.repository.CommentRepository;
import com.app.blog.repository.PostRepository;

@Service
public class PostService {

	private static Logger logger = LoggerFactory.getLogger(PostService.class);
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserRepository userRepository;

	public List<Post> retrieveAllPosts(Long id) throws ResourceNotFoundException{
		logger.info("retrieveAllPosts {}", this.getClass().getName());
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return postRepository.findByUser(user.get());
		} else {
			logger.error("retrieveAllPosts {} Record not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	public List<Post> searchPostByTitle(Long id, String searchString) throws ResourceNotFoundException{
		logger.info("searchPostByTitle {}", this.getClass().getName());
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			List<Post> postList = postRepository.findByUser(user.get());
			return postList.stream().filter(e -> e.getTitle().contains(searchString)).collect(Collectors.toList());
			
		} else {
			logger.error("searchPostByTitle {}  Record not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	public Map<Long, String> getPostsSummary(Long id) throws ResourceNotFoundException{
		logger.info("getPostsSummary {}", this.getClass().getName());
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			List<Post> postList = postRepository.findByUser(user.get());
			return postList.stream().collect(Collectors.toMap(Post::getId, p -> p.getTitle() + " " + p.getDescription()));
		} else {
			logger.error("getPostsSummary {}", this.getClass().getName() + " Record not found");
			throw new ResourceNotFoundException();
		}
	}

	public Set<String> retrievePostTitles(Long id) throws ResourceNotFoundException{
		logger.info("retrievePostTitles {}", this.getClass().getName());
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			List<Post> posts = postRepository.findByUser(user.get());
			return posts.stream().map(Post::getTitle).collect(Collectors.toSet());
		} else {
			logger.error("retrievePostTitles {} Record not found ", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	public Post getPost(long id) throws ResourceNotFoundException{
		Optional<Post> post = postRepository.findById(id);
		if(post.isPresent()) {
			return post.get();
		} else {
			logger.error("retrievePostTitles {} Record not found ", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	@Transactional
	public Post createPost(Post post, Long id) throws ResourceNotFoundException{
		logger.info("createPost {}", this.getClass().getName());
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			Optional<Post> p = postRepository.findById(id);
			if (p.isPresent()) {
				logger.error("createPost {} Record already exists", this.getClass().getName());
				throw new ResourceAlreadyExistsException();
			}
			post.setUser(user.get());
			return postRepository.save(post);
		} else {
			logger.error("createPost {} Record not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}
	
	@Transactional
	public void deletePost(long id) throws ResourceNotFoundException{
		logger.info("deletePost {}", this.getClass().getName());
		Optional<Post> post = postRepository.findById(id);
		if (post.isPresent()) {
			commentRepository.deleteByPost(post.get());
			postRepository.deleteById(id);
		} else {
			logger.error("deletePost {} Record not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	@Transactional
	public Post updatePost(Post post, Long id) throws ResourceNotFoundException{
		logger.info("updatePost {}", this.getClass().getName());
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			post.setUser(user.get());
			return postRepository.save(post);
		} else {
			logger.error("updatePost {} Record not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}
}
