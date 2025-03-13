package ir.maktabsharif.OnlineExamManagementProject.controller;


import ir.maktabsharif.OnlineExamManagementProject.model.dto.AnswerDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.QuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.StudentExamAnswerDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExam;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamService;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamSessionService;
import ir.maktabsharif.OnlineExamManagementProject.service.StudentExamAnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam-session")
public class ExamSessionController {

    private ExamSessionService examSessionService;
    private StudentExamAnswerService studentExamAnswerService;
    private ExamService examService;

    public ExamSessionController(ExamService examService, ExamSessionService examSessionService, StudentExamAnswerService studentExamAnswerService, Map<Long, StudentExam> activeExams) {
        this.examSessionService = examSessionService;
        this.examService = examService;
        this.studentExamAnswerService = studentExamAnswerService;
    }

    @PostMapping("/{examId}/start/student/{studentId}")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<String> startExam(
            @PathVariable Long examId,
            @PathVariable Long studentId) {
        examSessionService.startExam(examId, studentId);
        return ResponseEntity.status(HttpStatus.OK).body("exam started successfully.");
    }

    @GetMapping("/question/exam/{examId}/student/{studentId}")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<QuestionDto.ResponseDto> getCurrentQuestion(
            @PathVariable Long studentId,
            @PathVariable Long examId) {
        QuestionDto.ResponseDto question =
                examSessionService.getFirstQuestion(studentId, examId, 0);
        return ResponseEntity.status(HttpStatus.FOUND).body(question);
    }

    @GetMapping("/{examId}/student/{studentId}/next-question")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<QuestionDto.ResponseDto> getNextQuestion(
            @PathVariable Long examId,
            @PathVariable Long studentId) {

        QuestionDto.ResponseDto nextQuestion = examSessionService.nextQuestion(examId, studentId);
        return ResponseEntity.status(HttpStatus.FOUND).body(nextQuestion);
    }

    @GetMapping("/{examId}/student/{studentId}/previous-question")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<QuestionDto.ResponseDto> previousQuestion(
            @PathVariable Long studentId,
            @PathVariable Long examId) {

        QuestionDto.ResponseDto previousQuestion = examSessionService.previousQuestion(examId, studentId);
        return ResponseEntity.status(HttpStatus.FOUND).body(previousQuestion);
    }


    @PostMapping("/{examId}/student/{studentId}/finish")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<StudentExamAnswerDto.Response> finishExam(
            @PathVariable Long examId,
            @PathVariable Long studentId) {
        StudentExamAnswerDto.Response response = examSessionService.finishExam(examId, studentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/question/answer")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<AnswerDto.Response> saveAnswer(
            @RequestBody AnswerDto.SaveRequest answer) {
        AnswerDto.Response response = studentExamAnswerService.saveAnswer(answer);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{examId}/student/{studentId}/answers")
    @PreAuthorize("hasAuthority('VIEW_STUDENT_EXAM_ANSWERS')")
    public ResponseEntity<List<AnswerDto.AnswerResponse>> getStudentAnswers(
            @PathVariable Long examId,
            @PathVariable Long studentId) {
        List<AnswerDto.AnswerResponse> answers =
                examSessionService.getStudentAnswers(studentId, examId);
        return ResponseEntity.status(HttpStatus.FOUND).body(answers);
    }

    @GetMapping("exam/{examId}/student/{studentId}/total-score")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<Double> getTotalScore(
            @PathVariable Long examId,
            @PathVariable Long studentId
    ) {
        Double score = examService.completedExamTotalScore(examId, studentId);
        return ResponseEntity.status(HttpStatus.FOUND).body(score);
    }


    @GetMapping("/{examId}/time")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<String> getRemainingTime(
            @PathVariable Long examId) {
        return ResponseEntity.ok(examSessionService.getRemainingTime(examId));
    }


}
