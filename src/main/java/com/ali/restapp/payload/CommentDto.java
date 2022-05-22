package com.ali.restapp.payload;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
public class CommentDto  {
    private  long id;

    //should not be null or empty
    @NotEmpty(message="name should not be null or empty")
    private  String name;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private  String email;

    @NotEmpty
    @Size(min =10,message="Comment body must be minimum 10 characters")
    private  String body;



}
