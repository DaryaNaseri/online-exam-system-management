package ir.maktabsharif.OnlineExamManagementProject.service.strategy;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.ExamAnswer;

public interface ExamAnswerStrategy {

    boolean supports(ExamAnswer answer);

    void save(ExamAnswer answer);
}

