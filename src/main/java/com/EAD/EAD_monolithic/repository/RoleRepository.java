package com.EAD.EAD_monolithic.repository;

/* extends from JpaRepository which gives CRUD (Create, Read, Update, Deletion) operations for
'Role' entity and 'Long' is the type of the primary key of 'Role' entity */

import com.EAD.EAD_monolithic.models.ERole;
import com.EAD.EAD_monolithic.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository indicate that this is repository component
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    //search 'Role' entity in database based on its name

    /* returns 'Optional' because result can be null if not found
    ('Optional' can check whether object is found or not before access it)*/

    Optional<Role> findByName(ERole name);

}