package com.gelo.spirum.rest.service;

import com.gelo.spirum.model.User;

public interface UserService
{
    User saveUser(User user);
    User findOne(Long id);
    User findByUsername(String username);
}
