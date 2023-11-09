package com.EAD.EAD_monolithic.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    //'Public Content' can be access by all user roles

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }


    //return 'User Content' to relevant user

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('DELIVERY') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }


    //return 'Delivery Board' to only delivery role

    @GetMapping("/delivery")
    @PreAuthorize("hasRole('DELIVERY')")
    public String deliveryAccess() {
        return "Delivery Board.";
    }


    //return 'Admin Board' to only admin role

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
