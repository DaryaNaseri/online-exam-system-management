package ir.maktabsharif.OnlineExamManagementProject.model.entity.question;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "QType", discriminatorType = DiscriminatorType.STRING)
public abstract class Question extends BaseEntity<Long> {

    @Column(nullable = false)
    @NotBlank(message = "title is required, fill it")
    @Size(min = 1, max = 50,message = "title must be short that 50 characters.")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "content of question must not be empty.")
    private String content;

    @NotNull(message = "which teacher is creating this question?")
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

//    @ManyToMany(mappedBy = "questions")
//    private List<Exam> exams= new ArrayList<>();

    public @Size(min = 1, max = 50, message = "title must be short that 50 characters.") String getTitle() {
        return title;
    }

    public void setTitle(@Size(min = 1, max = 50, message = "title must be short that 50 characters.") String title) {
        this.title = title;
    }

    public @NotBlank(message = "content of question must not be empty.") String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "content of question must not be empty.") String content) {
        this.content = content;
    }

    public @NotNull(message = "which teacher is creating this question?") Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(@NotNull(message = "which teacher is creating this question?") Teacher teacher) {
        this.teacher = teacher;
    }

//    public List<Exam> getExams() {
//        return exams;
//    }
//
//    public void setExams(List<Exam> exams) {
//        this.exams = exams;
//    }
}
