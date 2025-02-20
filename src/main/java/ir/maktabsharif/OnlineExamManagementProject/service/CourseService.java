package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.Course;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.base.BaseService;

import java.util.List;
import java.util.Optional;

public interface CourseService extends BaseService<Course> {

    void create(CourseDto.CreateCourseRequestDto courseDto);

    List<CourseDto.Response> findAll();

    Optional<CourseDto.Response> findById(Long id);

    CourseDto.CourseStudentsListDto addStudentToCourse(CourseDto.Response course, UserDto.Response studentResponse);

    CourseDto.CourseTeacherDto assignTeacherToCourse(CourseDto.Response course, UserDto.Response teacherResponse);

    void deleteStudentFromCourse(CourseDto.Response course, UserDto.Response studentResponse);

    void unassignTeacherFromCourse(CourseDto.Response course, UserDto.Response teacherResponse);

    List<UserDto.Response> findAllStudents(Long courseId);

    Optional<UserDto.Response> findTeacher(Long courseId);






}
