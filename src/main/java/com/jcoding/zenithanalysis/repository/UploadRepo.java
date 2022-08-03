package com.jcoding.zenithanalysis.repository;

import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.entity.Uploads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadRepo extends JpaRepository<Uploads, Long> {
    List<Uploads> findAllByCourse(Course course);
}
