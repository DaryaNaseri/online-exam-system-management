package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.OptionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.QuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.service.Impl.QuestionServiceImpl;
import ir.maktabsharif.OnlineExamManagementProject.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("update-exam/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("course/{courseId}/teacher/{teacherId}/questions-bank")
    @PreAuthorize("hasAuthority('VIEW_QUESTION_BANK')")
    public ResponseEntity<List<QuestionDto.ResponseDto>> questionsBank(
            @PathVariable Long courseId,
            @PathVariable Long teacherId) {
        List<QuestionDto.ResponseDto> found = questionService.questionsBank(teacherId, courseId);
        return ResponseEntity.status(HttpStatus.FOUND).body(found);
    }

    @PostMapping("exam/{examId}/add-q-from-bank/{questionId}")
    @PreAuthorize("hasAuthority('ADD_QUESTION_TO_EXAM')")
    public ResponseEntity<QuestionDto.ResponseDto> addQuestionToExam(
            @PathVariable Long examId,
            @PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.addQuestionFromBank(examId, questionId));
    }

    @PostMapping("exams/{examId}/teacher/{teacherId}/create-and-add-questions")
    @PreAuthorize("hasAuthority('ADD_QUESTION_TO_EXAM')")
    public ResponseEntity<QuestionDto.ResponseDto> createQuestionAndAddToExam(
            @PathVariable Long examId,
            @Valid @RequestBody QuestionDto.CreateRequest questionDto,
            @PathVariable Long teacherId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        QuestionDto.ResponseDto savedQ = questionService.createQuestion(examId, questionDto, teacherId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQ);
    }


    @PutMapping("{questionId}/add-options")
    public ResponseEntity<QuestionDto.MultiChoiceQuestionResponse> addOptionsToQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody List<OptionDto.CreateRequest> optionDtos,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        QuestionDto.MultiChoiceQuestionResponse question = questionService.addOptionsToQuestion(questionId, optionDtos);
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }


    @GetMapping("search")
    @PreAuthorize("hasAuthority('SEARCH_BY_TITLE')")
    public ResponseEntity<List<QuestionDto.ResponseDto>> searchQuestions(
            @RequestParam String title) {
        List<QuestionDto.ResponseDto> questions = questionService.searchQuestions(title);
        return ResponseEntity.status(HttpStatus.FOUND).body(questions);
    }


    @GetMapping("page-search")
    @PreAuthorize("hasAuthority('SEARCH_BY_TITLE')")
    public ResponseEntity<Page<QuestionDto.ResponseDto>> searchQuestionsInPage(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<QuestionDto.ResponseDto> questions = questionService.searchQuestions(title, page, size);
        return ResponseEntity.ok(questions);
    }


    @GetMapping("/{examId}")
    public ResponseEntity<List<QuestionDto.ResponseDto>> getExamQuestions(@PathVariable Long examId) {
        return ResponseEntity.ok(questionService.getQuestionsForExam(examId));
    }




//    @PutMapping("{questionId}")
//    public ResponseEntity<Void> updateQuestion(@PathVariable Long examId, @PathVariable Long questionId, @RequestBody QuestionDTO questionDTO) {
//        return ResponseEntity.ok(questionService.updateQuestion(examId, questionId, questionDTO));
//    }
////
//    @DeleteMapping("/{examId}/questions/{questionId}")
//    public ResponseEntity<Void> deleteQuestion(@PathVariable Long examId, @PathVariable Long questionId) {
//
//        examQService.deleteQuestion(examId, questionId);
//        return ResponseEntity.noContent().build();
//    }


}
