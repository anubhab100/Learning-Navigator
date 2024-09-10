package com.crio.service;

import com.crio.entities.Subject;
import com.crio.exceptions.ResourceNotFoundException;
import com.crio.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // Create a new subject
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    // Retrieve a subject by ID
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    // Retrieve all subjects
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Update subject details
    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", id));
        subject.setName(subjectDetails.getName());
        return subjectRepository.save(subject);
    }

    // Delete a subject
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
