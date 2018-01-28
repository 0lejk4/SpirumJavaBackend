package com.gelo.spirum.rest;

import com.gelo.spirum.model.Conversation;
import com.gelo.spirum.model.ConversationType;
import com.gelo.spirum.model.Friendship;
import com.gelo.spirum.model.Notification;
import com.gelo.spirum.model.Task;
import com.gelo.spirum.model.User;
import com.gelo.spirum.rest.repository.ConversationsRepository;
import com.gelo.spirum.rest.repository.FriendsRepository;
import com.gelo.spirum.rest.repository.UserRepository;
import com.gelo.spirum.rest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;


@RestController
public class PersonController
{


    private final UserService userService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final FriendsRepository friendsRepository;

    private final UserRepository mUserRepository;

    private final ConversationsRepository mConversationsRepository;

    @Autowired
    public PersonController(UserService userService, SimpMessagingTemplate simpMessagingTemplate, FriendsRepository friendsRepository, UserRepository userRepository, ConversationsRepository conversationsRepository)
    {
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.friendsRepository = friendsRepository;
        mUserRepository = userRepository;
        mConversationsRepository = conversationsRepository;
    }

    @PostMapping(value = "/task")
    public void addTask(@RequestBody Set<Task> tasks, Principal principal){
        User user = userService.findByUsername(principal.getName());
        Set<Task> tasksU = user.getTasks();
        tasksU.clear();
        tasksU.addAll(tasks);
        mUserRepository.save(user);
    }


    @GetMapping(value = "/user/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username)
    {
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/friendD/{username}")
    public ResponseEntity<User> deleteFriend(Principal principal, @PathVariable String username){
        User user = userService.findByUsername(principal.getName());
        User friend = userService.findByUsername(username);
        friendsRepository.delete(friendsRepository.findByUserRefAndFriendRef(user,friend));
        friendsRepository.delete(friendsRepository.findByUserRefAndFriendRef(friend,user));
        Conversation conv = mConversationsRepository.findByTypeAndAndName(ConversationType.TWO_USERS,username + "|" + principal.getName());
        if(conv == null)conv = mConversationsRepository.findByTypeAndAndName(ConversationType.TWO_USERS, principal.getName() + "|" + username);
        mConversationsRepository.delete(conv);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/friendA/{username}")
    public ResponseEntity<User> answerFriendInvite(@RequestBody Answer answer, Principal principal, @PathVariable String username)
    {
        User user = userService.findByUsername(principal.getName());
        User inviter = userService.findByUsername(username);
        Set<Friendship> friends = user.getFriendsTo();
        Friendship answererAsFriend = friends.stream().filter(i -> i.getFriendRef().getUsername().equals(username)).findFirst().orElse(null);
        if (answererAsFriend != null && !answererAsFriend.getAccepted() && answer.answer)
        {
            Friendship inviterAsFriend = inviter.getFriendsTo().stream().filter(i -> i.getFriendRef().getUsername().equals(principal.getName())).findFirst().orElse(null);
            if(inviterAsFriend != null){
                inviterAsFriend.setAccepted(true);
                friendsRepository.save(inviterAsFriend);
            }else
            {
                inviter.getFriendsTo().add(new Friendship(inviter, user, true));
            }
            answererAsFriend.setAccepted(true);
            String unique = username + "|" + principal.getName();
            Conversation withTwo = new Conversation(unique, ConversationType.TWO_USERS, new LinkedHashSet<>(Arrays.asList(
                    user, inviter
            )));

            mConversationsRepository.save(withTwo);
            user.getConversations().add(withTwo);
            inviter.getConversations().add(withTwo);
            mUserRepository.save(user);
            mUserRepository.save(inviter);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping(value = "/friendI/{username}")
    public ResponseEntity<User> inviteFriend(Principal principal, @PathVariable String username)
    {
        User user = userService.findByUsername(username);
        User userInviter = userService.findByUsername(principal.getName());
        Set<Friendship> invites = user.getFriendsTo();
        Friendship inviterAsFriend = invites.stream().filter(i -> i.getFriendRef().getUsername().equals(principal.getName())).findFirst().orElse(null);

        if (inviterAsFriend == null)
        {
            inviterAsFriend = new Friendship(user, userInviter, false);
            invites.add(inviterAsFriend);
            user.setFriendsTo(invites);
            friendsRepository.save(inviterAsFriend);


            simpMessagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/notifications",
                    new Notification("info", "Got friend invite from " + principal.getName() + "\nAnswer as soon as possible!", "Friend invite")
            );
        }
        return new ResponseEntity<>(user, HttpStatus.OK);

    }


    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> getOneUserById(@PathVariable long id)
    {
        User user = userService.findOne(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    static class Answer
    {
        Answer()
        {
        }

        private Boolean answer;

        public Boolean getAnswer()
        {
            return answer;
        }

        public void setAnswer(Boolean answer)
        {
            this.answer = answer;
        }
    }

}
