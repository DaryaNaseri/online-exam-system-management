package ir.maktabsharif.OnlineExamManagementProject.model.entity.answer;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MCQExamAnswer extends ExamAnswer {
    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private Options selectedOption;
}

