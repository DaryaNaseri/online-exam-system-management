package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.ExamExpiredException;
import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidInputException;
import ir.maktabsharif.OnlineExamManagementProject.exception.ResourceNotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.AnswerDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.*;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.DescriptiveQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.repository.*;
import ir.maktabsharif.OnlineExamManagementProject.service.StudentExamAnswerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentExamAnswerServiceImpl implements StudentExamAnswerService {

    private ExamQuestionRepository examQuestionRepository;
    private StudentExamAnswerRepository answerRepository;
    private ExamRepository examRepository;
    private StudentRepository studentRepository;
    private QuestionRepository questionRepository;
    private OptionsRepository optionsRepository;
    private StudentExamRepository studentExamRepository;

    public StudentExamAnswerServiceImpl(StudentExamRepository studentExamRepository, ExamQuestionRepository examQuestionRepository, OptionsRepository optionsRepository, StudentExamAnswerRepository answerRepository, ExamRepository examRepository, StudentRepository studentRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.studentExamRepository = studentExamRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.optionsRepository = optionsRepository;
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
    }

    public AnswerDto.Response saveAnswer(AnswerDto.SaveRequest dto) {
        Question question = questionRepository.findById(dto.questionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Student student = studentRepository.findById(dto.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Exam exam = examRepository.findById(dto.examId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        StudentExam studentExam =
                studentExamRepository
                        .findByExamIdAndStudentId(dto.examId(), dto.studentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Exam not started"));

        ExamQuestion examQuestion =
                examQuestionRepository.findByExamIdAndQuestionId(exam.getId(), question.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("exam question not found"));

        if (studentExam.isExamTimeExpired()) {
            throw new ExamExpiredException("exam time expired");
        }

        StudentExamAnswer answer = answerRepository
                .findByStudentExamIdAndQuestionId(studentExam.getId(), question.getId())
                .orElse(new StudentExamAnswer());

        answer.setAssignedScore(null);
        answer.setStudentExam(studentExam);
        answer.setStudent(student);
        answer.setExam(exam);
        answer.setQuestion(question);


        if (question instanceof DescriptiveQuestion) {
            if (dto.descriptiveAnswer() == null || dto.descriptiveAnswer().trim().isEmpty()) {
                throw new IllegalArgumentException("Descriptive answer cannot be empty.");
            }
            answer.setDescriptiveAnswer(dto.descriptiveAnswer());
        }
        else if (question instanceof MultipleChoiceQuestion) {
            if (dto.selectedChoiceIds() == null || dto.selectedChoiceIds().isEmpty()) {
                throw new IllegalArgumentException("At least one choice must be selected.");
            }

            List<Options> selectedOptions = optionsRepository.findAllById(dto.selectedChoiceIds());
            answer.setSelectedChoices(selectedOptions.stream()
                    .map(Options::getText)
                    .collect(Collectors.toList()));

            boolean isCorrect =
                    selectedOptions.stream().allMatch(Options::getCorrect);
            answer.setAssignedScore(isCorrect ? examQuestion.getScore() : 0.0);
            answer.setFinalized(true);

        } else {
            throw new InvalidInputException("Unknown question type.");
        }

        answerRepository.save(answer);

        return new AnswerDto.Response(student.getId(),
                exam.getId(),
                question.getId(),
                true);
    }
}

