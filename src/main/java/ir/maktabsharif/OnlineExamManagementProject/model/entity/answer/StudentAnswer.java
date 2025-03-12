package ir.maktabsharif.OnlineExamManagementProject.model.entity.answer;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExam;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class StudentAnswer extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "student-exam-id")
    private StudentExam studentExam;

    @ManyToOne
    @JoinColumn(name = "question-id")
    private Question question;

    private String answer;

    public StudentExam getStudentExam() {
        return studentExam;
    }

    public void setStudentExam(StudentExam studentExam) {
        this.studentExam = studentExam;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
