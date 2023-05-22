package com.app.blog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.blog.exception.ResourceNotFoundException;
import com.app.blog.model.Comment;
import com.app.blog.service.CommentService;

@RestController
public class CommentController {

	private static Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentService commentService;

	@GetMapping("/comments/search")
	public ResponseEntity<List<Comment>> searchComments(@RequestParam("searchString") String searchString) throws ResourceNotFoundException{
		logger.info("search comments of post ...!");
		List<Comment> comments = commentService.searchComments(searchString);
		return new ResponseEntity<>(comments, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/comments/sorted/asc")
	public ResponseEntity<List<Comment>> sortedAscComments() throws ResourceNotFoundException{
		logger.info("get sorted comments asc ...!");
		List<Comment> comments = commentService.sortedAscComments();
		return new ResponseEntity<>(comments, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/comments/{postId}")
	public ResponseEntity<List<Comment>> retrieveAllCommentsOfPost(@PathVariable long postId) throws ResourceNotFoundException{
		logger.info("retrieve all comments of post ...!");
		List<Comment> comments = commentService.getAllCommentsOfPost(postId);
		return new ResponseEntity<>(comments, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/comments")
	public ResponseEntity<Comment> addComment(@RequestHeader("post-id") long postId, @RequestBody Comment comment) throws ResourceNotFoundException{

		logger.info("add comment controller...!");
		Comment savedComment = commentService.addComment(comment, postId);
		return new ResponseEntity<>(savedComment, new HttpHeaders(), HttpStatus.CREATED);
	}

	@DeleteMapping("/comments/{id}")
	public HttpStatus deleteComment(@PathVariable long id) throws ResourceNotFoundException{

		logger.info("delete comment controller...!");
		commentService.deleteComment(id);
		return HttpStatus.ACCEPTED;
	}

	@PutMapping("/comments")
	public ResponseEntity<Comment> updateComment(@RequestHeader("post-id") long postId, @RequestBody Comment comment) throws ResourceNotFoundException{

		logger.info("update comment controller...!");
		Comment updatedComment = commentService.updateComment(comment, postId);
		return new ResponseEntity<>(updatedComment, new HttpHeaders(), HttpStatus.ACCEPTED);
	}
}
