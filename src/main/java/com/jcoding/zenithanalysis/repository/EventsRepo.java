package com.jcoding.zenithanalysis.repository;

import com.jcoding.zenithanalysis.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepo extends JpaRepository<Events, Long> {
}
