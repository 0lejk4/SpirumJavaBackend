package com.gelo.spirum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by olejk4 on 17.10.17.
 */
@Entity
@Table(name = "post")
public class Post
{
    public Post()
    {
    }

    public Post(User user, String message, List<String> users, Set<Comment> comments)
    {
        this.user = user;
        this.message = message;
        this.users = users;
        this.comments = comments;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"friendsTo", "friendsOf", "authorities", "conversations"}, allowSetters = true)
    private User user;

    @NotBlank
    @Size(min=1, max = 250)
    private String message;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "post_like", joinColumns = @JoinColumn(name = "post_id"))
    private List<String> users = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Comment> comments = new LinkedHashSet<>();


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<String> getUsers()
    {
        return users;
    }

    public void setUsers(List<String> users)
    {
        this.users = users;
    }

    public Set<Comment> getComments()
    {
        return comments;
    }

    public void setComments(Set<Comment> comments)
    {
        this.comments = comments;
    }
}
