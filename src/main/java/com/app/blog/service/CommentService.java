package com.app.blog.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.app.blog.exception.ResourceAlreadyExistsException;
import com.app.blog.exception.ResourceNotFoundException;
import com.app.blog.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.blog.model.Comment;
import com.app.blog.model.Post;
import com.app.blog.repository.CommentRepository;

@Service
public class CommentService {

	private static Logger logger = LoggerFactory.getLogger(CommentService.class);

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	public List<Comment> getAllCommentsOfPost(long postId) throws ResourceNotFoundException{
		logger.info("getAllCommentsOfPost {}", this.getClass().getName());
		Optional<Post> post = postRepository.findById(postId);
		if(post.isPresent()) {
			return commentRepository.findByPost(post.get());
		} else {
			logger.error("getAllCommentsOfPost {} Record not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	public List<Comment> searchComments(String searchString) {
		logger.info("searchComments {}", this.getClass().getName());
		return commentRepository.searchComment(searchString);
	}

	public List<Comment> sortedAscComments() {
		logger.info("sortedComments {}", this.getClass().getName());
		return commentRepository.ascComment();
	}

	public Comment addComment(Comment comment, Long id) throws ResourceNotFoundException{
		logger.info("addComment {}", this.getClass().getName());
		Optional<Post> post = postRepository.findById(id);
		if(post.isPresent()) {
			Optional<Comment> com = commentRepository.findById(comment.getId());
			if (com.isPresent()) {
				logger.error("addComment {} Comment already exists!", this.getClass().getName());
				throw new ResourceAlreadyExistsException();
			}
			Timestamp timestamp = new Timestamp(new Date().getTime());
			comment.setTime(timestamp);
			comment.setPost(post.get());
			return commentRepository.save(comment);
		} else {
			logger.error("addComment {} Post not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	public void deleteComment(long id) throws ResourceNotFoundException{
		logger.info("deleteComment {}", this.getClass().getName());
		Optional<Comment> comment = commentRepository.findById(id);
		if (comment.isPresent()) {
			commentRepository.deleteById(id);
		} else {
			logger.error("deleteComment {} Comment not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}

	public Comment updateComment(Comment comment, Long id) throws ResourceNotFoundException{
		logger.info("updateComment {}", this.getClass().getName());
		Optional<Post> post = postRepository.findById(id);
		if(post.isPresent()) {
			Timestamp timestamp = new Timestamp(new Date().getTime());
			comment.setTime(timestamp);
			comment.setPost(post.get());
			return commentRepository.save(comment);
		} else {
			logger.error("updateComment {} Post not found", this.getClass().getName());
			throw new ResourceNotFoundException();
		}
	}
}
