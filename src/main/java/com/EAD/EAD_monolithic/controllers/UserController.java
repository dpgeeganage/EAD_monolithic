package com.EAD.EAD_monolithic.controllers;

import com.EAD.EAD_monolithic.dto.UserDTO;
import com.EAD.EAD_monolithic.security.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserServices userService;

    @PutMapping("/updatePassword")
    public UserDTO updatePassword(@RequestBody UserDTO userDTO) {
        return userService.updatePassword(userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword());
    }
}
