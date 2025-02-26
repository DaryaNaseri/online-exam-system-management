package ir.maktabsharif.OnlineExamManagementProject.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.maktabsharif.OnlineExamManagementProject.model.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import jakarta.validation.constraints.*;

public interface UserDto {

    record SignupRequest(
            @NotNull(message = "select your role (TEACHER/STUDENT)")
            UserRole userRole,

            @NotBlank(message = "email must not be blank")
            @Email(message = "enter email in this format: example@gmail.com ")
            String email,

            @NotBlank(message = "username must not be blank")
            @Size(min = 6, message = "username must be have least 6 characters")
            String username,

            @NotBlank(message = "password must not be blank")
            @Size(min = 6, message = "Password must be at least 6 characters long")
            @Pattern(
                    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
                    message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
            )
            String password) {
    }

    record LoginRequest(
            @NotBlank
            @Size(min = 6, message = "username must be have least 6 characters")
            String username,
            @NotBlank(message = "password could not be null")
            @Size(min = 6, message = "Password must be at least 6 characters long")
            @Pattern(
                    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
                    message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
            )
            String password) {
    }

    record Response(Long id,
                    String firstName,
                    String lastName,
                    String email,
                    @JsonIgnore
                    String password,
                    String username,
                    UserRole role,
                    RegistrationStatus status
    ) {
    }

    record EditUserRequest(
            @NotNull(message = "id must not be null")
            Long id,
            UserRole role,
            @Email(message = "enter email in this format: example@gmail.com ")
            String email,
            @Size(min = 6,
                    message = "username must be have least 6 characters")
            String username,
            @Size(min = 6,
                    message = "Password must be at least 6 characters long")
            @Pattern(
                    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
                    message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
            )
            String password,
            @Pattern(
                    regexp = "^[A-Z][a-z]+$",
                    message = "The name must start with a capital letter, have at least two letters, and contain no spaces or numbers.")
            String firstName,
            @Pattern(
                    regexp = "^[A-Z][a-z]+$",
                    message = "The name must start with a capital letter and not contain numbers.")
            String lastName,
            RegistrationStatus status
    ) {
    }


}
