package com.fyp.aps.model;


import jakarta.persistence.*;

@Entity
@Table(name = "cleaned_student_data")
public class StudentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer gender;
    private Double hsc;
    private Double ssc;
    private Integer income;
    private Integer computer;
    private Integer preparation;
    private Integer gaming;
    private Integer attendance;
    private Integer job;
    private Integer english;
    private Integer extra;
    private Integer semester;
    private Double last;
    private Double overall;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public Double getHsc() { return hsc; }
    public void setHsc(Double hsc) { this.hsc = hsc; }

    public Double getSsc() { return ssc; }
    public void setSsc(Double ssc) { this.ssc = ssc; }

    public Integer getIncome() { return income; }
    public void setIncome(Integer income) { this.income = income; }

    public Integer getComputer() { return computer; }
    public void setComputer(Integer computer) { this.computer = computer; }

    public Integer getPreparation() { return preparation; }
    public void setPreparation(Integer preparation) { this.preparation = preparation; }

    public Integer getGaming() { return gaming; }
    public void setGaming(Integer gaming) { this.gaming = gaming; }

    public Integer getAttendance() { return attendance; }
    public void setAttendance(Integer attendance) { this.attendance = attendance; }

    public Integer getJob() { return job; }
    public void setJob(Integer job) { this.job = job; }

    public Integer getEnglish() { return english; }
    public void setEnglish(Integer english) { this.english = english; }

    public Integer getExtra() { return extra; }
    public void setExtra(Integer extra) { this.extra = extra; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Double getLast() { return last; }
    public void setLast(Double last) { this.last = last; }

    public Double getOverall() { return overall; }
    public void setOverall(Double overall) { this.overall = overall; }
}
