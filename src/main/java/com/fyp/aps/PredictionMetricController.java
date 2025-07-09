package com.fyp.aps;

import com.fyp.aps.model.StudentPrediction;
import com.fyp.aps.model.User;
import com.fyp.aps.repository.GenderCount;
import com.fyp.aps.repository.StudentPredictionRepository;
import com.fyp.aps.service.PredictionService;
import com.fyp.aps.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PredictionMetricController {

    @Autowired
    private PredictionService predictionService;

    @GetMapping("/predictionmetrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> result = new HashMap<>();

        result.put("totalPredictions", predictionService.getTotalPredictions());
        result.put("predictionBreakdown", predictionService.getPredictionDistribution());
        result.put("predictionsPerEducator", predictionService.getPredictionsPerEducator());
        result.put("predictionsOverTime", predictionService.getPredictionsOverTime());

        return ResponseEntity.ok(result);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private StudentPredictionRepository studentPredictionRepository;

    @GetMapping("/cgpa-bands")
    public ResponseEntity<Map<String, Integer>> getCgpaBandCounts(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User educator = userService.findById(userId);
        if (educator == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Fetch StudentPrediction list for this educator
        List<StudentPrediction> predictions = studentPredictionRepository.findByEducator(educator);

        Map<String, Integer> bands = new LinkedHashMap<>();
        bands.put("< 2.0", 0);
        bands.put("2.0–2.99", 0);
        bands.put("3.0–3.49", 0);
        bands.put("3.5–4.0", 0);

        for (StudentPrediction p : predictions) {
        String cgpaStr = p.getPrediction();
        double cgpa = 0.0;
        try {
            cgpa = Double.parseDouble(cgpaStr);
        } catch (NumberFormatException e) {
            // Handle invalid number format, skip or set cgpa to 0
            continue; // skip this entry if invalid
        }

        if (cgpa < 2.0) bands.put("< 2.0", bands.get("< 2.0") + 1);
        else if (cgpa < 3.0) bands.put("2.0–2.99", bands.get("2.0–2.99") + 1);
        else if (cgpa < 3.5) bands.put("3.0–3.49", bands.get("3.0–3.49") + 1);
        else bands.put("3.5–4.0", bands.get("3.5–4.0") + 1);
    }

        return ResponseEntity.ok(bands);
    }

    @GetMapping("/genderdistribution")
    public ResponseEntity<List<Map<String, Object>>> getGenderDistributionByEducator(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User educator = userService.findById(userId);
        if (educator == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GenderCount> results = studentPredictionRepository.countByGenderForEducator(educator);
        List<Map<String, Object>> response = new ArrayList<>();

        for (GenderCount gc : results) {
            String genderLabel = switch (gc.getGender()) {
                case "Male" -> "Male";
                case "Female" -> "Female";
                default -> "Other";
            };

            Map<String, Object> map = new HashMap<>();
            map.put("gender", genderLabel);
            map.put("count", gc.getCount());
            response.add(map);
        }

        return ResponseEntity.ok(response);
    }





}
