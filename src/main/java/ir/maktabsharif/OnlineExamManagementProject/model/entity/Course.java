package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "title")
public class Course extends BaseEntity<Long> {

    @NotBlank(message = "title must not be blank")
    private String title;

    @NotBlank(message = "course must have code")
    private String courseCode;

    @NotNull(message = "when course will started? ")
    private LocalDate startDate;

    @NotNull(message = "when course will end? ")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "j_course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();

    public static CourseBuilder builder() {
        return new CourseBuilder();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public static class CourseBuilder {
        private Course course;

        public CourseBuilder() {
            course = new Course();
        }

        public CourseBuilder title(String title) {
            course.setTitle(title);
            return this;
        }

        public CourseBuilder courseCode(String courseCode) {
            course.setCourseCode(courseCode);
            return this;
        }

        public CourseBuilder startDate(LocalDate startDate) {
            course.setStartDate(startDate);
            return this;
        }

        public CourseBuilder endDate(LocalDate endDate) {
            course.setEndDate(endDate);
            return this;
        }

        public CourseBuilder teacher(Teacher teacher) {
            course.setTeacher(teacher);
            return this;
        }

        public CourseBuilder students(List<Student> students) {
            course.setStudents(students);
            return this;
        }

        public Course build() {
            return course;
        }

    }
}
