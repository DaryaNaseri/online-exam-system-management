package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.Course;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Exam;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findExamByCourse(Course course);

    Boolean existsByTitleAndCourse(String title, Course course);

    @Query("SELECT e.course FROM Exam e WHERE e = :exam")
    Optional<Course> findCourseByExam(@Param("exam")Exam exam);

    @Query("SELECT e.teacher FROM Exam e WHERE e = :exam")
    Optional<Teacher> findTeacherByExam(@Param("exam")Exam exam);
}
