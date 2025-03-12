package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.StudentAnswer;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamSessionService;
import ir.maktabsharif.OnlineExamManagementProject.service.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam-status")
public class ExamStatusController {
    @Autowired
    private StudentAnswerService studentAnswerService;
    @Autowired
    private ExamSessionService examSessionService;

    @GetMapping("/{studentExamId}")
    public ResponseEntity<Map<String, Object>> getExamStatus(@PathVariable Long studentExamId) {
        //int remainingTime = examSessionService.getRemainingTime(studentExamId);
        //List<StudentAnswer> answers = studentAnswerService.getAnswersForExam(studentExamId);

        Map<String, Object> response = new HashMap<>();
        //response.put("remainingTime", remainingTime);
        //response.put("answers", answers);

        return ResponseEntity.ok(response);
    }
}

