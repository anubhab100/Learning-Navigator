package com.crio.entities;

import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "subjects")
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "enrolledSubjects")
    private Set<Student> enrolledStudents = new HashSet<>();

    public Subject() {
    }

    public Subject(int i, String name2) {
    }

}
