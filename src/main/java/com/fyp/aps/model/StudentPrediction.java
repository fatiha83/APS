package com.fyp.aps.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
public class StudentPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private int age;
    private String gender;
    private String studyHours;
    private String attendance;
    private double previousGrades;
    private String prediction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "educator_id")
    private User educator;


    @Temporal(TemporalType.TIMESTAMP)
    private Date predictionTime = new Date();


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStuName() { return studentName; }
    public void setStuName(String studentName) { this.studentName = studentName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getStuHours() { return studyHours; }
    public void setStuHours(String studyHours) { this.studyHours = studyHours; }

    public String getAttendance() { return attendance; }
    public void setAttendance(String attendance) { this.attendance = attendance; }

    public double getPrevGrades() { return previousGrades; }
    public void setPrevGrades(double previousGrades) { this.previousGrades = previousGrades; }

    public String getPrediction() { return prediction; }
    public void setPrediction(String prediction) { this.prediction = prediction; }

    public void setEducator(User educator) {this.educator = educator;}

   
    
}
