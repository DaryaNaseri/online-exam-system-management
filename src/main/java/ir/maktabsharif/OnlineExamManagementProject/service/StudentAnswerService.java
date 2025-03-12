package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.exception.ResourceNotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExam;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.StudentAnswer;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.repository.QuestionRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.StudentAnswerRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.StudentExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAnswerService {
    private StudentAnswerRepository studentAnswerRepository;
    private StudentExamRepository studentExamRepository;
    private QuestionRepository questionRepository;

    public StudentAnswerService(StudentAnswerRepository studentAnswerRepository, StudentExamRepository studentExamRepository, QuestionRepository questionRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
        this.studentExamRepository = studentExamRepository;
        this.questionRepository = questionRepository;
    }

    public void saveOrUpdateAnswer(Long studentExamId, Long questionId, String answer) {
        StudentExam studentExam = studentExamRepository.findById(studentExamId).orElseThrow(() -> new ResourceNotFoundException("Student Exam Not Found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        StudentAnswer foundStudentAnswer =
                studentAnswerRepository
                        .findByStudentExamIdAndQuestionId(studentExamId, questionId)
                        .orElse(new StudentAnswer());



        foundStudentAnswer.setStudentExam(studentExam);
        foundStudentAnswer.setQuestion(question);
        foundStudentAnswer.setAnswer(answer);

        studentAnswerRepository.save(foundStudentAnswer);
    }
}

