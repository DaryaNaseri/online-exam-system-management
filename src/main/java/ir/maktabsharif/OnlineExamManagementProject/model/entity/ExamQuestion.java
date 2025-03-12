package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import jakarta.persistence.*;

@Entity
@Table(name = "exam_question")
public class ExamQuestion extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private Double score = 0.0;


    public ExamQuestion() {}

    public ExamQuestion(Exam exam, Question question, Double score) {
        this.exam = exam;
        this.question = question;
        this.score = score;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}

