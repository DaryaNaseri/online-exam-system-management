package ir.maktabsharif.OnlineExamManagementProject.model.dto;

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
                    String password,
                    String username,
                    UserRole role,
                    RegistrationStatus status
    ) {
    }

    class EditUserRequest{
        @NotNull(message = "id must not be null")
        private Long id;
        private UserRole role;
        @Email(message = "enter email in this format: example@gmail.com ")
        private String email;
        @Size(min = 6,
                message = "username must be have least 6 characters")
        private String username;
        @Size(min = 6,
                message = "Password must be at least 6 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
        )
       private String password;
        @Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "The name must start with a capital letter, have at least two letters, and contain no spaces or numbers.")
        private String firstName;
        @Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "The name must start with a capital letter and not contain numbers.")
        private String lastName;
        private RegistrationStatus status;

        public static EditUserRequest builder() {
            return new EditUserRequest();
        }


        public @NotNull(message = "id must not be null") Long getId() {
            return id;
        }

        public void setId(@NotNull(message = "id must not be null") Long id) {
            this.id = id;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }

        public @Email(message = "enter email in this format: example@gmail.com ") String getEmail() {
            return email;
        }

        public void setEmail(@Email(message = "enter email in this format: example@gmail.com ") String email) {
            this.email = email;
        }

        public @Size(min = 6,
                message = "username must be have least 6 characters") String getUsername() {
            return username;
        }

        public void setUsername(@Size(min = 6,
                message = "username must be have least 6 characters") String username) {
            this.username = username;
        }

        public @Size(min = 6,
                message = "Password must be at least 6 characters long") @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
        ) String getPassword() {
            return password;
        }

        public void setPassword(@Size(min = 6,
                message = "Password must be at least 6 characters long") @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
        ) String password) {
            this.password = password;
        }

        public @Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "The name must start with a capital letter, have at least two letters, and contain no spaces or numbers.") String getFirstName() {
            return firstName;
        }

        public void setFirstName(@Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "The name must start with a capital letter, have at least two letters, and contain no spaces or numbers.") String firstName) {
            this.firstName = firstName;
        }

        public @Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "The name must start with a capital letter and not contain numbers.") String getLastName() {
            return lastName;
        }

        public void setLastName(@Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "The name must start with a capital letter and not contain numbers.") String lastName) {
            this.lastName = lastName;
        }

        public RegistrationStatus getStatus() {
            return status;
        }

        public void setStatus(RegistrationStatus status) {
            this.status = status;
        }

        public static class EditUserRequestBuilder {
            private EditUserRequest editUserRequest;

            public EditUserRequestBuilder() {
                editUserRequest = new EditUserRequest();
            }

            public EditUserRequestBuilder id(Long id) {
                editUserRequest.setId(id);
                return this;
            }

            public EditUserRequestBuilder username(String username) {
                editUserRequest.setUsername(username);
                return this;
            }

            public EditUserRequestBuilder password(String password) {
                editUserRequest.setPassword(password);
                return this;
            }

            public EditUserRequestBuilder firstName(String firstName) {
                editUserRequest.setFirstName(firstName);
                return this;
            }

            public EditUserRequestBuilder lastName(String lastName) {
                editUserRequest.setLastName(lastName);
                return this;
            }

            public EditUserRequestBuilder email(String email) {
                editUserRequest.setEmail(email);
                return this;
            }

            public EditUserRequestBuilder role(UserRole role) {
                editUserRequest.setRole(role);
                return this;
            }

            public EditUserRequestBuilder status(RegistrationStatus status) {
                editUserRequest.setStatus(status);
                return this;
            }

            public EditUserRequest build() {
                return editUserRequest;
            }

        }

    }



}
