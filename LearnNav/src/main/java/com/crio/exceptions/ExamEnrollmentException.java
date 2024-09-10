package com.crio.exceptions;

public class ExamEnrollmentException extends RuntimeException {

    private String studentName;
    private String subjectName;
    private String message;

    public ExamEnrollmentException(String studentName, String subjectName, String message) {
        super(String.format("Error enrolling student '%s' in subject '%s': %s", studentName, subjectName, message));
        this.studentName = studentName;
        this.subjectName = subjectName;
        this.message = message;
    }

    // Constructor that accepts a custom error message
    public ExamEnrollmentException(String message) {
        super(message);
    }

    // Optional: Constructor that accepts a custom error message and a cause
    public ExamEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    @Override
    public String getMessage() {
        return message;
    }
}