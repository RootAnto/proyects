package org.educa.airline.repository.inmemory;

import org.educa.airline.entity.User;
import org.educa.airline.entity.UserRole;
import org.educa.airline.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUser implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public InMemoryUser() {
        UserRole role = new UserRole("ROLE_USER", "User", "User app");
        List<UserRole> roles = new ArrayList<>();
        roles.add(role);
        User user  = new User("user@user.com","1ARVn2Auq2_WAqx2gNrL-q3RNjAzXpUfCXrzkA6d4Xa22yhRLy4AC50E-6UTPoscbo31nbOoq51gvkuXzJ6B2w==", roles);
        users.put("user@user.com", user);
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean add(User user) {
        // Verificar si ya existe un usuario con el mismo email
        for (User existingUser : users.values()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        // No hay otro usuario con el mismo email, podemos agregarlo
        users.put(user.getUsername(), user);
        return true;
    }


    @Override
    public boolean deleteUser(String username) {
        users.remove(username);
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        users.replace(user.getUsername(), user);
        return true;
    }

    @Override
    public boolean existUser(String username) {
        return users.containsKey(username);
    }
}
