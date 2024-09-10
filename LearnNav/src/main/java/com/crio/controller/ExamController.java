package com.crio.controller;

import com.crio.entities.Exam;
import com.crio.exceptions.ResourceNotFoundException;
import com.crio.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        Exam exam = examService.getExamById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", id));
        return ResponseEntity.ok(exam);
    }

    @PostMapping
    public Exam createExam(@RequestBody Exam exam) {
        return examService.createExam(exam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable Long id, @RequestBody Exam examDetails) {
        Exam updatedExam = examService.updateExam(id, examDetails);
        return ResponseEntity.ok(updatedExam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{examId}/register")
    public ResponseEntity<Void> registerStudentForExam(@PathVariable Long examId, @RequestParam Long studentId) {
        examService.registerStudentForExam(examId, studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "ExamController is working!";
    }
}
