package com.crio.controller;

import com.crio.entities.Exam;
import com.crio.service.ExamService;
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

@WebMvcTest(ExamController.class)
public class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExamService examService;

    private Exam exam;

    @BeforeEach
    public void setup() {
        exam = new Exam();
        exam.setId(1L);
    }

    @Test
    public void testGetAllExams() throws Exception {
        Mockito.when(examService.getAllExams()).thenReturn(Arrays.asList(exam));

        mockMvc.perform(get("/exams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void testGetExamById_Success() throws Exception {
        Mockito.when(examService.getExamById(1L)).thenReturn(Optional.of(exam));

        mockMvc.perform(get("/exams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetExamById_NotFound() throws Exception {
        Mockito.when(examService.getExamById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/exams/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Exam not found with id : '1'"));
    }

    @Test
    public void testCreateExam() throws Exception {
        Mockito.when(examService.createExam(any(Exam.class))).thenReturn(exam);

        String examJson = "{\"subject\": { \"id\": 1 }}";

        mockMvc.perform(post("/exams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(examJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdateExam_Success() throws Exception {
        Mockito.when(examService.updateExam(anyLong(), any(Exam.class))).thenReturn(exam);

        String examJson = "{\"subject\": { \"id\": 2 }}";

        mockMvc.perform(put("/exams/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(examJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteExam_Success() throws Exception {
        Mockito.doNothing().when(examService).deleteExam(1L);

        mockMvc.perform(delete("/exams/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRegisterStudentForExam() throws Exception {
        Mockito.doNothing().when(examService).registerStudentForExam(1L, 1L);

        mockMvc.perform(post("/exams/1/register?studentId=1"))
                .andExpect(status().isOk());
    }
}
