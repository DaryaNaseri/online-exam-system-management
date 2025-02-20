package ir.maktabsharif.OnlineExamManagementProject.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
