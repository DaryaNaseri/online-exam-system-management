package ir.maktabsharif.OnlineExamManagementProject.service.scheduler;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExam;
import ir.maktabsharif.OnlineExamManagementProject.repository.StudentExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExamAutoFinisher {
    @Autowired
    private StudentExamRepository studentExamRepository;

    @Scheduled(fixedRate = 10000)
    public void finishExpiredExams() {
        List<StudentExam> ongoingExams = studentExamRepository.findByCompletedFalse();

        for (StudentExam studentExam : ongoingExams) {
            if (studentExam.isExamTimeExpired()) {
                studentExam.setCompleted(true);
                studentExam.setEndTime(LocalDateTime.now());
                studentExamRepository.save(studentExam);
            }
        }
    }
}

