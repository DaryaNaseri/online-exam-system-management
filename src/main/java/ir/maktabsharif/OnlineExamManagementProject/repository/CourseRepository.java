package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.Course;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Student;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByTitle(String courseName);

    @Query("SELECT c.teacher FROM Course c WHERE c.courseCode = :courseCode")
    Optional<Teacher> findTeacherByCourseCode(@Param("courseCode") String courseCode);


    @Query("SELECT c.students FROM Course c JOIN c.students s WHERE c = :course")
    List<Student> findStudentsByCourse(@Param("course") Course course);

    List<Course> findByTeacher(Teacher teacher);


    Optional<Course> findCourseByCourseCode(String courseCode);
}
