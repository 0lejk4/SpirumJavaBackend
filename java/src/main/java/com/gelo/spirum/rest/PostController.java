package com.gelo.spirum.rest;


import com.gelo.spirum.model.AuthorityName;
import com.gelo.spirum.model.Comment;
import com.gelo.spirum.model.Post;

import com.gelo.spirum.model.User;
import com.gelo.spirum.rest.repository.CommentRepository;
import com.gelo.spirum.rest.repository.ConversationsRepository;
import com.gelo.spirum.rest.repository.PostRepository;
import com.gelo.spirum.rest.repository.UserRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;


@RestController
public class PostController
{
    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ConversationsRepository conversationsRepository;
    //ToDO : conversations, profiles


    @PostMapping(value = "/post/{id}/comment")
    public ResponseEntity<Comment> addComment(@PathVariable long id, @RequestBody @Valid Comment comment, Errors errors, Principal principal)throws MethodArgumentNotValidException
    {
        User user = userRepository.findByUsername(principal.getName());
        if(errors.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{

            comment.setUser(user);
            comment.setPost(postRepository.findOne(id));
            commentRepository.save(comment);
        }
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping(value = "/post/{id}/comment/{cid}")
    public ResponseEntity deleteComment(@PathVariable long id,@PathVariable long cid, Principal principal)throws MethodArgumentNotValidException
    {
        Comment comment = commentRepository.findOne(cid);
        User user = userRepository.findByUsername(principal.getName());
        if(comment == null || !comment.getUser().equals(user) || user.getAuthorities().stream().noneMatch(a -> a.getName() == AuthorityName.ROLE_ADMIN)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            commentRepository.delete(comment);
        }
        return new ResponseEntity( HttpStatus.OK);
    }

    @PutMapping(value = "/post/{id}/comment/{cid}")
    public ResponseEntity<Comment> updateComment(@PathVariable long id,@PathVariable long cid,@RequestBody @Valid Comment comment,Errors errors, Principal principal)throws MethodArgumentNotValidException
    {
        Comment comment_db = commentRepository.findOne(cid);
        User user = userRepository.findByUsername(principal.getName());
        if(comment_db == null || !comment_db.getUser().equals(user) || errors.hasErrors() || user.getAuthorities().stream().noneMatch(a -> a.getName() == AuthorityName.ROLE_ADMIN)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            comment_db.setComment(comment.getComment());
            commentRepository.save(comment_db);
        }
        return new ResponseEntity<>(comment_db, HttpStatus.OK);
    }

    @PutMapping(value = "/post/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable long id, @RequestBody Post post, Principal principal)
    {
        User user = userRepository.findByUsername(principal.getName());
        Post postFromDB = postRepository.findOne(id);
        if (post != null && (postFromDB.getUser().getUsername().equals(principal.getName()) || user.getAuthorities().stream().anyMatch(a -> a.getName() == AuthorityName.ROLE_ADMIN)))
        {
            postFromDB.setMessage(post.getMessage());
            postRepository.save(postFromDB);
            return new ResponseEntity<>(postFromDB, HttpStatus.OK);
        }
        return new ResponseEntity<>(postFromDB, HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/post")
    public ResponseEntity<Post> addPost(@RequestBody @Valid Post post, Errors errors)throws MethodArgumentNotValidException
    {
        if(!errors.hasErrors())
        {
            post.setUser(userRepository.findOne(post.getUser().getId()));
            postRepository.save(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable long id, Principal principal)
    {
        User user = userRepository.findByUsername(principal.getName());
        Post post = postRepository.findOne(id);

        if (post.getUser().getUsername().equals(principal.getName()) || user.getAuthorities().stream().anyMatch(a -> a.getName() == AuthorityName.ROLE_ADMIN))
        {
            postRepository.delete(id);
            return new ResponseEntity<>(new Post(), HttpStatus.OK);
        }
        return new ResponseEntity<>(post, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity<Post> getPost(@PathVariable long id)
    {
        Post post = postRepository.findOne(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping(value = "/like/posts/{id}")
    public Post likePost(@PathVariable Long id, Principal principal)
    {
        Post post = postRepository.findOne(id);
        List<String> likes = post.getUsers();
        Boolean add = likes.contains(principal.getName());
        if (add)
        {
            likes.remove(principal.getName());
            post.setUsers(likes);
            postRepository.save(post);
        }
        else
        {
            likes.add(principal.getName());
            post.setUsers(likes);
            postRepository.save(post);
        }
        return post;
    }


    @GetMapping(value = "/like/comment/{id}")
    public Comment likeComment(@PathVariable Long id, Principal principal)
    {
        Comment comment = commentRepository.findOne(id);
        List<String> likes = comment.getUsers();
        Boolean add = likes.contains(principal.getName());
        if (add)
        {
            likes.remove(principal.getName());
            comment.setUsers(likes);
            commentRepository.save(comment);
        }
        else
        {
            likes.add(principal.getName());
            comment.setUsers(likes);
            commentRepository.save(comment);
        }
        return comment;
    }


}

