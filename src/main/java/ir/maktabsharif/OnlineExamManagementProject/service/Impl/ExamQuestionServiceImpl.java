package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.ResourceNotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamQuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Exam;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.ExamQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.repository.ExamQuestionRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.ExamRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.QuestionRepository;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamQuestionServiceImpl implements ExamQuestionService {

    private final ExamQuestionRepository examQuestionRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;


    public ExamQuestionDto.Response setScoreForQuestion(Long examId, Long questionId, Double score) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        ExamQuestion examQuestion = examQuestionRepository.findByExamIdAndQuestionId(examId, questionId)
                .orElseThrow(() -> new ResourceNotFoundException("ExamQuestion not found"));

        examQuestion.setScore(score);

        return convertToDto(examQuestionRepository.save(examQuestion));
    }

    public List<ExamQuestionDto.Response> getExamQuestions(Long examId) {
        List<ExamQuestion> byExamId = examQuestionRepository.findByExamId(examId);
        if (byExamId.isEmpty()) {
            throw new ResourceNotFoundException("Exam not found,maybe this exam question list is empty");
        }
        return byExamId.stream().map(this::convertToDto).toList();
    }

    public Double calculateTotalScore(Long examId) {
        return examQuestionRepository.findByExamId(examId).stream()
                .mapToDouble(ExamQuestion::getScore)
                .sum();
    }

    public ExamQuestionDto.ScoreResponse getTotalExamScore(Long examId) {
        Exam found = examRepository.findById(examId).orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        Double totalScore = calculateTotalScore(examId);
        return new ExamQuestionDto.ScoreResponse(
                examId,
                found.getTitle(),
                totalScore
        );
    }

    @Override
    public void deleteQuestionFromExam(Long examId, Long questionId) {
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        ExamQuestion examQuestion = examQuestionRepository.findByExamIdAndQuestionId(examId, questionId).orElseThrow(() -> new ResourceNotFoundException("ExamQuestion not found"));

        examQuestionRepository.delete(examQuestion);
        //questionRepository.delete(question);
    }

    private ExamQuestionDto.Response convertToDto(ExamQuestion examQuestion) {
        return new ExamQuestionDto.Response(
                examQuestion.getExam().getId(),
                examQuestion.getExam().getTitle(),
                examQuestion.getQuestion().getId(),
                examQuestion.getQuestion().getContent(),
                examQuestion.getScore()
        );
    }
}

