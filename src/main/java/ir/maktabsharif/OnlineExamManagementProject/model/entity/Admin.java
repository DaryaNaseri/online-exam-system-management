package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.enums.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@DiscriminatorValue("ADMIN")

public class Admin extends User {


    public Admin(String email, String password, String username, UserRole userRole, RegistrationStatus registrationStatus) {
        super(email, password, username, userRole, registrationStatus);
    }

    public Admin() {

    }
}
