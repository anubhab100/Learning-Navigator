package com.crio.service;

import com.crio.entities.Exam;
import com.crio.entities.Student;
import com.crio.entities.Subject;
import com.crio.exceptions.ExamEnrollmentException;
import com.crio.exceptions.ResourceNotFoundException;
import com.crio.repositories.ExamRepository;
import com.crio.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private ExamService examService;

    private Exam exam;
    private Student student;
    private Subject subject;

    @BeforeEach
    public void setup() {
        // Initialize subject
        subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        // Initialize student
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.getEnrolledSubjects().add(subject); // Enroll student in the subject

        // Initialize exam
        exam = new Exam();
        exam.setId(1L);
        exam.setSubject(subject);
    }

    @Test
    public void testRegisterStudentForExam_Success() {
        // Mocking the repositories
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Call the service method
        examService.registerStudentForExam(1L, 1L);

        // Verify interactions
        assertTrue(exam.getEnrolledStudents().contains(student));
        verify(examRepository, times(1)).save(exam);
    }

    @Test
    public void testRegisterStudentForExam_StudentNotEnrolledInSubject() {
        // Create a student not enrolled in the subject
        Student anotherStudent = new Student();
        anotherStudent.setId(2L);
        anotherStudent.setName("Jane Doe");

        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(studentRepository.findById(2L)).thenReturn(Optional.of(anotherStudent));

        // Test the scenario
        ExamEnrollmentException exception = assertThrows(ExamEnrollmentException.class,
                () -> examService.registerStudentForExam(1L, 2L));

        assertEquals("2 is not enrolled in the subject with ID 1", exception.getMessage());
    }

    @Test
    public void testRegisterStudentForExam_ExamNotFound() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        // Test the scenario
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> examService.registerStudentForExam(1L, 1L));

        assertEquals("Exam not found with id : '1'", exception.getMessage());
    }

    @Test
    public void testRegisterStudentForExam_StudentNotFound() {
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Test the scenario
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> examService.registerStudentForExam(1L, 1L));

        assertEquals("Student not found with id : '1'", exception.getMessage());
    }

    @Test
    public void testGetAllExams() {
        examService.getAllExams();
        verify(examRepository, times(1)).findAll();
    }

    @Test
    public void testGetExamById_Success() {
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        Optional<Exam> result = examService.getExamById(1L);

        assertTrue(result.isPresent());
        assertEquals(exam, result.get());
    }

    @Test
    public void testGetExamById_NotFound() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Exam> result = examService.getExamById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testCreateExam() {
        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        Exam result = examService.createExam(exam);

        assertEquals(exam, result);
    }

    @Test
    public void testUpdateExam_Success() {
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        Exam updatedExam = examService.updateExam(1L, exam);

        assertEquals(exam, updatedExam);
    }

    @Test
    public void testDeleteExam_Success() {
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        examService.deleteExam(1L);

        verify(examRepository, times(1)).delete(exam);
    }

    @Test
    public void testDeleteExam_NotFound() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> examService.deleteExam(1L));
    }
}
