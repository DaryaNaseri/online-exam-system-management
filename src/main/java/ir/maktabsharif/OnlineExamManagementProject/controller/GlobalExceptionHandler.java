package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.TitleOrCourseCodeMustBeUniqueException;
import ir.maktabsharif.OnlineExamManagementProject.exception.UsernameOrEmailMustBeUniqueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameOrEmailMustBeUniqueException.class)
    public ResponseEntity<String> handleUsernameAlreadyTaken(UsernameOrEmailMustBeUniqueException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(TitleOrCourseCodeMustBeUniqueException.class)
    public ResponseEntity<String> handleTitleAlreadyTaken(TitleOrCourseCodeMustBeUniqueException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
