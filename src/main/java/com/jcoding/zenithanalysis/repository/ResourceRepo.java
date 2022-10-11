package com.jcoding.zenithanalysis.repository;

import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepo extends JpaRepository<Resource, Long> {

    List<Resource> findByResourceType(String resourceType);

}
