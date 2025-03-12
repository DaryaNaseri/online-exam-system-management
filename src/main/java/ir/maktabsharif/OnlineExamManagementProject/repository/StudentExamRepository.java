package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.Student;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
   @Query("SELECT se.exam.id FROM StudentExam se WHERE se.student.id = :studentId")
   List<Long> findCompletedExamIdsByStudentId(@Param("studentId") Long studentId);

   Optional<StudentExam> findByExamId(Long examId);

   List<StudentExam> findByStudentId(Long studentId);

   List<StudentExam> findByCompletedFalse();

    Optional<StudentExam> findByExamIdAndStudentId(Long examId, Long studentId);
}
