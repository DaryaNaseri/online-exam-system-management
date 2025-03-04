package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.exception.*;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameOrEmailMustBeUniqueException.class)
    public ResponseEntity<ResponseDto.ApiError> handleUsernameAndEmailAlreadyTaken(UsernameOrEmailMustBeUniqueException e) {
        ResponseDto.ApiError apiError = new ResponseDto.ApiError(
                HttpStatus.CONFLICT.value(),
                "username or email have been taken, enter another one.",
                e.getMessage());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto.ApiError> handleNotFound(NotFoundException e) {
        ResponseDto.ApiError apiError = new ResponseDto.ApiError(
                HttpStatus.NOT_FOUND.value(),
                "We couldn't find the requested.",
                e.getMessage()
        );
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(CourseCodeMustBeUniqueException.class)
    public ResponseEntity<ResponseDto.ApiError> handleCourseCodeAlreadyTaken(CourseCodeMustBeUniqueException e) {
        ResponseDto.ApiError apiError = new ResponseDto.ApiError(
                HttpStatus.CONFLICT.value(),
                "course code has been used, enter another one.",
                e.getMessage());
        return ResponseEntity.badRequest().body(apiError);
    }


    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<ResponseDto.ApiError> handleAccountNotVerifiedException(AccountNotVerifiedException e) {
        ResponseDto.ApiError errorResponse = new ResponseDto.ApiError(
                HttpStatus.FORBIDDEN.value(),
                "Account not verified",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(DuplicateExamTitleException.class)
    public ResponseEntity<ResponseDto.ApiError> handleDuplicateExamTitleException(DuplicateExamTitleException e) {
        ResponseDto.ApiError errorResponse = new ResponseDto.ApiError(
                HttpStatus.CONFLICT.value(),
                "Exam title already exists",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseDto.ApiError> handleInvalidInputException(InvalidInputException e) {
        ResponseDto.ApiError errorResponse = new ResponseDto.ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid input",
                e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ResponseDto.ApiError> handleInvalidPasswordException(InvalidPasswordException e) {
        ResponseDto.ApiError errorResponse = new ResponseDto.ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid password, password must be at least 6 characters and include capital letters is lowercase letters.",
                e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }






}
