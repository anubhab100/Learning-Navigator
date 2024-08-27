package com.crio.service;

import com.crio.entities.Student;
import com.crio.entities.Subject;
import com.crio.exceptions.ResourceNotFoundException;
import com.crio.entities.Exam;
import com.crio.repositories.StudentRepository;
import com.crio.repositories.SubjectRepository;
import com.crio.repositories.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamRepository examRepository;

    // Create a new student
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // Retrieve a student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Retrieve all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Update student details
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        student.setName(studentDetails.getName());
        return studentRepository.save(student);
    }

    // Delete a student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Enroll a student in a subject
    @Transactional
    public Student enrollStudentInSubject(Long studentId, Long subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", subjectId));

        student.getEnrolledSubjects().add(subject);
        subject.getStudents().add(student);

        return studentRepository.save(student);
    }

    // Register a student for an exam
    @Transactional
    public Student registerStudentForExam(Long studentId, Long examId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", examId));

        if (!student.getEnrolledSubjects().contains(exam.getSubject())) {
            throw new IllegalArgumentException(
                    "Student must be enrolled in the subject before registering for the exam");
        }

        student.getEnrolledExams().add(exam);
        exam.getEnrolledStudents().add(student);

        return studentRepository.save(student);
    }
}
