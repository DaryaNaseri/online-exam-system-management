package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.Course;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Exam;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findExamByCourse(Course course);

    Boolean existsByTitleAndCourse(String title, Course course);

}
