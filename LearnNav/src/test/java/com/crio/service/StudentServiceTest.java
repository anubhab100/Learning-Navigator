package com.crio.service;

import com.crio.entities.Exam;
import com.crio.entities.Student;
import com.crio.entities.Subject;
import com.crio.exceptions.ResourceNotFoundException;
import com.crio.repositories.ExamRepository;
import com.crio.repositories.StudentRepository;
import com.crio.repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ExamRepository examRepository;

    private Student student;
    private Subject subject;
    private Exam exam;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setEnrolledSubjects(new HashSet<>());
        student.setEnrolledExams(new HashSet<>());

        subject = new Subject();
        subject.setId(1L);
        subject.setName("Mathematics");
        subject.setEnrolledStudents(new HashSet<>());

        exam = new Exam();
        exam.setId(1L);
        exam.setSubject(subject);
        exam.setEnrolledStudents(new HashSet<>());
    }

    @Test
    void testCreateStudent() {
        when(studentRepository.save(student)).thenReturn(student);

        Student createdStudent = studentService.createStudent(student);

        assertNotNull(createdStudent);
        assertEquals(student.getName(), createdStudent.getName());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testGetStudentById_ExistingId() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> foundStudent = studentService.getStudentById(1L);

        assertTrue(foundStudent.isPresent());
        assertEquals(student.getName(), foundStudent.get().getName());
    }

    @Test
    void testEnrollStudentInSubject_SubjectNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.enrollStudentInSubject(1L, 1L));
    }

    @Test
    void testRegisterStudentForExam_Success() {
        student.getEnrolledSubjects().add(subject); // Student must be enrolled in the subject first
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(studentRepository.save(student)).thenReturn(student);

        Student registeredStudent = studentService.registerStudentForExam(1L, 1L);

        assertTrue(registeredStudent.getEnrolledExams().contains(exam));
        assertTrue(exam.getEnrolledStudents().contains(student));
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testRegisterStudentForExam_NotEnrolledInSubject() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        assertThrows(IllegalArgumentException.class, () -> studentService.registerStudentForExam(1L, 1L));
    }

    @Test
    void testDeleteStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudent_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
    }
}
