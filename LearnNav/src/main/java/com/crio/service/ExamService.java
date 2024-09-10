package com.crio.service;

import com.crio.entities.Exam;
import com.crio.entities.Student;
import com.crio.entities.Subject;
import com.crio.exceptions.ExamEnrollmentException;
import com.crio.exceptions.ResourceNotFoundException;
import com.crio.repositories.ExamRepository;
import com.crio.repositories.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentRepository studentRepository;

    public void registerStudentForExam(Long examId, Long studentId) {
        // Retrieve the exam from the repository
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", examId));

        // Retrieve the student from the repository
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        // Check if the student is enrolled in the subject associated with the exam
        Subject subject = exam.getSubject();
        if (!student.getEnrolledSubjects().contains(subject)) {
            throw new ExamEnrollmentException(studentId + " is not enrolled in the subject with ID " + subject.getId());
        }

        // Register the student for the exam
        if (!exam.getEnrolledStudents().contains(student)) {
            exam.getEnrolledStudents().add(student);
            examRepository.save(exam);
        }
    }

    // Retrieve all exams
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    // Retrieve an exam by ID
    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }

    // Create a new exam
    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    // Update an existing exam
    public Exam updateExam(Long id, Exam examDetails) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", id));

        // Update the exam's fields with the provided details
        exam.setSubject(examDetails.getSubject());
        exam.setEnrolledStudents(examDetails.getEnrolledStudents());

        return examRepository.save(exam);
    }

    // Delete an exam by ID
    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", id));

        examRepository.delete(exam);
    }
}
