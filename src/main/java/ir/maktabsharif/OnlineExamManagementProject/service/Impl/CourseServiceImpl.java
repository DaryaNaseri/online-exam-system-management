package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.AccountNotVerifiedException;
import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.TitleOrCourseCodeMustBeUniqueException;
import ir.maktabsharif.OnlineExamManagementProject.model.*;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Course;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Student;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;
import ir.maktabsharif.OnlineExamManagementProject.repository.CourseRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.UserRepository;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
import ir.maktabsharif.OnlineExamManagementProject.service.UserService;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class CourseServiceImpl implements CourseService {

    private final UserRepository userRepository;
    private final UserService userService;
    private CourseRepository courseRepository;


    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, UserServiceImpl userService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public Course save(Course entity) {
        return courseRepository.save(entity);
    }


    @Override
    public void create(CourseDto.CreateCourseRequestDto courseDto) {
        checkIfCourseExists(courseDto.title(), courseDto.courseCode());

        courseRepository.save(Course
                .builder()
                .title(courseDto.title())
                .courseCode(courseDto.courseCode())
                .startDate(courseDto.startDate())
                .endDate(courseDto.endDate())
                .build());
    }


    private void checkIfCourseExists(String title, String courseCode) {
        if (courseRepository.findCourseByTitle(title).isPresent()) {
            throw new TitleOrCourseCodeMustBeUniqueException("This title has already been taken.");
        }
        if (courseRepository.findCourseByCourseCode(courseCode).isPresent()) {
            throw new TitleOrCourseCodeMustBeUniqueException("This course code has already been taken.");
        }
    }


    @Override
    public List<CourseDto.Response> findAll() {
        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            throw new NotFoundException("There are no courses in the database");
        }
        return courses.stream()
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());
    }


    private CourseDto.Response convertToCourseResponse(Course course) {
        return new CourseDto.Response(
                course.getId(),
                course.getTitle(),
                course.getCourseCode(),
                course.getStartDate(),
                course.getEndDate()
        );
    }


    @Override
    public Optional<CourseDto.Response> findById(Long id) {
        return courseRepository.findById(id)
                .map(this::convertToCourseResponse);
    }


    @Override
    public void deleteById(Long id) {
        Optional<Course> byId = courseRepository.findById(id);
        if (byId.isPresent()) {
            courseRepository.delete(byId.get());
        }else
        throw new NotFoundException("Course not found");
    }


    @Override
    public CourseDto.CourseStudentsListDto addStudentToCourse(CourseDto.Response course, UserDto.Response studentResponse) {
        User user = userRepository.findById(studentResponse.id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Course foundCourse = courseRepository.findById(course.id())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        if (user.getStatus().equals(RegistrationStatus.PENDING)) {
            throw new AccountNotVerifiedException("Account must be approved first!");
        }

        if (!(user instanceof Student student)) {
            throw new IllegalArgumentException("User is not a student");
        }

        if (foundCourse.getStudents().contains(user)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        foundCourse.getStudents().add(student);
        courseRepository.save(foundCourse);
        return new CourseDto.CourseStudentsListDto(
                foundCourse.getStudents(),
                foundCourse.getId(),
                foundCourse.getCourseCode()
        );
    }


    @Override
    public CourseDto.CourseTeacherDto assignTeacherToCourse(CourseDto.Response course, UserDto.Response teacherResponse) {
        User user = userRepository.findById(teacherResponse.id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Course foundCourse = courseRepository.findById(course.id())
                .orElseThrow(() -> new NotFoundException("Course not found"));


        if (user.getStatus().equals(RegistrationStatus.PENDING)) {
            throw new AccountNotVerifiedException("Account must be approved first!");
        }

        if (!(user instanceof Teacher teacher)) {
            throw new IllegalArgumentException("User is not a teacher");
        }

        if (foundCourse.getTeacher() != null && !foundCourse.getTeacher().equals(teacher)) {
            throw new IllegalArgumentException("Teacher is already assign in this course");

        }

        foundCourse.setTeacher(teacher);
        courseRepository.save(foundCourse);
        return new CourseDto.CourseTeacherDto(
                foundCourse.getTeacher(),
                foundCourse.getId(),
                foundCourse.getCourseCode()
        );
    }


    @Override
    public void deleteStudentFromCourse(CourseDto.Response course, UserDto.Response studentResponse) {
        User user = userRepository.findById(studentResponse.id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Course foundCourse = courseRepository.findById(course.id())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        if (!(user instanceof Student student)) {
            throw new IllegalArgumentException("User is not a student");
        }

        if (!(foundCourse.getStudents().contains(student))) {
            throw new IllegalArgumentException("Student isn't already exist in this course");
        }

        foundCourse.getStudents().remove(student);
        courseRepository.save(foundCourse);

    }


    @Override
    public void unassignTeacherFromCourse(CourseDto.Response course, UserDto.Response teacherResponse) {
        User user = userRepository.findById(teacherResponse.id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Course foundCourse = courseRepository.findById(course.id())
                .orElseThrow(() -> new NotFoundException("Course not found"));


        if (!(user instanceof Teacher teacher)) {
            throw new IllegalArgumentException("User is not a teacher");
        }

        if (!(foundCourse.getTeacher().equals(teacher))) {
            throw new IllegalArgumentException("This teacher is not assign for this course");
        }

        foundCourse.setTeacher(null);
        courseRepository.save(foundCourse);

    }


    @Override
    public List<UserDto.Response> findAllStudents(Long courseId) {
        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        return courseRepository.findStudents(foundCourse)
                .stream()
                .map(Util::convertToUserResponse)
                .toList();

    }


    @Override
    public Optional<UserDto.Response> findTeacher(Long courseId) {
        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        Optional<Teacher> teacher = courseRepository.findTeacher(foundCourse.getCourseCode());
        return teacher.map(Util::convertToUserResponse);
    }


}