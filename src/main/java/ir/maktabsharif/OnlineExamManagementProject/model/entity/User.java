package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_role", discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")
public class User extends BaseEntity<Long> {

    @Pattern(
            regexp = "^[A-Z][a-z]+$",
            message = "The name must start with a capital letter, have at least two letters, and contain no spaces or numbers.")
    private String firstName;

    @Pattern(
            regexp = "^[A-Z][a-z]+$",
            message = "The name must start with a capital letter and not contain numbers.")
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "email must not be blank")
    @Email(message = "enter email in this format: example@gmail.com ")
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "password must not be blank")
    @Size(min = 6,
            message = "Password must be at least 6 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
    )
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "username must not be blank")
    @Size(min = 6,
            message = "username must be have least 6 characters")
    private String username;

    @NotNull(message = "UserRole must not be null")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @NotNull(message = "status must not be null")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    public User(Long aLong, LocalDateTime createdAt, LocalDateTime updatedAt, String firstName, String lastName, String email, String password, String username, UserRole role, RegistrationStatus status) {

        super(aLong, createdAt, updatedAt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.status = status;
    }

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String username, UserRole role, RegistrationStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.status = status;
    }

    public User(String email, String password, String username, UserRole role, RegistrationStatus status) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.status = status;

    }

    public static UserAccountBuilder builder() {
        return new UserAccountBuilder();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public static class UserAccountBuilder {
        private User account;

        public UserAccountBuilder() {
            account = new User();
        }

        public UserAccountBuilder username(String username) {
            account.setUsername(username);
            return this;
        }

        public UserAccountBuilder password(String password) {
            account.setPassword(password);
            return this;
        }

        public UserAccountBuilder firstName(String firstName) {
            account.setFirstName(firstName);
            return this;
        }

        public UserAccountBuilder lastName(String lastName) {
            account.setLastName(lastName);
            return this;
        }

        public UserAccountBuilder email(String email) {
            account.setEmail(email);
            return this;
        }

        public UserAccountBuilder role(UserRole role) {
            account.setRole(role);
            return this;
        }

        public UserAccountBuilder status(RegistrationStatus status) {
            account.setStatus(status);
            return this;
        }

        public User build() {
            return account;
        }

    }


}
