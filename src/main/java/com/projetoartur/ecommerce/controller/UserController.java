package com.projetoartur.ecommerce.controller;

import com.projetoartur.ecommerce.dto.CreateUserDto;
import com.projetoartur.ecommerce.entities.UserEntity;
import com.projetoartur.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto dto){
        var user = userService.createUser(dto);
        return ResponseEntity.created(URI.create("/users/" + user.getUserId())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable("userId") UUID userId){
        var user = userService.getUserById(userId);

        return user.isPresent() ?
                ResponseEntity.ok(user.get()) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId") UUID userId){
        var userDeleted = userService.deleteUserById(userId);

        return userDeleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }




}
