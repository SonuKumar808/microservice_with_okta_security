package com.hrs.user_service.service;

import com.hrs.user_service.entity.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    List<User> getAllUsers();
    User getUser(String userId);
    /*User deleteUser(String userId);
    User updateUser(String userId);*/
}
