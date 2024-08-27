package com.crio.entities;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "student_subjects", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> enrolledSubjects = new HashSet<>();

    @ManyToMany(mappedBy = "enrolledStudents")
    private Set<Exam> enrolledExams = new HashSet<>();

    // Getters and Setters for enrolledSubjects

    public Set<Subject> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    public void setEnrolledSubjects(Set<Subject> enrolledSubjects) {
        this.enrolledSubjects = enrolledSubjects;
    }

    // Getters and Setters for enrolledExams

    public Set<Exam> getEnrolledExams() {
        return enrolledExams;
    }

    public void setEnrolledExams(Set<Exam> enrolledExams) {
        this.enrolledExams = enrolledExams;
    }
}
