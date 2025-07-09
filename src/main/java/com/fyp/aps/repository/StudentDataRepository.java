package com.fyp.aps.repository;

import com.fyp.aps.model.StudentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDataRepository extends JpaRepository<StudentData, Long> {

    @Query("SELECT u.gender AS gender, COUNT(u) AS count FROM StudentData u GROUP BY u.gender")
    List<GenderCount> countByGender();
}
