package ir.maktabsharif.OnlineExamManagementProject.model.entity;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;

import java.util.List;

public class ExamSession {
    private Exam exam;
    private Student student;
    private int currentQuestionIndex = 0;
    private boolean isFinished = false;

    public Question getCurrentQuestion() {
        List<ExamQuestion> examQuestions = exam.getExamQuestions();
        if (currentQuestionIndex < examQuestions.size()) {
            return examQuestions.get(currentQuestionIndex).getQuestion();
        }
        return null;
    }

    public void nextQuestion() {
        if (currentQuestionIndex < exam.getExamQuestions().size() - 1) {
            currentQuestionIndex++;
        }
    }
}

