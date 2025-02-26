package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.Course;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.base.BaseService;

import java.util.List;
import java.util.Optional;

public interface CourseService extends BaseService<Course> {

    CourseDto.Response create(CourseDto.CreateCourseRequestDto courseDto);

    List<CourseDto.Response> findAll();

    Optional<CourseDto.Response> findById(Long id);

    CourseDto.CourseStudentsListDto addStudentToCourse(Long courseId, Long studentId);

    CourseDto.CourseTeacherDto assignTeacherToCourse(Long courseId, Long userId);

    void deleteStudentFromCourse(Long courseId,Long userId);

    void unassignTeacherFromCourse(Long courseId,Long userId);

    List<UserDto.Response> findAllStudents(Long courseId);

    UserDto.Response findTeacher(Long courseId);

    List<CourseDto.Response> findCoursesByTeacherId(Long teacherId);




}
