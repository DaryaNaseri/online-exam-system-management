package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    Optional<StudentAnswer> findByStudentExamIdAndQuestionId(Long studentExamId, Long questionId);
}
