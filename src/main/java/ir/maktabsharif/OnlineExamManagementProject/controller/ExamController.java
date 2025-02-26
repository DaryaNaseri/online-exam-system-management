package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamDto;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/course/{courseId}/create-exam")
    public ResponseEntity<ExamDto.Response> createExam(@PathVariable Long courseId, @RequestBody ExamDto.CreateRequest examDto) {
        ExamDto.Response createdExam = examService.create(courseId, examDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ExamDto.Response>> exams(@PathVariable Long courseId) {
        List<ExamDto.Response> byCourse = examService.findByCourse(courseId);
        return ResponseEntity.status(HttpStatus.FOUND).body(byCourse);
    }

    @PutMapping("/course/{courseId}/update")
    public ResponseEntity<ExamDto.Response> updateExam(@PathVariable Long courseId, @RequestBody ExamDto.EditRequest examDto) {
        ExamDto.Response update = examService.update(examDto);
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

    @DeleteMapping("/course/{courseId}/delete-exam/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long courseId, @PathVariable Long examId) {
        examService.delete(examId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
