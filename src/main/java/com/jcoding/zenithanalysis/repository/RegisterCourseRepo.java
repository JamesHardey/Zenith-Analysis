package com.jcoding.zenithanalysis.repository;

import com.jcoding.zenithanalysis.entity.AppUser;
import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.entity.RegisterCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegisterCourseRepo extends JpaRepository<RegisterCourse, Long> {
     RegisterCourse findByCourse(Course course);
     List<RegisterCourse> findAllByCourse(Course course);
     List<RegisterCourse> findAllByUser(AppUser appUser);
     List<RegisterCourse> findAllByUser(AppUser appUser, Pageable pageable);
}

