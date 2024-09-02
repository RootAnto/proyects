package com.rootanto.airline.services;
import com.rootanto.airline.entity.User;
import com.rootanto.airline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUser(username);
    }

    public boolean addUser(User user) {
        System.out.println("Usuario: " + user);
        return userRepository.add(user);
    }

    public User getUserById (String id){
        return userRepository.getUser(id);
    }


    public boolean updateUser(User existingUser) {
        return userRepository.updateUser(existingUser);
    }

    public boolean deleteUser(String userId) {
        return  userRepository.deleteUser(userId);
    }

    public List<User> getAllUsers() {
        return  userRepository.getAllUsers();
    }
}