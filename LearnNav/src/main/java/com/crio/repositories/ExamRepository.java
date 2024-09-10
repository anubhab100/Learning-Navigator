package com.crio.repositories;

import com.crio.entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    // Custom query methods can be added here if needed
}
