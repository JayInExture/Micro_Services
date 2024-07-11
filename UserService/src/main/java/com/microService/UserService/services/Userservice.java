package com.microService.UserService.services;

import com.microService.UserService.entities.User;

import java.util.List;

public interface Userservice {

    //user operations

    //create
    User saveUser(User user);

    //get all user
    List<User> getAllUser();

    //get single user of given userId

    User getUser(int userId);

    //TODO: delete
    //TODO: update
}
