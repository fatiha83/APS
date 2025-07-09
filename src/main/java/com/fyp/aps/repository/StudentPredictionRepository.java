package com.fyp.aps.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fyp.aps.model.StudentPrediction;
import com.fyp.aps.model.User;

public interface StudentPredictionRepository extends JpaRepository<StudentPrediction, Long> {

    // StudentPrediction findTopByOrderByIdDesc();
    
    StudentPrediction findTopByEducatorIdOrderByIdDesc(Long educatorId);

    List<StudentPrediction> findByEducator(User educator);

    @Query("SELECT p.educator.name, COUNT(p) FROM StudentPrediction p GROUP BY p.educator.name")
    List<Object[]> countByEducator();

    @Query("SELECT FUNCTION('DATE', p.predictionTime), COUNT(p) FROM StudentPrediction p GROUP BY FUNCTION('DATE', p.predictionTime)")
    List<Object[]> countByDate();

    @Query("SELECT u.gender AS gender, COUNT(u) AS count FROM StudentPrediction u WHERE u.educator = :educator GROUP BY u.gender")
    List<GenderCount> countByGenderForEducator(@Param("educator") User educator);

    
}
