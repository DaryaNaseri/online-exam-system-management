package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;


import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @ManyToMany(mappedBy = "students")
    private List<Course> courses = new ArrayList<>();

    public Student(String firstName,
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

    public Student(
                   String email,
                   String password,
                   String username,
                   UserRole role,
                   RegistrationStatus status
                   ) {
        super(email,password,username,role,status);
    }

    public Student(List<Course> courses) {
        this.courses = courses;
    }

    public Student() {

    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }


}
