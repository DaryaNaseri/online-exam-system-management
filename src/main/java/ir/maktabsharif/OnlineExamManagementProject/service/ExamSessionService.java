package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.*;

import java.util.List;


public interface ExamSessionService {

    void startExam(Long examId, Long studentId);

    String getRemainingTime(Long examId);

    void autoFinishExams();

    QuestionDto.ResponseDto getFirstQuestion(Long studentId, Long examId, int currentQuestionIndex);

    QuestionDto.ResponseDto previousQuestion(Long examId, Long studentId);

    QuestionDto.ResponseDto nextQuestion(Long examId, Long studentId);

    StudentExamAnswerDto.Response finishExam(Long examId, Long studentId);

    List<StudentExamDto.Response> getCompletedExamsByTeacher(Long teacherId);

    StudentExamAnswerDto.Response updateDescriptiveScore(StudentExamAnswerDto.Request request);

    List<AnswerDto.AnswerResponse> getStudentAnswers(Long studentId, Long examId);
}

