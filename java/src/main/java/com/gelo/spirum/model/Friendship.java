package com.gelo.spirum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "friendship",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "FRIEND_ID"})})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONE)
public class Friendship
{
    public Friendship()
    {
    }

    public Friendship(User userRef, User friendRef, Boolean accepted)
    {
        this.userRef = userRef;
        this.friendRef = friendRef;
        this.accepted = accepted;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User userRef;

    @ManyToOne
    @JoinColumn(name = "FRIEND_ID")
    @JsonIgnoreProperties(value = {"friendsTo", "friendsOf", "authorities", "conversations"}, allowSetters = true)
    private User friendRef;

    private Boolean accepted;


    public User getUserRef()
    {
        return userRef;
    }

    public void setUserRef(User userRef)
    {
        this.userRef = userRef;
    }

    public User getFriendRef()
    {
        return friendRef;
    }

    public void setFriendRef(User friendRef)
    {
        this.friendRef = friendRef;
    }

    public Boolean getAccepted()
    {
        return accepted;
    }

    public void setAccepted(Boolean accepted)
    {
        this.accepted = accepted;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friendship that = (Friendship) o;

        if (!userRef.equals(that.userRef)) return false;
        return friendRef.equals(that.friendRef);
    }

    @Override
    public int hashCode()
    {
        int result = userRef.hashCode();
        result = 31 * result + friendRef.hashCode();
        return result;
    }
}
