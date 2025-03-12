package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    //todo: it should be nullable false
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "exam")
    private List<StudentExam> studentExams;

    @OneToMany(mappedBy = "exam")
    private List<ExamQuestion> examQuestions;


//    @ManyToMany
//    @JoinTable(
//            name = "j_exams_questions",
//            joinColumns = @JoinColumn(name = "exam_id"),
//            inverseJoinColumns = @JoinColumn(name = "question_id")
//    )
//    private List<Question> questions= new ArrayList<>();


//    private Double defaultScore;


    public Exam(String title, String description, Integer duration, Course course) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.course = course;
    }

    public Exam() {
    }

    public Exam(String title, String description, Integer duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }


    public static ExamBuilder builder() {
        return new ExamBuilder();
    }

//      public List<Question> getQuestions() {
//        return questions;
//    }

//    public void setQuestions(List<Question> questions) {
//        this.questions = questions;
//    }

//    public Double getDefaultScore() {
//        return defaultScore;
//    }
//
//    public void setDefaultScore(Double defaultScore) {
//        this.defaultScore = defaultScore;
//    }


    public List<StudentExam> getStudentExams() {
        return studentExams;
    }

    public void setStudentExams(List<StudentExam> studentExams) {
        this.studentExams = studentExams;
    }

    public List<ExamQuestion> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(List<ExamQuestion> examQuestions) {
        this.examQuestions = examQuestions;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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

        public ExamBuilder teacher(Teacher teacher){
            exam.setTeacher(teacher);
            return this;
        }

        public Exam build() {
            return exam;
        }

    }

}

