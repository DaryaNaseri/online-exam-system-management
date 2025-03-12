package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamAnswerRepository extends JpaRepository<StudentExamAnswer, Integer> {
    Optional<StudentExamAnswer> findByStudentIdAndExamIdAndQuestionId(Long studentId, Long examId, Long questionId);

    List<StudentExamAnswer> findByStudentIdAndExamId(Long studentId, Long examId);

    Optional<StudentExamAnswer> findByStudentExamIdAndQuestionId(Long aLong, Long aLong1);
}
