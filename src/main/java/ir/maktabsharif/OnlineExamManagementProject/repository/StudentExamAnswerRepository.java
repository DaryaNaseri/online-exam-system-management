package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamAnswerRepository extends JpaRepository<StudentExamAnswer, Long> {
    Optional<StudentExamAnswer> findByStudentIdAndExamIdAndQuestionId(Long studentId, Long examId, Long questionId);

    List<StudentExamAnswer> findByStudentIdAndExamId(Long studentId, Long examId);

    Optional<StudentExamAnswer> findByStudentExamIdAndQuestionId(Long aLong, Long aLong1);

    List<StudentExamAnswer> findByStudentExamId(Long studentExamId);

    @Query("SELECT SUM(a.assignedScore) FROM StudentExamAnswer a " +
            "WHERE a.studentExam.exam.id = :examId " +
            "AND a.studentExam.student.id = :studentId " +
            "AND a.isFinalized = true")
    Double getTotalScoreForCompletedExam(@Param("examId") Long examId, @Param("studentId") Long studentId);

}
