package com.ali.restapp.repository;

import com.ali.restapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PostRepository extends JpaRepository<Post, Long> {


}