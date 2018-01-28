package com.gelo.spirum.rest;

import com.gelo.spirum.model.Authority;
import com.gelo.spirum.model.AuthorityName;
import com.gelo.spirum.model.User;
import com.gelo.spirum.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.*;

import javax.validation.Valid;


/**
 * Created by olejk4 on 13.10.17.
 */
@CrossOrigin(origins = {"http://localhost:4200", "https://spirum-191511.firebaseapp.com","https://spirum.tk" })
@RestController
public class HomeController {
    @Autowired
    UserRepository rep;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(value = "/register")
    ResponseEntity<User> register(@RequestBody @Valid UserRegisterForm userRegisterForm, Errors errors)throws MethodArgumentNotValidException
    {
        if(!errors.hasErrors() && isValidUsername(userRegisterForm.getUsername()))
        {
            Authority authority = new Authority();
            authority.setId(1L);
            authority.setName(AuthorityName.ROLE_USER);
            Set<Authority> authorities = new LinkedHashSet<>(Arrays.asList(authority));
            User user = new User(userRegisterForm.getUsername(), passwordEncoder.encode(userRegisterForm.getPassword()), userRegisterForm.getFirstName(), userRegisterForm.getLastName(), userRegisterForm.getEmail(),
                    true, new Date(System.currentTimeMillis()), authorities, userRegisterForm.getProfilePic());
            rep.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private boolean isValidUsername(String username){
        return rep.findByUsername(username) == null;
    }
}
