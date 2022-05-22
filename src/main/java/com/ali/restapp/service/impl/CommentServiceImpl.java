package com.ali.restapp.service.impl;

import com.ali.restapp.entity.Comment;
import com.ali.restapp.entity.Post;
import com.ali.restapp.exception.BlogAPIException;
import com.ali.restapp.exception.ResourceNotFoundException;
import com.ali.restapp.payload.CommentDto;
import com.ali.restapp.repository.CommentRepository;
import com.ali.restapp.repository.PostRepository;
import com.ali.restapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);


        //retrieve post entity byy id
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        //set post to comment entity
        comment.setPost(post);

        //save comment entity to db
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);


    }


    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comment by id
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert list of comment entities to list of comment dto's
        return comments.stream()
                       .map(this :: mapToDto)
                       .collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Comment comment = getComment(postId, commentId);

        return mapToDto(comment);

    }


    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        Comment comment = getComment(postId, commentId);

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody((commentRequest.getBody()));

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);

    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = getComment(postId, commentId);

        commentRepository.delete(comment);

    }

    private Comment getComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));


        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost()
                   .getId()
                   .equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belonmg to the post");
        }
        return comment;
    }



    private CommentDto mapToDto(Comment comment) {
        return mapper.map(comment,CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto) {
        return mapper.map(commentDto,Comment.class);
    }

}
