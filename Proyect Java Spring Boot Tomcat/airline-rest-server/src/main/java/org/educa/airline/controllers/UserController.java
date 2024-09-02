package org.educa.airline.controllers;

import jakarta.validation.Valid;
import org.educa.airline.dto.UserDTO;
import org.educa.airline.entity.User;
import org.educa.airline.mappers.ProductUserMapper;
import org.educa.airline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final ProductUserMapper userMapper;

    @Autowired
    public UserController(UserService userService, ProductUserMapper userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO userDTO){
        // Verificar si el usuario ya existe por su ID
        if (userService.getUserById(userDTO.getEmail())!=null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
        }

        // Convertir UserDTO a User usando el mapper
        User user = userMapper.toEntity(userDTO);

        // Obtener true si se creó el usuario
        boolean created = userService.addUser(user);

        // Devolver respuesta al cliente
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user.");
        }
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable String userId) {
        // Obtener el usuario por su ID utilizando el servicio UserService
        User user = userService.getUserById(userId);

        // Verificar si el usuario existe
        if (user != null) {
            // Convertir el usuario a UserDTO utilizando el mapper ProductUserMapper
            UserDTO userDTO = userMapper.toDTO(user);

            // Devolver el usuario como UserDTO en la respuesta HTTP
            return ResponseEntity.ok(userDTO);
        } else {
            // Si el usuario no existe, devolver una respuesta con estado NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        // Obtener el usuario por su ID utilizando el servicio UserService
        User user = userService.getUserById(userId);

        // Verificar si el usuario existe
        if (user != null) {
            // Si el usuario existe, intentar eliminarlo utilizando el servicio UserService
            boolean deleted = userService.deleteUser(userId);

            if (deleted) {
                return ResponseEntity.ok("User deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO userDTO) {
        // Verificar si el usuario con el ID proporcionado existe
        User existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Actualizar el usuario con los datos proporcionados en el cuerpo de la petición
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(userDTO.getPassword());
        // Actualizar otros campos segun sea necesario


        // Guardar el usuario actualizado en la base de datos
        userService.updateUser(existingUser);

        // Devolver un HTTP Status OK para indicar que el usuario ha sido actualizado correctamente
        return ResponseEntity.ok().build();
    }

}
