package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("TEACHER")

public class Teacher extends User {

    @OneToMany(mappedBy = "teacher")
    private List<Course> courses = new ArrayList<>();

    public Teacher(String firstName,
                   String lastName,
                   String email,
                   String password,
                   String username,
                   UserRole role,
                   RegistrationStatus status,
                   List<Course> courses) {
        super(firstName, lastName, email, password, username, role, status);
        this.courses = courses;
    }

    public Teacher(
                   String email,
                   String password,
                   String username,
                   UserRole role,
                   RegistrationStatus status) {
        super(email,password,username,role,status);
    }
    public Teacher(List<Course> courses) {
        this.courses = courses;
    }

    public Teacher() {

    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
