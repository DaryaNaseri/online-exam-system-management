package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidInputException;
import ir.maktabsharif.OnlineExamManagementProject.exception.ResourceNotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.AnswerDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.*;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.DescriptiveQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentExamAnswerService {

    private ExamQuestionRepository examQuestionRepository;
    private StudentExamAnswerRepository answerRepository;
private ExamRepository examRepository;
private StudentRepository studentRepository;
private QuestionRepository questionRepository;
private OptionsRepository optionsRepository;
private StudentExamRepository studentExamRepository;

    public StudentExamAnswerService(StudentExamRepository studentExamRepository,ExamQuestionRepository examQuestionRepository,OptionsRepository optionsRepository,StudentExamAnswerRepository answerRepository, ExamRepository examRepository, StudentRepository studentRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.studentExamRepository = studentExamRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.optionsRepository = optionsRepository;
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
    }

    public AnswerDto.Response saveAnswer(AnswerDto.SaveRequest dto) {
        // دریافت اطلاعات اولیه
        Question question = questionRepository.findById(dto.questionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Student student = studentRepository.findById(dto.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Exam exam = examRepository.findById(dto.examId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        StudentExam studentExam = studentExamRepository.findByExamIdAndStudentId(dto.examId(),dto.studentId()).orElseThrow(() -> new ResourceNotFoundException("StudentExam not found"));


        StudentExamAnswer answer = new StudentExamAnswer();
        answer.setStudentExam(studentExam);
        answer.setStudent(student);
        answer.setExam(exam);
        answer.setQuestion(question);

        // اگر سوال تشریحی است
        if (question instanceof DescriptiveQuestion) {
            if (dto.descriptiveAnswer() == null || dto.descriptiveAnswer().trim().isEmpty()) {
                throw new IllegalArgumentException("Descriptive answer cannot be empty.");
            }
            answer.setDescriptiveAnswer(dto.descriptiveAnswer());
        }
        // اگر سوال چندگزینه‌ای است
        else if (question instanceof MultipleChoiceQuestion) {
            if (dto.selectedChoiceIds() == null || dto.selectedChoiceIds().isEmpty()) {
                throw new IllegalArgumentException("At least one choice must be selected.");
            }

            // بررسی گزینه‌های انتخاب‌شده و بررسی صحت پاسخ‌ها
            List<Options> selectedOptions = optionsRepository.findAllById(dto.selectedChoiceIds());
            answer.setSelectedChoices(selectedOptions.stream()
                    .map(Options::getText)
                    .collect(Collectors.toList()));

            ExamQuestion examQuestion = examQuestionRepository.findByExamIdAndQuestionId(dto.examId(),dto.questionId()).orElseThrow(() -> new ResourceNotFoundException("ExamQuestion not found"));

            // تصحیح خودکار سوالات چندگزینه‌ای
            boolean isCorrect = selectedOptions.stream().allMatch(Options::getCorrect);
            answer.setAssignedScore(isCorrect ? examQuestion.getScore() : 0.0); // فرض کنیم ۱ نمره داشته باشد
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

