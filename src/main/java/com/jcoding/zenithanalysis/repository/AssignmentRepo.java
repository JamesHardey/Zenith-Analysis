package com.jcoding.zenithanalysis.repository;

import com.jcoding.zenithanalysis.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, Long> {

}
