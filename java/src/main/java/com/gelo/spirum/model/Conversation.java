package com.gelo.spirum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "conversation")
public class Conversation
{
    public Conversation()
    {
    }


    public Conversation(String name, ConversationType type, Set<User> users)
    {
        this.name = name;
        this.type = type;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 6, max = 50)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private ConversationType type;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_conversation",
            joinColumns = {@JoinColumn(name = "CONVERSATION_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    @JsonIgnoreProperties(value = {"friendsTo", "friendsOf", "authorities", "conversations"}, allowSetters = true)
    private Set<User> users = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER
            , cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "conversation_id")
    private Set<Message> messages = new LinkedHashSet<>();


    @Transient
    private Message lastMessage;


    @PostLoad
    public void lastMessageInit()
    {
        lastMessage = messages.stream().max(Comparator.comparing(Message::getCreateDate)).orElse(null);
    }

    public Message getLastMessage()
    {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage)
    {
        this.lastMessage = lastMessage;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }

    public ConversationType getType()
    {
        return type;
    }

    public void setType(ConversationType type)
    {
        this.type = type;
    }

    public Set<Message> getMessages()
    {
        return messages;
    }

    public void setMessages(Set<Message> messages)
    {
        this.messages = messages;
    }
}
