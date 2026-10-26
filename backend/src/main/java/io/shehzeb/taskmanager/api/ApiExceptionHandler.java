package io.shehzeb.taskmanager.api;

import io.shehzeb.taskmanager.domain.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/** RFC 7807 error translation. */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail handleNotFound(TaskNotFoundException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        p.setTitle("Task not found");
        return p;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed");
        p.setTitle("Validation error");
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
        p.setProperty("errors", errors);
        return p;
    }
}
