package com.ali.restapp.service.impl;

import com.ali.restapp.entity.Post;
import com.ali.restapp.exception.ResourceNotFoundException;
import com.ali.restapp.payload.PostDto;
import com.ali.restapp.payload.PostResponse;
import com.ali.restapp.repository.PostRepository;
import com.ali.restapp.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ListIterator;

import static java.util.stream.Collectors.toList;


@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;


    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        //convert dto to entity
        Post post = mapToEntity(postDto);

        Post newPost = postRepository.save(post);

        PostDto postResponse = mapToDto(newPost);

        return postResponse;

    }


    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy)
                                                                              .ascending() : Sort.by(sortBy)
                                                                                                 .descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        //get content from  page objects
        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content = listOfPosts.stream()
                                           .map(this :: mapToDto)
                                           .collect(toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPage(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        //get post by id from database
        Post post = postRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(post.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);

    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    // Convert entity into Dto
    private PostDto mapToDto(Post post) {

        return mapper.map(post,PostDto.class);

    }


    private Post mapToEntity(PostDto postDto) {

        return mapper.map(postDto,Post.class);

//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());

    }


}
