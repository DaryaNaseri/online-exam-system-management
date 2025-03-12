package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT DISTINCT q FROM Question q " +
            "JOIN ExamQuestion eq ON eq.question.id = q.id " +
            "JOIN eq.exam e " +
            "WHERE e.course.id = :courseId " +
            "AND e.teacher.id = :teacherId")
    List<Question> findQuestionsByCourseAndTeacher(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId);

    Optional<Page<Question>> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Question> findByTitleContainingIgnoreCase(String title);
}
