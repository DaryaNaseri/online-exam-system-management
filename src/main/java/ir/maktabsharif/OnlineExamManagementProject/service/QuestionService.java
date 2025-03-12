package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.OptionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.QuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.service.base.BaseService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService extends BaseService<Question> {

    List<QuestionDto.ResponseDto> questionsBank(Long teacherId, Long courseId);

    QuestionDto.ResponseDto addQuestionFromBank(Long examId, Long questionId);

    List<QuestionDto.ResponseDto> getQuestionsForExam(Long examId);

    QuestionDto.ResponseDto createQuestion(Long examId, QuestionDto.CreateRequest questionDto, Long teacherId);

    Page<QuestionDto.ResponseDto> searchQuestions(String title, int page, int size);

    List<QuestionDto.ResponseDto> searchQuestions(String title);

    QuestionDto.MultiChoiceQuestionResponse addOptionsToQuestion(Long questionId, List<OptionDto.CreateRequest> optionDtos);
}
