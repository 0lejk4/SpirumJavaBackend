package com.gelo.spirum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name = "user", types = {User.class})
interface UserRestData
{
    Long getId();

    String getUsername();

    String getFirstname();

    String getLastname();

    String getEmail();

    Boolean getEnabled();

    @JsonIgnoreProperties(value = "users", allowSetters = true)
    List<Authority> getAuthorities();

    List<Friendship> getFriendsTo();

}
