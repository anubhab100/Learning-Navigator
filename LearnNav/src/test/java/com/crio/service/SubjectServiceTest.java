package com.crio.service;

import com.crio.entities.Subject;
import com.crio.exceptions.ResourceNotFoundException;
import com.crio.repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject subject;

    @BeforeEach
    public void setup() {
        subject = new Subject();
        subject.setId(1L);
        subject.setName("Mathematics");
    }

    @Test
    public void testCreateSubject_Success() {
        // Mock save() method in repository
        when(subjectRepository.save(subject)).thenReturn(subject);

        // Call the service method
        Subject createdSubject = subjectService.createSubject(subject);

        // Verify the result
        assertNotNull(createdSubject);
        assertEquals(subject.getId(), createdSubject.getId());
        assertEquals("Mathematics", createdSubject.getName());

        // Verify that save() was called
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    public void testGetSubjectById_Success() {
        // Mock findById() method in repository
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        // Call the service method
        Optional<Subject> retrievedSubject = subjectService.getSubjectById(1L);

        // Verify the result
        assertTrue(retrievedSubject.isPresent());
        assertEquals(subject.getId(), retrievedSubject.get().getId());

        // Verify that findById() was called
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetSubjectById_NotFound() {
        // Mock findById() to return an empty Optional
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method
        Optional<Subject> result = subjectService.getSubjectById(1L);

        // Verify the result
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllSubjects_Success() {
        // Create a list of subjects
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(subject);
        subjectList.add(new Subject(2, "Physics"));

        // Mock findAll() method in repository
        when(subjectRepository.findAll()).thenReturn(subjectList);

        // Call the service method
        List<Subject> allSubjects = subjectService.getAllSubjects();

        // Verify the result
        assertEquals(2, allSubjects.size());
        assertEquals("Mathematics", allSubjects.get(0).getName());
        assertEquals("Physics", allSubjects.get(1).getName());

        // Verify that findAll() was called
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateSubject_Success() {
        // Mock findById() method in repository
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        // Create subject details to update
        Subject updatedDetails = new Subject();
        updatedDetails.setName("Advanced Mathematics");

        // Call the service method
        Subject updatedSubject = subjectService.updateSubject(1L, updatedDetails);

        // Verify the result
        assertNotNull(updatedSubject);
        assertEquals("Advanced Mathematics", updatedSubject.getName());

        // Verify that save() was called
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    public void testUpdateSubject_NotFound() {
        // Mock findById() to return an empty Optional
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        // Create subject details to update
        Subject updatedDetails = new Subject();
        updatedDetails.setName("Advanced Mathematics");

        // Call the service method and expect a ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> subjectService.updateSubject(1L, updatedDetails));

        // Verify the exception message
        assertEquals("Subject not found with id : '1'", exception.getMessage());
    }

    @Test
    public void testDeleteSubject_Success() {
        // Call the service method
        subjectService.deleteSubject(1L);

        // Verify that deleteById() was called
        verify(subjectRepository, times(1)).deleteById(1L);
    }
}
