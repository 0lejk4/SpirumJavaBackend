package com.gelo.spirum.rest;


import com.gelo.spirum.model.Conversation;
import com.gelo.spirum.model.ConversationType;
import com.gelo.spirum.model.Message;
import com.gelo.spirum.model.User;
import com.gelo.spirum.rest.repository.ConversationsRepository;
import com.gelo.spirum.rest.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.Date;

import javax.validation.Valid;

@Validated
@RestController
public class ConversationsController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ConversationsRepository conversationsRepository;

    @MessageMapping("/message")
    @SendTo("/topic/greetings")
    public Message produceMessage(Message message, Principal principal)
    {
        User user = userRepository.findByUsername(principal.getName());
        message.setCreateDate(new Date());
        message.setUser(user);
        return message;
    }



    @MessageMapping("/ConversationMessage/{conversationId}")
    public void chatMessage(@DestinationVariable("conversationId") Long conversationId,Message message, Principal principal)
    {
        if(message.getText().length() < 250)
        {
            User user = userRepository.findByUsername(principal.getName());
            Conversation conversation = conversationsRepository.findOne(conversationId);
            Message mess = new Message(message.getText(), user);
            conversation.getMessages().add(mess);
            conversationsRepository.save(conversation);
            simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversationId, mess);
        }
    }


    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable Long conversationId, Principal principal)
    {
        User user = userRepository.findByUsername(principal.getName());
        Conversation conversation = conversationsRepository.findOne(conversationId);
        if (conversation == null && !conversation.getUsers().contains(user))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @PostMapping(value = "/conversation")
    public  ResponseEntity<User> createConversation(Principal principal, @RequestBody @Valid Conversation conversation, Errors errors) throws MethodArgumentNotValidException
    {
        if(!errors.hasErrors())
        {
            User user = userRepository.findByUsername(principal.getName());
            System.out.println("Started");
            conversation.setType(ConversationType.MANY_USERS);
            conversationsRepository.save(conversation);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
