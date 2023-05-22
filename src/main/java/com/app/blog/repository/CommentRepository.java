package com.app.blog.repository;

import java.util.List;

import com.app.blog.model.Post;
import com.app.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.blog.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	public List<Comment> findByPost(Post post);
	
	public void deleteByPost(Post post);

	@Query(nativeQuery = true,value = "SELECT * FROM comment c WHERE c.comment=:string")
	public List<Comment> searchComment(@Param("string") String comment);

	@Query(nativeQuery = true,value = "SELECT * FROM comment c ORDER BY comment")
	public List<Comment> ascComment();
}
