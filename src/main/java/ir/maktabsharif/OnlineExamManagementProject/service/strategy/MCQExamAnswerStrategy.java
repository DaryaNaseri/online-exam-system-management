package ir.maktabsharif.OnlineExamManagementProject.service.strategy;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.ExamAnswer;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.MCQExamAnswer;
import ir.maktabsharif.OnlineExamManagementProject.repository.answer.MCQExamAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MCQExamAnswerStrategy implements ExamAnswerStrategy {

    private final MCQExamAnswerRepository repository;

    @Autowired
    public MCQExamAnswerStrategy(MCQExamAnswerRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(ExamAnswer answer) {
        return answer instanceof MCQExamAnswer;
    }

    @Override
    public void save(ExamAnswer answer) {
        repository.save((MCQExamAnswer) answer);
    }
}
