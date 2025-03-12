package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.exception.ExamExpiredException;
import ir.maktabsharif.OnlineExamManagementProject.exception.ResourceNotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.AnswerDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.OptionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.QuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.*;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.DescriptiveQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.repository.*;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExamSessionService {

    private StudentExamAnswerRepository studentExamAnswerRepository;
    private ExamRepository examRepository;
    private StudentExamRepository studentExamRepository;
    private StudentRepository studentRepository;
    private QuestionRepository questionRepository;
    private QuestionService questionService;
    private final Map<Long, Integer> examTimers = new ConcurrentHashMap<>();


    public ExamSessionService(QuestionService questionService, StudentExamAnswerRepository studentExamAnswerRepository, QuestionRepository questionRepository, StudentRepository studentRepository, ExamRepository examRepository, StudentExamRepository studentExamRepository) {
        this.studentExamRepository = studentExamRepository;
        this.studentExamAnswerRepository = studentExamAnswerRepository;
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
    }

    public void startExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("exam not found!"));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found!"));

        StudentExam studentExam =
                studentExamRepository.findByExamIdAndStudentId(examId, studentId)
                        .orElseThrow(() -> new ResourceNotFoundException("student exam not found!"));


        if (studentExam.getStartTime() == null) {
            studentExam.setStartTime(LocalDateTime.now());
            studentExamRepository.save(studentExam);
        } else {
            throw new RuntimeException("Exam already started!");
        }

        timer(examId, exam);
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

    public QuestionDto.ResponseDto getCurrentQuestion(Long studentId, Long examId, int currentQuestionIndex) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("exam not found!"));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found!"));

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

        if (question instanceof DescriptiveQuestion){
            return new QuestionDto.DescriptiveQuestionResponse(question.getId(), question.getTitle(), question.getContent(), question.getTeacher().getId());
        }

        return Util.convertToQuestionResponse(examQuestions.get(currentQuestionIndex).getQuestion());
    }

    public ExamQuestion previousQuestion(Long examId, Long studentId) {
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
            studentExamRepository.save(studentExam);  // ذخیره مقدار جدید
        } else if (studentExam.getCurrentQuestionIndex() == 0) {
            throw new RuntimeException("You are at the first question.");
        }

        return examQuestions.get(studentExam.getCurrentQuestionIndex());
    }


    public ExamQuestion nextQuestion(Long examId, Long studentId) {
        StudentExam studentExam = studentExamRepository.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new RuntimeException("Exam session not found!"));

        // بررسی اینکه آزمون شروع شده است
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
            studentExamRepository.save(studentExam);  // ذخیره مقدار جدید
        } else {
            throw new RuntimeException("You are at the last question.");
        }

        return examQuestions.get(studentExam.getCurrentQuestionIndex());
    }

    public void finishExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("exam not found!"));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found!"));

        StudentExam studentExam = studentExamRepository.findByExamId(examId).orElseThrow(() -> new ResourceNotFoundException("student exam not found!"));


        if (!studentExam.isExamTimeExpired()) {
            studentExam.setEndTime(LocalDateTime.now());
        }
        studentExam.setCompleted(true);
        studentExamRepository.save(studentExam);
    }


    @Scheduled(fixedRate = 30000) // هر ۳۰ ثانیه
    public void autoSaveStudentAnswers() {
        List<StudentExam> ongoingExams = studentExamRepository.findByCompletedFalse();

        for (StudentExam studentExam : ongoingExams) {
            Long studentId = studentExam.getStudent().getId();
            Long examId = studentExam.getExam().getId();

            // یافتن پاسخ‌های ذخیره‌نشده
            List<StudentExamAnswer> answers = studentExamAnswerRepository.findByStudentIdAndExamId(studentId, examId);

            if (!answers.isEmpty()) {
                studentExamAnswerRepository.saveAll(answers);
                System.out.println("Auto-saved answers for student " + studentId + " in exam " + examId);
            }
        }
    }


//    public boolean saveStudentAnswer(AnswerDto.SaveRequest answerDto) {
//        // جستجو برای یافتن پاسخ قبلی در آزمون
//        Optional<StudentExamAnswer> existingAnswer = studentExamAnswerRepository.findByStudentExamIdAndQuestionId(
//                answerDto.studentExamId(), answerDto.questionId()
//        );
//
//        StudentExamAnswer studentAnswer = existingAnswer.orElse(new StudentExamAnswer());
//
//        // تنظیم اطلاعات پاسخ
//        StudentExam studentExam = studentExamRepository.findById(answerDto.studentExamId())
//                .orElseThrow(() -> new RuntimeException("StudentExam not found"));
//
//        Question question = questionRepository.findById(answerDto.questionId())
//                .orElseThrow(() -> new RuntimeException("Question not found"));
//
//        studentAnswer.setStudentExam(studentExam);
//        studentAnswer.setStudent(studentExam.getStudent());
//        studentAnswer.setExam(studentExam.getExam());
//        studentAnswer.setQuestion(question);
//
//        // بررسی نوع سوال و ذخیره پاسخ مناسب
//        if (question instanceof DescriptiveQuestion) {
//            studentAnswer.setDescriptiveAnswer(answerDto.descriptiveAnswer());
//        } else if (question instanceof MultipleChoiceQuestion) {
//            studentAnswer.setSelectedChoices(answerDto.selectedChoiceIds());
//        }
//
//        studentAnswer.setFinalized(false); // هنوز نهایی نشده است
//
//        // ذخیره پاسخ در دیتابیس
//        studentExamAnswerRepository.save(studentAnswer);
//        return true;
//    }


//    public void saveAnswer(Long studentId, Long examId, Long questionId, Object answer) {
//        Question question = questionRepository.findById(questionId).orElseThrow();
//        StudentExamAnswer studentExamAnswer = studentExamAnswerRepository
//                .findByStudentIdAndExamIdAndQuestionId(studentId, examId, questionId)
//                .orElse(new StudentExamAnswer());
//
//        studentExamAnswer.setStudent(studentRepository.findById(studentId).orElseThrow());
//        studentExamAnswer.setExam(examRepository.findById(examId).orElseThrow());
//        studentExamAnswer.setQuestion(question);
//
//        // بررسی نوع سوال برای ذخیره پاسخ مناسب
//        if (question instanceof DescriptiveQuestion) {
//            studentExamAnswer.setDescriptiveAnswer((String) answer);
//        } else if (question instanceof MultipleChoiceQuestion) {
//            studentExamAnswer.setSelectedChoices((List<String>) answer);
//        }
//
//        studentExamAnswerRepository.save(studentExamAnswer);
//    }

    public List<AnswerDto.AnswerResponse> getStudentAnswers(Long studentId, Long examId) {
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new ResourceNotFoundException("exam not found!"));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found!"));

        StudentExam studentExam = studentExamRepository.findByExamIdAndStudentId(examId, studentId).orElseThrow(() -> new ResourceNotFoundException("student exam not found!"));

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
                for (String item : studentExamAnswer.getSelectedChoices()){
                    for (Options option : options) {
                        if (option.getText().equals(item)){
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

