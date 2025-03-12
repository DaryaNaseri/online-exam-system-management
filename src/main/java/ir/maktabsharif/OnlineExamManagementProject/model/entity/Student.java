package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.enums.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.enums.UserRole;
import ir.maktabsharif.OnlineExamManagementProject.model.permision.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @ManyToMany(mappedBy = "students")
    private List<Course> courses = new ArrayList<>();

    public Student(
                   String email,
                   String password,
                   String username,
                   UserRole role,
                   RegistrationStatus status
                   ) {
        super(email,password,username,role,status);
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
