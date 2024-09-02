package org.educa.airline.mappers;

import org.educa.airline.dto.UserDTO;
import org.educa.airline.entity.User;
import org.educa.airline.entity.UserRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductUserMapper {

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        List<String> roles = new ArrayList<>();
        for (UserRole role : user.getRoles()) {
            roles.add(role.getRole());
        }
        userDTO.setRoles(roles);
        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        List<UserRole> userRoles = new ArrayList<>();
        for (String roleName : userDTO.getRoles()) {
            userRoles.add(new UserRole(roleName, null, null));
        }
        user.setRoles(userRoles);
        return user;
    }

    public List<UserDTO> toDTOs(List<User> users) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : users) {
            userDTOList.add(toDTO(user));
        }
        return userDTOList;
    }
}
