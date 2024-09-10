package com.crio.controller;

import com.crio.entities.Student;
import com.crio.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private Student student;

    @BeforeEach
    public void setup() {
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Mockito.when(studentService.getAllStudents()).thenReturn(Arrays.asList(student));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    public void testGetStudentById_Success() throws Exception {
        Mockito.when(studentService.getStudentById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testGetStudentById_NotFound() throws Exception {
        Mockito.when(studentService.getStudentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found with id : '1'"));
    }

    @Test
    public void testCreateStudent() throws Exception {
        Mockito.when(studentService.createStudent(any(Student.class))).thenReturn(student);

        String studentJson = "{\"name\": \"John Doe\"}";

        mockMvc.perform(post("/students/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testUpdateStudent_Success() throws Exception {
        Mockito.when(studentService.updateStudent(anyLong(), any(Student.class))).thenReturn(student);

        String studentJson = "{\"name\": \"John Updated\"}";

        mockMvc.perform(put("/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testDeleteStudent_Success() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNoContent());
    }
}
