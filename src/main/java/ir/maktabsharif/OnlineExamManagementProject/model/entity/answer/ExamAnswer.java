package ir.maktabsharif.OnlineExamManagementProject.model.entity.answer;


import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Exam;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "exam_answer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "answer_type", discriminatorType = DiscriminatorType.STRING)
public class ExamAnswer extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    }


