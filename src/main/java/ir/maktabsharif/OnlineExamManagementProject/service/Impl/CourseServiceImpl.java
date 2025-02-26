package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidInputException;
import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.CourseCodeMustBeUniqueException;
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
    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public Course save(Course entity) {
        return courseRepository.save(entity);
    }


    @Override
    public CourseDto.Response create(CourseDto.CreateCourseRequestDto courseDto) {
        checkIfCourseExists(courseDto.courseCode());

        Course save = courseRepository.save(Course
                .builder()
                .title(courseDto.title())
                .courseCode(courseDto.courseCode())
                .startDate(courseDto.startDate())
                .endDate(courseDto.endDate())
                .build());
        return convertToCourseResponse(save);
    }


    private void checkIfCourseExists(String courseCode) {
        if (courseRepository.findCourseByCourseCode(courseCode).isPresent()) {
            throw new CourseCodeMustBeUniqueException("This course code has already been taken.");
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
        } else
            throw new NotFoundException("Course not found");
    }


    @Override
    public CourseDto.CourseStudentsListDto addStudentToCourse(Long courseId, Long studentId) {
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        Util.validateUserStatus(user);


        if (!(user instanceof Student student)) {
            throw new InvalidInputException("User is not a student");
        }

        if (foundCourse.getStudents().contains(user)) {
            throw new InvalidInputException("Student is already enrolled in this course");
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
    public CourseDto.CourseTeacherDto assignTeacherToCourse(Long courseId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));


        Util.validateUserStatus(user);

        if (!(user instanceof Teacher teacher)) {
            throw new InvalidInputException("User is not a teacher");
        }

        if (foundCourse.getTeacher() != null && !foundCourse.getTeacher().equals(teacher)) {
            throw new InvalidInputException("Teacher is already assign in this course");

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
    public void deleteStudentFromCourse(Long courseId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        if (!(user instanceof Student student)) {
            throw new InvalidInputException("User is not a student");
        }

        if (!(foundCourse.getStudents().contains(student))) {
            throw new InvalidInputException("Student isn't already exist in this course");
        }

        foundCourse.getStudents().remove(student);
        courseRepository.save(foundCourse);

    }


    @Override
    public void unassignTeacherFromCourse(Long courseId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));


        if (!(user instanceof Teacher teacher)) {
            throw new InvalidInputException("User is not a teacher");
        }

        if (!(foundCourse.getTeacher().equals(teacher))) {
            throw new InvalidInputException("This teacher is not assign for this course");
        }

        foundCourse.setTeacher(null);
        courseRepository.save(foundCourse);

    }


    @Override
    public List<UserDto.Response> findAllStudents(Long courseId) {
        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        return courseRepository.findStudentsByCourse(foundCourse)
                .stream()
                .map(Util::convertToUserResponse)
                .toList();

    }


    @Override
    public UserDto.Response findTeacher(Long courseId) {
        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        Teacher teacher = courseRepository.findTeacherByCourseCode(
                        foundCourse.getCourseCode())
                .orElseThrow(() -> new NotFoundException("No teacher has been assigned to this course yet."));
        return Util.convertToUserResponse(teacher);
    }


    public List<CourseDto.Response> findCoursesByTeacherId(Long teacherId) {
        User user = userRepository.findById(teacherId).orElseThrow(() -> new NotFoundException("User not found"));
        if (!(user instanceof Teacher teacher)) {
            throw new IllegalArgumentException("User is not a teacher");
        }
        return courseRepository.findByTeacher(teacher).stream()
                .map(this::convertToCourseResponse)
                .toList();
    }
}