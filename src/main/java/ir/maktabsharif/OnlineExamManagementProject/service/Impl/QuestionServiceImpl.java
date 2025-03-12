package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.*;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.OptionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.QuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.*;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.DescriptiveQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.repository.*;
import ir.maktabsharif.OnlineExamManagementProject.service.QuestionService;
import ir.maktabsharif.OnlineExamManagementProject.service.factory.QuestionFactory;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

import static ir.maktabsharif.OnlineExamManagementProject.utility.Util.convertToQuestionResponse;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionFactory questionFactory;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;


    public List<QuestionDto.ResponseDto> questionsBank(Long teacherId, Long courseId) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!(user instanceof Teacher teacher)) {
            throw new InvalidInputException("User is not a teacher");
        }

        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!foundCourse.getTeacher().getId().equals(user.getId())) {
            throw new TeacherNotAssignedToCourseException(teacherId, courseId);
        }

        List<Question> questionsByCourseAndTeacher =
                questionRepository.findQuestionsByCourseAndTeacher(courseId, teacherId);


        if (questionsByCourseAndTeacher.isEmpty()) {
            throw new ResourceIsEmptyException("resource might be empty");
        }

        List<QuestionDto.ResponseDto> questionDtos = new ArrayList<>();
        for (Question question : questionsByCourseAndTeacher) {
            if (question instanceof MultipleChoiceQuestion) {
                List<OptionDto.Response> optionDtos = new ArrayList<>();
                for (Options option : ((MultipleChoiceQuestion) question).getOptions()) {
                    optionDtos.add(new OptionDto.Response(option.getId(), option.getText()));
                }
                questionDtos.add(new QuestionDto.MultiChoiceQuestionResponse(question.getId(),question.getTitle(),question.getContent(),question.getTeacher().getId(),optionDtos));
            }
            if (question instanceof DescriptiveQuestion) {
                questionDtos.add(new QuestionDto.DescriptiveQuestionResponse(question.getId(),question.getTitle(),question.getContent(),question.getTeacher().getId()));
            }
        }

        return questionDtos;
    }

    @Override
    public QuestionDto.ResponseDto addQuestionFromBank(Long examId, Long questionId) {
        Exam foundExam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        Question foundQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        boolean exists = examQuestionRepository.existsByExamAndQuestion(foundExam, foundQuestion);
        if (exists) {
            throw new QuestionIsAlreadyExistsInExamException("Error, duplicate question");
        }

        this.save(foundQuestion);

        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExam(foundExam);
        examQuestion.setQuestion(foundQuestion);

        examQuestionRepository.save(examQuestion);

        return convertToQuestionResponse(foundQuestion);
    }

    @Override
    public List<QuestionDto.ResponseDto> getQuestionsForExam(Long examId) {
        Exam found = examRepository.findById(examId).orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamId(examId);

        if (examQuestions.isEmpty()) {
            throw new ResourceNotFoundException("Question not found");
        }

        List<Question> questions = new ArrayList<>();

        for (ExamQuestion examQuestion : examQuestions) {
            questions.add(examQuestion.getQuestion());
        }

        return questions.stream().map(Util::convertToQuestionResponse).toList();
    }

    @Override
    public QuestionDto.ResponseDto createQuestion(Long examId, QuestionDto.CreateRequest questionDto, Long teacherId) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Exam found = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if (!found.getTeacher().getId().equals(user.getId())) {
            throw new InvalidInputException("this teacher cant edit this exam");
        }

        Question question = questionFactory.createQuestion(questionDto.qType());
        question.setTitle(questionDto.title());
        question.setContent(questionDto.content());
        question.setTeacher((Teacher) user);
        System.out.println(questionDto.optionsDto());

        if (questionDto.isMultipleChoice()) {
            if (questionDto.optionsDto() == null || questionDto.optionsDto().isEmpty()) {
                throw new InvalidInputException("Options must be provided for multiple choice questions");
            }

            boolean hasCorrectAnswer =
                    questionDto.optionsDto().stream()
                            .anyMatch(OptionDto.CreateRequest::isCorrect);

            if (!hasCorrectAnswer) {
                throw new InvalidInputException("At least one correct answer must be provided for multiple choice questions");
            }


            List<Options> options = questionDto.optionsDto().stream()
                    .map(optionText -> {
                        Options option = new Options();
                        option.setCorrect(optionText.isCorrect());
                        option.setText(optionText.text());
                        option.setQuestion((MultipleChoiceQuestion) question);
                        return option;
                    })
                    .toList();
            ((MultipleChoiceQuestion) question).setOptions(options);

        }


        this.save(question);

        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExam(found);
        examQuestion.setQuestion(question);

        examQuestionRepository.save(examQuestion);

        return convertToQuestionResponse(question);
    }

    @Override
    public Page<QuestionDto.ResponseDto> searchQuestions(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<Question> questionPage =
                questionRepository.findByTitleContainingIgnoreCase(title, pageable)
                        .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        return questionPage.map(Util::convertToQuestionResponse);
    }

    @Override
    public List<QuestionDto.ResponseDto> searchQuestions(String title) {
        List<Question> byTitleContainingIgnoreCase = questionRepository.findByTitleContainingIgnoreCase(title);
        if (byTitleContainingIgnoreCase.isEmpty()) {
            throw new ResourceNotFoundException("Question not found,");
        }
        return byTitleContainingIgnoreCase.stream().map(Util::convertToQuestionResponse).toList();
    }

    @Override
    public QuestionDto.MultiChoiceQuestionResponse addOptionsToQuestion(Long questionId, List<OptionDto.CreateRequest> optionDtos) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        List<Options> list = optionDtos.stream()
                .map(optionText -> {
                    Options option = new Options();
                    option.setCorrect(optionText.isCorrect());
                    option.setText(optionText.text());
                    option.setQuestion((MultipleChoiceQuestion) question);
                    return option;
                })
                .toList();

        ((MultipleChoiceQuestion) question).getOptions().addAll(list);
        this.save(question);
        List<OptionDto.Response> optionsDto = new ArrayList<>();
        for (Options option : ((MultipleChoiceQuestion) question).getOptions()) {
            OptionDto.Response response = new OptionDto.Response(option.getId(), option.getText());
            optionsDto.add(response);
        }
        return new QuestionDto.MultiChoiceQuestionResponse(questionId, question.getTitle(), question.getContent(), question.getTeacher().getId(), optionsDto);


    }


    @Override
    public Question save(Question entity) {
        return questionRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
//todo
    }

//
//    public Question saveQuestionToExam(Question question, Long examId) {
//        Exam exam = examRepository.findById(examId)
//                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
//
//        if (!questionRepository.existsByContentAndTeacher_Id(question.getContent(), question.getTeacher().getId())) {
//            questionRepository.save(question);
//        }
//
//        exam.getQuestions().add(question);
//        examRepository.save(exam);
//
//        return question;
//    }


}
