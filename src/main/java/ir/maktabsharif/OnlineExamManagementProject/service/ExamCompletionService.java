package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExam;
import ir.maktabsharif.OnlineExamManagementProject.repository.StudentExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamCompletionService {
    @Autowired
    private StudentExamRepository studentExamRepository;

    public void completeExam(Long studentExamId) {
        StudentExam studentExam = studentExamRepository.findById(studentExamId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        studentExam.setCompleted(true);
        studentExamRepository.save(studentExam);
    }
}

