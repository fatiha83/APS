package com.fyp.aps.service;

import com.fyp.aps.model.StudentPrediction;
import com.fyp.aps.model.User;
import com.fyp.aps.repository.StudentPredictionRepository;
import com.fyp.aps.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PredictionService {

    @Autowired
    private StudentPredictionRepository predictionRepository;

    @Autowired
    private UserRepository userRepository;

    public void savePrediction(StudentPrediction prediction, Long educatorId) {
        User educator = userRepository.findById(educatorId).orElseThrow();
        prediction.setEducator(educator);
        predictionRepository.save(prediction);
    }

    public Map<String, Long> getPredictionsPerEducator() {
        List<Object[]> data = predictionRepository.countByEducator();
        return data.stream().collect(Collectors.toMap(
            d -> (String) d[0], // educator name
            d -> (Long) d[1]    // count
        ));
    }

    public Map<String, Long> getPredictionsOverTime() {
        List<Object[]> data = predictionRepository.countByDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return data.stream().collect(Collectors.toMap(
            d -> sdf.format((Date) d[0]), // date
            d -> (Long) d[1]              // count
        ));
    }

    public long getTotalPredictions() {
    return predictionRepository.count();
    }

    public Map<String, Long> getPredictionDistribution() {
        List<StudentPrediction> predictions = predictionRepository.findAll();
        return predictions.stream().collect(Collectors.groupingBy(
            StudentPrediction::getPrediction,
            Collectors.counting()
        ));
    }
    

}
