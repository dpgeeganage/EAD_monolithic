package com.EAD.EAD_monolithic.repository;

import com.EAD.EAD_monolithic.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Same as before here use 'User' entity
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //search by 'username' for te 'User' entity that matches
    Optional<User> findByUsername(String username);

    //returns a boolean whether searched 'username' or 'email' exists
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByEmail(String email);

    public User findByEmailAndUsername(String email, String username);
}