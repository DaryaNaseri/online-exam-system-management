package ir.maktabsharif.OnlineExamManagementProject.controller;


import ir.maktabsharif.OnlineExamManagementProject.model.dto.AnswerDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.QuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.ExamQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExam;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExamAnswer;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamSessionService;
import ir.maktabsharif.OnlineExamManagementProject.service.StudentExamAnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam-session")
public class ExamSessionController {

    private ExamSessionService examSessionService;
private StudentExamAnswerService studentExamAnswerService;

    public ExamSessionController(ExamSessionService examSessionService, StudentExamAnswerService studentExamAnswerService, Map<Long, StudentExam> activeExams) {
        this.examSessionService = examSessionService;
        this.studentExamAnswerService = studentExamAnswerService;
        this.activeExams = activeExams;
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
    public ResponseEntity<QuestionDto.ResponseDto> getCurrentQuestion(
            @PathVariable Long studentId,
            @PathVariable Long examId) {
        QuestionDto.ResponseDto question =
                examSessionService.getCurrentQuestion(studentId,examId, 0);
        return ResponseEntity.status(HttpStatus.FOUND).body(question);
    }

    @GetMapping("/{examId}/student/{studentId}/next-question")
    public ResponseEntity<Question> getNextQuestion(
            @PathVariable Long examId,
            @PathVariable Long studentId) {

        ExamQuestion nextQuestion = examSessionService.nextQuestion(examId, studentId);
        return ResponseEntity.ok(nextQuestion.getQuestion());
    }

    @GetMapping("/{examId}/student/{studentId}/previous-question")
    public ResponseEntity<Question> previousQuestion(
            @PathVariable Long studentId,
            @PathVariable Long examId) {

        ExamQuestion previousQuestion = examSessionService.previousQuestion(examId, studentId);
        return ResponseEntity.ok(previousQuestion.getQuestion());
    }



    @PostMapping("/{examId}/student/{studentId}/finish")
    public ResponseEntity<String> finishExam(
            @PathVariable Long examId,
            @PathVariable Long studentId) {
        examSessionService.finishExam(examId,studentId);
        return ResponseEntity.status(HttpStatus.OK).body("finished exam successfully");
    }

    //..................................


    private Map<Long, StudentExam> activeExams = new HashMap<>();




    @PostMapping("/question/answer")
    public ResponseEntity<AnswerDto.Response> saveAnswer(
            @RequestBody AnswerDto.SaveRequest answer) {
        AnswerDto.Response response = studentExamAnswerService.saveAnswer(answer);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{examId}/student/{studentId}/answers")
    public ResponseEntity<List<AnswerDto.AnswerResponse>> getStudentAnswers(
            @PathVariable Long examId,
            @PathVariable Long studentId) {
        List<AnswerDto.AnswerResponse> answers = examSessionService.getStudentAnswers(studentId, examId);
        return ResponseEntity.status(HttpStatus.FOUND).body(answers);
    }



    @GetMapping("/{examId}/time")
    @PreAuthorize("hasAuthority('GET_EXAM_ACCESS')")
    public ResponseEntity<String> getRemainingTime(
            @PathVariable Long examId) {
        return ResponseEntity.ok(examSessionService.getRemainingTime(examId));
    }



}
