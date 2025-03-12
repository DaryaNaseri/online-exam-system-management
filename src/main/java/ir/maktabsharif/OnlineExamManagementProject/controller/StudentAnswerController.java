package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.service.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
public class StudentAnswerController {
    @Autowired
    private StudentAnswerService studentAnswerService;

    @PostMapping("/{studentExamId}/{questionId}")
    public ResponseEntity<String> saveAnswer(
            @PathVariable Long studentExamId,
            @PathVariable Long questionId,
            @RequestBody String answer) {
        studentAnswerService.saveOrUpdateAnswer(studentExamId, questionId, answer);
        return ResponseEntity.ok("Answer saved");
    }
}
