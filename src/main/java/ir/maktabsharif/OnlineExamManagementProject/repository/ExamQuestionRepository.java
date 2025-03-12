package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.Exam;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.ExamQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    Optional<ExamQuestion> findByExamIdAndQuestionId(Long examId, Long questionId);

    List<ExamQuestion> findByExamId(Long examId);

    boolean existsByExamAndQuestion(Exam exam, Question question);

}

