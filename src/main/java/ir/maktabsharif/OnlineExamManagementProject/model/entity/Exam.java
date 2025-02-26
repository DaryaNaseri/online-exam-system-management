package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "exams")
public class Exam extends BaseEntity<Long> {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "How long does this exam take?")
    private Integer duration;

    @ManyToOne
    //todo: it should be nullable false
    @JoinColumn(name = "course_id")
    private Course course;

    public Exam(String title, String description, Integer duration, Course course) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.course = course;
    }

    public Exam() {
    }


    public static ExamBuilder builder() {
        return new ExamBuilder();
    }


    public @NotBlank(message = "Title must not be blank") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title must not be blank") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Description must not be blank") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Description must not be blank") String description) {
        this.description = description;
    }

    public @NotNull(message = "How long does this exam take?") Integer getDuration() {
        return duration;
    }

    public void setDuration(@NotNull(message = "How long does this exam take?") Integer duration) {
        this.duration = duration;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public static class ExamBuilder {
        private Exam exam;

        public ExamBuilder() {
            exam = new Exam();
        }

        public ExamBuilder title(String title) {
            exam.setTitle(title);
            return this;
        }

        public ExamBuilder description(String description) {
            exam.setDescription(description);
            return this;
        }

        public ExamBuilder duration(Integer duration) {
            exam.setDuration(duration);
            return this;
        }

        public ExamBuilder course(Course course) {
            exam.setCourse(course);
            return this;
        }

        public Exam build() {
            return exam;
        }

    }

}

