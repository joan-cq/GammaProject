package com.gamma.backend.repository;

import com.gamma.backend.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByOrderByTimestampDesc();
}
