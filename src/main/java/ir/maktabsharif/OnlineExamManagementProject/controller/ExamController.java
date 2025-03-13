package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.StudentExamAnswerDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.StudentExamDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExam;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamService;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("exams/course/{courseId}/teacher/{teacherId}")
public class ExamController {

    private final ExamService examService;
private final ExamSessionService studentExamService;

    public ExamController(ExamSessionService studentExamService,ExamService examService) {
        this.examService = examService;
        this.studentExamService = studentExamService;
    }


    @PostMapping("create-exam")
    @PreAuthorize("hasAuthority('CREATE_EXAM')")
    public ResponseEntity<ExamDto.Response> createExam(
            @PathVariable Long courseId,
            @PathVariable Long teacherId,
            @RequestBody ExamDto.CreateRequest examDTO) {
        ExamDto.Response createdExam = examService.create(courseId, teacherId, examDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
    }


    @GetMapping("all_exam")
    @PreAuthorize("hasAuthority('ALL_EXAMS_OF_COURSE')")
    public ResponseEntity<List<ExamDto.Response>> getExamsByCourse(
            @PathVariable Long courseId,
            @PathVariable Long teacherId) {
        List<ExamDto.Response> byCourse = examService.findByCourse(courseId, teacherId);
        return ResponseEntity.status(HttpStatus.FOUND).body(byCourse);
    }


    @PutMapping("update")
    @PreAuthorize("hasAuthority('EDIT_EXAM')")
    public ResponseEntity<ExamDto.Response> updateExam(
            @RequestBody ExamDto.EditRequest examDto,
            @PathVariable Long teacherId,
            @PathVariable Long courseId
    ) {
        ExamDto.Response update = examService.update(examDto, teacherId, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }


    @DeleteMapping("delete-exam/{examId}")
    @PreAuthorize("hasAuthority('DELETE_EXAM')")
    public ResponseEntity<Void> deleteExam(
            @PathVariable Long examId,
            @PathVariable Long teacherId,
            @PathVariable Long courseId
    ) {
        examService.delete(examId, teacherId, courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("available-exam/student/{studentId}")
    @PreAuthorize("hasAuthority('VIEW_AVAILABLE_EXAM')")
    public ResponseEntity<List<ExamDto.Response>> getAvailableExams(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(examService.findAvailableExams(courseId, studentId));
    }


    @GetMapping("/completed")
    @PreAuthorize("hasAuthority('VIEW_COMPLETED_EXAM')")
    public ResponseEntity<List<StudentExamDto.Response>> getCompletedExams(
            @PathVariable Long teacherId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(studentExamService.getCompletedExamsByTeacher(teacherId));
    }


    @PostMapping("/score")
    @PreAuthorize("hasAuthority('GIVE_SCORE')")
    public ResponseEntity<StudentExamAnswerDto.Response> updateEssayScore(
            @RequestBody StudentExamAnswerDto.Request request) {
        StudentExamAnswerDto.Response response = studentExamService.updateDescriptiveScore(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

