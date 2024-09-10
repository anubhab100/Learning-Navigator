package com.crio.controller;

import com.crio.entities.Subject;
import com.crio.service.SubjectService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    private Subject subject;

    @BeforeEach
    public void setup() {
        subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");
    }

    @Test
    public void testGetAllSubjects() throws Exception {
        Mockito.when(subjectService.getAllSubjects()).thenReturn(Arrays.asList(subject));

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Math"));
    }

    @Test
    public void testGetSubjectById_Success() throws Exception {
        Mockito.when(subjectService.getSubjectById(1L)).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Math"));
    }

    @Test
    public void testGetSubjectById_NotFound() throws Exception {
        Mockito.when(subjectService.getSubjectById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/subjects/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Subject not found with id : '1'"));
    }

    @Test
    public void testCreateSubject() throws Exception {
        Mockito.when(subjectService.createSubject(any(Subject.class))).thenReturn(subject);

        String subjectJson = "{\"name\": \"Math\"}";

        mockMvc.perform(post("/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subjectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Math"));
    }

    @Test
    public void testUpdateSubject_Success() throws Exception {
        Mockito.when(subjectService.updateSubject(anyLong(), any(Subject.class))).thenReturn(subject);

        String subjectJson = "{\"name\": \"Physics\"}";

        mockMvc.perform(put("/subjects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subjectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Math"));
    }

    @Test
    public void testDeleteSubject_Success() throws Exception {
        Mockito.doNothing().when(subjectService).deleteSubject(1L);

        mockMvc.perform(delete("/subjects/1"))
                .andExpect(status().isNoContent());
    }
}
