package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "student_exam_answer")
public class StudentExamAnswer extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "student_exam_id", nullable = false)
    private StudentExam studentExam;

    @Column
    private Double assignedScore;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private Boolean isFinalized = false;

    @Column
    private String descriptiveAnswer;

    @ElementCollection
    @CollectionTable(name = "student_exam_answer_choices",
            joinColumns = @JoinColumn(name = "student-exam-answer_id"))
    @Column(name = "selected_choice")
    private List<String> selectedChoices;

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Boolean getFinalized() {
        return isFinalized;
    }

    public void setFinalized(Boolean finalized) {
        isFinalized = finalized;
    }

    public String getDescriptiveAnswer() {
        return descriptiveAnswer;
    }

    public void setDescriptiveAnswer(String descriptiveAnswer) {
        this.descriptiveAnswer = descriptiveAnswer;
    }

    public List<String> getSelectedChoices() {
        return selectedChoices;
    }

    public void setSelectedChoices(List<String> selectedChoices) {
        this.selectedChoices = selectedChoices;
    }

    public StudentExam getStudentExam() {
        return studentExam;
    }

    public void setStudentExam(StudentExam studentExam) {
        this.studentExam = studentExam;
    }

    public Double getAssignedScore() {
        return assignedScore;
    }

    public void setAssignedScore(Double assignedScore) {
        this.assignedScore = assignedScore;
    }
}

