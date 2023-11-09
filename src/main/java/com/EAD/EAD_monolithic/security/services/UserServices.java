package com.EAD.EAD_monolithic.security.services;

import com.EAD.EAD_monolithic.dto.UserDTO;
import com.EAD.EAD_monolithic.models.User;
import com.EAD.EAD_monolithic.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServices {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserDTO updatePassword(String email, String username, String newPassword) {
        User user = userRepo.findByEmailAndUsername(email, username); // Change the method name from "findByEmailAndName" to "findByEmailAndUsername"
        if (user != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepo.save(user);
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }
}
