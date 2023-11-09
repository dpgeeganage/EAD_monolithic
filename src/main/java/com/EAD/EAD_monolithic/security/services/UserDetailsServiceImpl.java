package com.EAD.EAD_monolithic.security.services;

import com.EAD.EAD_monolithic.models.User;
import com.EAD.EAD_monolithic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    // a method that takes 'username' and returns 'UserDetails' object

    @Override
    //@Transactional -> ensure transaction management handle my spring (ensure integrity of database)
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)

                //if user not found it throws UsernameNotFoundException
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}

