package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.service.ExamCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam-completion")
public class ExamCompletionController {
    @Autowired
    private ExamCompletionService examCompletionService;

    @PostMapping("/{studentExamId}/finish")
    public ResponseEntity<String> finishExam(@PathVariable Long studentExamId) {
        examCompletionService.completeExam(studentExamId);
        return ResponseEntity.ok("Exam finished");
    }
}

