package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.ExamExpiredException;
import ir.maktabsharif.OnlineExamManagementProject.exception.ResourceNotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.*;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.*;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.DescriptiveQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.repository.*;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamSessionService;
import ir.maktabsharif.OnlineExamManagementProject.service.QuestionService;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExamSessionServiceImpl implements ExamSessionService {

    private final StudentExamAnswerRepository studentExamAnswerRepository;
    private final ExamRepository examRepository;
    private final StudentExamRepository studentExamRepository;
    private final StudentRepository studentRepository;
    private final Map<Long, Integer> examTimers = new ConcurrentHashMap<>();
    private final ExamQuestionRepository examQuestionRepository;


    public ExamSessionServiceImpl(ExamQuestionRepository examQuestionRepository, StudentExamAnswerRepository studentExamAnswerRepository, StudentRepository studentRepository, ExamRepository examRepository, StudentExamRepository studentExamRepository) {
        this.studentExamRepository = studentExamRepository;
        this.studentExamAnswerRepository = studentExamAnswerRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
    }


    public void startExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found!"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found!"));

        StudentExam studentExam = studentExamRepository.findByExamIdAndStudentId(examId, studentId)
                .orElse(new StudentExam(student, exam, false));

        if (studentExam.isCompleted()) {
            throw new IllegalStateException("You have already taken this exam and cannot retake it.");
        }

        if (studentExam.getStartTime() == null) {
            studentExam.setStartTime(LocalDateTime.now());
            studentExam.setCompleted(false);
            studentExamRepository.save(studentExam);

            createInitialAnswersForStudent(studentExam);


        } else {
            throw new RuntimeException("Exam already started!");
        }

        timer(examId, exam);
    }

    private void createInitialAnswersForStudent(StudentExam studentExam) {
        List<ExamQuestion> examQuestions = studentExam.getExam().getExamQuestions();
        List<StudentExamAnswer> studentExamAnswers = new ArrayList<>();

        for (ExamQuestion examQuestion : examQuestions) {
            StudentExamAnswer studentExamAnswer = new StudentExamAnswer();
            studentExamAnswer.setStudentExam(studentExam);
            studentExamAnswer.setStudent(studentExam.getStudent());
            studentExamAnswer.setExam(studentExam.getExam());
            studentExamAnswer.setQuestion(examQuestion.getQuestion());
            studentExamAnswer.setAssignedScore(0.0);
            studentExamAnswer.setFinalized(false);

            studentExamAnswers.add(studentExamAnswer);
        }

        studentExamAnswerRepository.saveAll(studentExamAnswers);
    }


    private void timer(Long examId, Exam exam) {
        examTimers.put(examId, exam.getDuration() * 60);
        new Thread(() -> {
            try {
                while (examTimers.getOrDefault(examId, 0) > 0) {
                    Thread.sleep(1000);
                    examTimers.computeIfPresent(examId, (id, time) -> time > 0 ? time - 1 : 0);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                examTimers.remove(examId);
            }
        }).start();
    }


    public String getRemainingTime(Long examId) {
        int remainingSeconds = examTimers.getOrDefault(examId, 0);

        if (remainingSeconds <= 0) {

            StudentExam studentExam = studentExamRepository.findByExamId(examId).orElseThrow(() -> new ResourceNotFoundException("studentExam not found!"));

            studentExam.setCompleted(true);
            studentExamRepository.save(studentExam);
            return "time is up";
        }

        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


    @Scheduled(fixedRate = 1000)
    public void autoFinishExams() {
        List<StudentExam> activeExams = studentExamRepository.findAllByCompletedFalse();

        for (StudentExam exam : activeExams) {
            if (!exam.isExamTimeExpired()) {
                exam.setCompleted(true);
                studentExamRepository.save(exam);
            }
        }
    }


    public QuestionDto.ResponseDto getFirstQuestion(Long studentId, Long examId, int currentQuestionIndex) {
        StudentExam studentExam = studentExamRepository.findByExamId(examId).orElseThrow(() -> new ResourceNotFoundException("student exam not found!"));

        if (studentExam.isExamTimeExpired()) {
            finishExam(examId, studentId);
            throw new ExamExpiredException("Time is up!");
        }

        List<ExamQuestion> examQuestions = studentExam.getExam().getExamQuestions();
        if (currentQuestionIndex >= examQuestions.size()) {
            throw new RuntimeException("No more questions available.");
        }

        Question question = examQuestions.get(currentQuestionIndex).getQuestion();

        return Util.mapToQuestionDto(question);
    }

    public QuestionDto.ResponseDto previousQuestion(Long examId, Long studentId) {
        StudentExam studentExam = studentExamRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new RuntimeException("Exam session not found!"));

        if (studentExam.getStartTime() == null) {
            throw new RuntimeException("You must start the exam before navigating questions.");
        }

        if (studentExam.isExamTimeExpired()) {
            finishExam(examId, studentId);
            throw new ExamExpiredException("Time is up! You cannot continue.");
        }

        List<ExamQuestion> examQuestions = studentExam.getExam().getExamQuestions();

        if (studentExam.getCurrentQuestionIndex() > 0) {
            studentExam.setCurrentQuestionIndex(studentExam.getCurrentQuestionIndex() - 1);
            studentExamRepository.save(studentExam);
        } else if (studentExam.getCurrentQuestionIndex() == 0) {
            throw new RuntimeException("You are at the first question.");
        }

        ExamQuestion examQuestion = examQuestions.get(studentExam.getCurrentQuestionIndex());
        return Util.mapToQuestionDto(examQuestion.getQuestion());
    }


    public QuestionDto.ResponseDto nextQuestion(Long examId, Long studentId) {
        StudentExam studentExam = studentExamRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new RuntimeException("Exam session not found!"));

        if (studentExam.getStartTime() == null) {
            throw new RuntimeException("You must start the exam before navigating questions.");
        }

        if (studentExam.isExamTimeExpired()) {
            finishExam(examId, studentId);
            throw new ExamExpiredException("Time is up! You cannot continue.");
        }

        List<ExamQuestion> examQuestions = studentExam.getExam().getExamQuestions();

        if (studentExam.getCurrentQuestionIndex() < examQuestions.size() - 1) {
            studentExam.setCurrentQuestionIndex(studentExam.getCurrentQuestionIndex() + 1);
            studentExamRepository.save(studentExam);
        } else {
            throw new RuntimeException("You are at the last question.");
        }
        ExamQuestion examQuestion = examQuestions.get(studentExam.getCurrentQuestionIndex());
        return Util.mapToQuestionDto(examQuestion.getQuestion());
    }


    public StudentExamAnswerDto.Response finishExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("exam not found!"));

        examRepository.findCourseByExam(exam)
                .orElseThrow(() -> new ResourceNotFoundException("course not found!"));

        Teacher teacher = examRepository.findTeacherByExam(exam)
                .orElseThrow(() -> new ResourceNotFoundException("teacher not found!"));


        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found!"));

        StudentExam studentExam = studentExamRepository.findByExamId(examId).orElseThrow(() -> new ResourceNotFoundException("student exam not found!"));


        if (!studentExam.isExamTimeExpired()) {
            studentExam.setEndTime(LocalDateTime.now());
        }
        studentExam.setCompleted(true);

        notAnsweredQuestionsScore(studentExam);

        studentExamRepository.save(studentExam);

        return new StudentExamAnswerDto.Response(
                student.getUsername(),
                teacher.getUsername(),
                exam.getTitle(),
                studentExam.getStartTime(),
                studentExam.isGraded()
        );
    }

    private void notAnsweredQuestionsScore(StudentExam studentExam) {
        for (StudentExamAnswer answer : studentExam.getStudentExamAnswer()) {
            if (!answer.getFinalized()) {
                answer.setAssignedScore(0.0);
                answer.setFinalized(true);
            }
        }
    }


    public List<StudentExamDto.Response> getCompletedExamsByTeacher(Long teacherId) {

        List<StudentExam> byExamTeacherIdAndCompletedTrue = studentExamRepository.findByExamTeacherIdAndCompletedTrue(teacherId);

        List<StudentExamDto.Response> responses = new ArrayList<>();
        Integer count = 0;
        for (StudentExam studentExam : byExamTeacherIdAndCompletedTrue) {
            count++;
            StudentExamDto.Response response = new StudentExamDto.Response(
                    count + ". ",
                    studentExam.getStudent().getUsername(),
                    studentExam.getTotalScore()
            );
            responses.add(response);
        }

        return responses;
    }


    public StudentExamAnswerDto.Response updateDescriptiveScore(StudentExamAnswerDto.Request request) {
        StudentExamAnswer answer = studentExamAnswerRepository
                .findByStudentIdAndExamIdAndQuestionId(request.studentId(), request.examId(), request.questionId())
                .orElseThrow(() -> new ResourceNotFoundException("student exam answer not found!"));

        ExamQuestion examQuestion = examQuestionRepository.findByExamIdAndQuestionId(request.examId(), request.questionId())
                .orElseThrow(() -> new RuntimeException("Exam question mapping not found"));


        if (request.score() > examQuestion.getScore()) {
            throw new IllegalArgumentException("Score exceeds max allowed value");
        }

        answer.setAssignedScore(request.score());
        answer.setFinalized(true);
        answer.getStudentExam().setGraded(true);


        studentExamAnswerRepository.save(answer);
        updateFinalScore(answer.getStudentExam());

        return new StudentExamAnswerDto.Response(
                answer.getStudent().getUsername(),
                examQuestion.getExam().getTeacher().getUsername(),
                answer.getExam().getTitle(),
                answer.getStudentExam().getStartTime(),
                answer.getStudentExam().isGraded()
        );
    }


    private void updateFinalScore(StudentExam studentExam) {
        double totalScore = studentExam.getStudentExamAnswer().stream()
                .filter(StudentExamAnswer::getFinalized)
                .mapToDouble(
                        a -> a.getAssignedScore() != null ?
                                a.getAssignedScore() : 0)
                .sum();
        studentExam.setTotalScore(totalScore);
        studentExamRepository.save(studentExam);
    }

    public List<AnswerDto.AnswerResponse> getStudentAnswers(Long studentId, Long examId) {
        examRepository.findById(examId).orElseThrow(() -> new ResourceNotFoundException("exam not found!"));

        studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found!"));

        studentExamRepository.findByExamIdAndStudentId(examId, studentId).orElseThrow(() -> new ResourceNotFoundException("student exam not found!"));

        List<StudentExamAnswer> byStudentIdAndExamId = studentExamAnswerRepository.findByStudentIdAndExamId(studentId, examId);

        List<AnswerDto.AnswerResponse> answers = new ArrayList<>();
        for (StudentExamAnswer studentExamAnswer : byStudentIdAndExamId) {
            if (studentExamAnswer.getQuestion() instanceof DescriptiveQuestion) {
                answers.add(new AnswerDto.AnswerResponse(
                        studentId,
                        examId,
                        studentExamAnswer.getQuestion().getId(),
                        studentExamAnswer.getDescriptiveAnswer()
                ));
            }
            if (studentExamAnswer.getQuestion() instanceof MultipleChoiceQuestion) {
                List<OptionDto.Response> answerOptions = new ArrayList<>();
                List<Options> options = ((MultipleChoiceQuestion) studentExamAnswer.getQuestion()).getOptions();
                for (String item : studentExamAnswer.getSelectedChoices()) {
                    for (Options option : options) {
                        if (option.getText().equals(item)) {
                            answerOptions.add(new OptionDto.Response(option.getId(), option.getText()));
                        }
                    }
                }

                answers.add(new AnswerDto.AnswerResponse(
                        studentId,
                        examId,
                        studentExamAnswer.getQuestion().getId(),
                        answerOptions
                ));
            }
        }

        return answers;
    }
}

