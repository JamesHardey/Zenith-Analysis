package com.jcoding.zenithanalysis.repository;

import com.jcoding.zenithanalysis.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<AppUser, Long> {
}
