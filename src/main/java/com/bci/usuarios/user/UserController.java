package com.bci.usuarios.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/usuario")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/lista")
    public List<UserDTO> getUsers(){

        return userService.getUsers();
    }

    @GetMapping(path = "/login")
    public UserDTO loginUser(
            @RequestParam(required = true) String email,
            @RequestParam(required = true) String password){

        return userService.loginUser(email, password);
    }

    @PostMapping(path = "/registrar")
    public UserDTO registerUser(@RequestBody User user){

        return userService.addUser(user);
    }

    @PutMapping(path = "/actualizar/{userId}")
    public UserDTO updateUser(
            @PathVariable("userId") String userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String email){

        return userService.updateUser(userId,name,email,password);
    }

    @PutMapping(path = "/activar/{userId}")
    public UserDTO activateUser(
            @PathVariable("userId") String userId,
            @RequestParam(required = true) String token){

        return userService.activateUser(userId,token);
    }
}
