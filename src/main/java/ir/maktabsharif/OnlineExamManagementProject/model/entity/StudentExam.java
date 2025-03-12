package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class StudentExam extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    private boolean completed = false;

    private LocalDateTime startTime; // زمان شروع آزمون

    @Column
    private LocalDateTime endTime; // زمان پایان آزمون

    @Column
    private Integer currentQuestionIndex = 0; // آخرین سوالی که کاربر مشاهده کرد

    @Column
    private Double totalScore; // نمره کل آزمون

    @Column(nullable = false)
    private boolean isGraded = false; // آیا تصحیح کامل شده؟

    public boolean isExamTimeExpired() {
        if (startTime == null || exam == null) {
            return false; // اگر اطلاعات ناقص باشد، ازمون منقضی در نظر نمی‌گیریم
        }
        return LocalDateTime.now()
                .isAfter(startTime.plusMinutes(exam.getDuration()));
    }


    public StudentExam(Student student, Exam exam, boolean completed) {
        this.student = student;
        this.exam = exam;
        this.completed = completed;
    }

    public StudentExam() {

    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(Integer currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public boolean isGraded() {
        return isGraded;
    }

    public void setGraded(boolean graded) {
        isGraded = graded;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
