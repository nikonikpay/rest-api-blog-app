package com.ali.restapp.service;

import com.ali.restapp.entity.Post;
import com.ali.restapp.payload.PostDto;
import com.ali.restapp.payload.PostResponse;

import java.util.List;

public interface PostService {


    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePost(long id);


}
