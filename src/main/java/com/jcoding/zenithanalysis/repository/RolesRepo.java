package com.jcoding.zenithanalysis.repository;

import com.jcoding.zenithanalysis.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends JpaRepository<Roles, Long> {
    Roles findByName(String role);
}
