package com.gelo.spirum.rest.service;

import com.gelo.spirum.model.User;
import com.gelo.spirum.rest.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userService")
@Transactional
public class UserServiceImpl implements UserService
{
    private  final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public com.gelo.spirum.model.User saveUser(com.gelo.spirum.model.User user)
    {
        return userRepository.save(user);
    }

    @Override
    public User findOne(Long id)
    {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
}
