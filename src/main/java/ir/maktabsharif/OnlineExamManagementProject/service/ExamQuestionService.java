package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamQuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.ExamQuestion;

import java.util.List;

public interface ExamQuestionService {

    Double calculateTotalScore(Long examId);

    List<ExamQuestionDto.Response> getExamQuestions(Long examId);

    ExamQuestionDto.Response setScoreForQuestion(Long examId, Long questionId, Double score);

    ExamQuestionDto.ScoreResponse getTotalExamScore(Long examId);

    void deleteQuestionFromExam(Long examId, Long questionId);
}
