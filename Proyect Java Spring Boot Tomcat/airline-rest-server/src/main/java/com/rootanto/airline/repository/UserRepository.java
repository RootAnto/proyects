package com.rootanto.airline.repository;

import com.rootanto.airline.entity.User;

import java.util.List;

public interface UserRepository {
    User getUser(String id);

    List<User> getAllUsers();

    boolean add(User user);

    boolean deleteUser(String username);

    boolean updateUser(User user);

    boolean existUser(String username);

}
