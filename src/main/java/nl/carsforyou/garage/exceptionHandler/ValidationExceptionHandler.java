package nl.carsforyou.garage.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        //iterate through all errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        //return nicely formatted error
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);
    }

    //this handles exception during IT tests 'not found'
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", ex.getMessage()));
    }



    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        // 'global' error messages like this;
        //  {
        //     "message": "Cannot delete Customer 1 because vehicles exist",
        //     "status": 400,
        //     "path": "/customers/1",
        //     "error": "400 BAD_REQUEST",
        //     "timestamp": "2026-01-25T16:32:28.991869100Z"
        //  }
        @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
        public ResponseEntity<?> handle(ResponseStatusException ex, HttpServletRequest request) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", ex.getStatusCode().value(),
                    "error", ex.getStatusCode().toString(),
                    "message", ex.getReason(),
                    "path", request.getRequestURI()
            ));
        }

        //Exception on deleteById(id) when the row doesn't exist
        @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
        public ResponseEntity<?> handleEmptyDelete(org.springframework.dao.EmptyResultDataAccessException ex,
                                                   HttpServletRequest request) {
            return ResponseEntity.status(404).body(Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 404,
                    "error", "404 NOT_FOUND",
                    "message", "Resource not found",
                    "path", request.getRequestURI()
            ));
        }

        // FK constraint / integrity issues like a vehicle referenced by other tables
        @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
        public ResponseEntity<?> handleIntegrity(org.springframework.dao.DataIntegrityViolationException ex,
                                                 HttpServletRequest request) {
            return ResponseEntity.status(409).body(Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 409,
                    "error", "409 CONFLICT",
                    "message", "Cannot delete because related records exist",
                    "path", request.getRequestURI()
            ));
        }
    }
}
