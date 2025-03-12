package ir.maktabsharif.OnlineExamManagementProject.service.strategy;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.DescriptiveExamAnswer;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.ExamAnswer;
import ir.maktabsharif.OnlineExamManagementProject.repository.answer.DescriptiveExamAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DescriptiveExamAnswerStrategy implements ExamAnswerStrategy {

    private final DescriptiveExamAnswerRepository repository;

    @Autowired
    public DescriptiveExamAnswerStrategy(DescriptiveExamAnswerRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(ExamAnswer answer) {
        return answer instanceof DescriptiveExamAnswer;
    }

    @Override
    public void save(ExamAnswer answer) {
        repository.save((DescriptiveExamAnswer) answer);
    }
}
