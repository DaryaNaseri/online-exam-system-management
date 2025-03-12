package ir.maktabsharif.OnlineExamManagementProject.model.entity.answer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class DescriptiveExamAnswer extends ExamAnswer {

    @Column(name = "answer_text")
    private String answerText;
}

