package com.app.blog.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.blog.exception.ResourceNotFoundException;
import com.app.blog.model.Post;
import com.app.blog.service.PostService;

@RestController
public class PostController {

	private static Logger logger = LoggerFactory.getLogger(PostController.class);

	@Autowired
	private PostService postService;

	@GetMapping("/posts")
	public ResponseEntity<List<Post>> retrieveAllPosts(@RequestHeader("user-id") long userId) throws ResourceNotFoundException{
		logger.info("retrieve all posts controller...!");
		List<Post> posts = postService.retrieveAllPosts(userId);
		return new ResponseEntity<>(posts, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/posts/search")
	public ResponseEntity<List<Post>> searchPostByTitle(@RequestParam("searchString") String searchString, @RequestHeader("user-id") long userId) throws ResourceNotFoundException{
		logger.info("search posts by titles...!");
		List<Post> posts = postService.searchPostByTitle(userId, searchString);
		return new ResponseEntity<>(posts, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/posts/summary")
	public ResponseEntity<Map<Long, String>> getPostsSummary(@RequestHeader("user-id") long userId) throws ResourceNotFoundException{
		logger.info("get posts summary...!");
		Map<Long, String> summary = postService.getPostsSummary(userId);
		return new ResponseEntity<>(summary, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/posts/titles")
	public ResponseEntity<Set<String>> retrievePostTitles(@RequestHeader("user-id") long userId) throws ResourceNotFoundException{
		logger.info("retrieve posts titles...!");
		Set<String> titles = postService.retrievePostTitles(userId);
		return new ResponseEntity<>(titles, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<Post> retrievePostById(@PathVariable long id) throws ResourceNotFoundException{

		logger.info("retrieve post controller...!");
		Post post = postService.getPost(id);
		return new ResponseEntity<>(post, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/posts")
	public ResponseEntity<Post> createPost(@RequestHeader("user-id") long userId, @RequestBody Post post) throws ResourceNotFoundException{

		logger.info("create post controller...!");
		Post savedPost = postService.createPost(post, userId);
		return new ResponseEntity<>(savedPost, new HttpHeaders(), HttpStatus.CREATED);
	}

	@DeleteMapping("/posts/{id}")
	public HttpStatus deletePost(@PathVariable long id) throws ResourceNotFoundException{

		logger.info("delete post controller...!");
		postService.deletePost(id);
		return HttpStatus.ACCEPTED;
	}

	@PutMapping("/posts")
	public ResponseEntity<Post> updatePost(@RequestHeader("user-id") long userId, @RequestBody Post post) throws ResourceNotFoundException{

		logger.info("update post controller...!");
		Post updatedPost = postService.updatePost(post, userId);
		return new ResponseEntity<>(updatedPost, new HttpHeaders(), HttpStatus.ACCEPTED);
	}
}
