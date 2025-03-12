package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamQuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamQuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("exams/{examId}/questions")
public class ExamQuestionController {

    private final ExamQuestionService examQuestionService;

    public ExamQuestionController(ExamQuestionService examQuestionService) {
        this.examQuestionService = examQuestionService;
    }

    @PutMapping("{questionId}/update-score")
    @PreAuthorize("hasAuthority('UPDATE_SCORE')")
    public ResponseEntity<ExamQuestionDto.Response> setScoreForQuestion(
            @PathVariable Long examId,
            @PathVariable Long questionId,
            @RequestParam Double score
    ) {
        ExamQuestionDto.Response updatedExamQuestion = examQuestionService.setScoreForQuestion(examId, questionId, score);
        return ResponseEntity.status(HttpStatus.OK).body(updatedExamQuestion);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_EXAM_QUESTIONS')")
    public ResponseEntity<List<ExamQuestionDto.Response>> getExamQuestions(
            @PathVariable Long examId) {
        List<ExamQuestionDto.Response> questions = examQuestionService.getExamQuestions(examId);
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

    @GetMapping("/total-score")
    @PreAuthorize("hasAuthority('VIEW_TOTAL_SCORE')")
    public ResponseEntity<ExamQuestionDto.ScoreResponse> getTotalScore(
            @PathVariable Long examId) {
        ExamQuestionDto.ScoreResponse totalScore = examQuestionService.getTotalExamScore(examId);
        return ResponseEntity.status(HttpStatus.FOUND).body(totalScore);
    }

    @DeleteMapping("{questionId}")
    public ResponseEntity<Void> deleteQuestionFromExam(
            @PathVariable Long examId,
            @PathVariable Long questionId) {
        examQuestionService.deleteQuestionFromExam(examId,questionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

