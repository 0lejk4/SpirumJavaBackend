package com.gelo.spirum.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.rest.core.config.Projection;

import java.util.List;
import java.util.Set;

@Projection(name = "post", types = {Post.class})
public interface PostProj
{
    Long getId();
    @JsonIgnoreProperties(value = {"friendsTo", "friendsOf", "authorities", "conversations"}, allowSetters = true)
    User getUser();

    String getMessage();

    List<String> getUsers();

    Set<Comment> getComments();
}
