package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.ExamAnswer;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamAnswerService;
import ir.maktabsharif.OnlineExamManagementProject.service.strategy.ExamAnswerStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamAnswerServiceImpl implements ExamAnswerService {

    private final List<ExamAnswerStrategy> strategies;


    public void saveAnswer(ExamAnswer answer) {
        for (ExamAnswerStrategy strategy : strategies) {
            if (strategy.supports(answer)) {
                strategy.save(answer);
                return;
            }
        }
        throw new IllegalArgumentException("Unsupported answer type");
    }
}
