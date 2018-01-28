package com.gelo.spirum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "userEntity")
public class User
{

    public User()
    {
    }

    public User(String username, String password, String firstname, String lastname, String email, Boolean enabled, Date lastPasswordResetDate, Set<Authority> authorities, String profile_pic )
    {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;
        this.profile_pic = profile_pic;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column( length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Column( length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column( length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String firstname;

    @Column(length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String lastname;

    @Column( length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Set<Authority> authorities = new LinkedHashSet<>();

    private String profile_pic;

    @OneToMany(mappedBy = "userRef",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnoreProperties(value = "conversations", allowSetters = true)
    private Set<Friendship> friendsTo = new LinkedHashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "friendRef",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Friendship> friendsOf = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"messages", "users"}, allowSetters = true)
    private Set<Conversation> conversations = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "user_task", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "task_id") })
    private Set<Task> tasks = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    public Set<Task> getTasks()
    {
        return tasks;
    }

    public void setTasks(Set<Task> tasks)
    {
        this.tasks = tasks;
    }

    public Set<Authority> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities)
    {
        this.authorities = authorities;
    }

    public Set<Friendship> getFriendsTo()
    {
        return friendsTo;
    }

    public void setFriendsTo(Set<Friendship> friendsTo)
    {
        this.friendsTo = friendsTo;
    }

    public Set<Friendship> getFriendsOf()
    {
        return friendsOf;
    }

    public void setFriendsOf(Set<Friendship> friendsOf)
    {
        this.friendsOf = friendsOf;
    }

    public Set<Conversation> getConversations()
    {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations)
    {
        this.conversations = conversations;
    }

    public String getProfile_pic()
    {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic)
    {
        this.profile_pic = profile_pic;
    }


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }


    public Date getLastPasswordResetDate()
    {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate)
    {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
}