package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.TitleOrCourseCodeMustBeUniqueException;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
import ir.maktabsharif.OnlineExamManagementProject.service.Impl.CourseServiceImpl;
import ir.maktabsharif.OnlineExamManagementProject.service.Impl.UserServiceImpl;
import ir.maktabsharif.OnlineExamManagementProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseServiceImpl courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCourse(@Valid @RequestBody CourseDto.CreateCourseRequestDto course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
        }

        try {
            courseService.create(course);
            return ResponseEntity.ok("Course created successfully.");
        } catch (TitleOrCourseCodeMustBeUniqueException e) {
            return ResponseEntity.badRequest().body("title or course code must be unique.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDto.Response>> allCourses() {
        try {
            List<CourseDto.Response> courses = courseService.findAll();
            return ResponseEntity.ok(courses);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }
    }


    @PostMapping("/{courseId}/add-student/{userId}")
    public ResponseEntity<String> addStudentToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        Optional<CourseDto.Response> courseOpt = courseService.findById(courseId);
        Optional<UserDto.Response> studentOpt = userService.findById(userId);

        if (courseOpt.isEmpty() || studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Course or Student not found");
        }

        UserDto.Response student = studentOpt.get();
        if (student.role() != UserRole.STUDENT) {
            return ResponseEntity.badRequest().body("This user is not a student");
        }

        courseService.addStudentToCourse(courseOpt.get(), student);
        return ResponseEntity.ok("Student added to course");
    }


    @PostMapping("/{courseId}/assign-teacher/{userId}")
    public ResponseEntity<String> assignTeacherToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        Optional<CourseDto.Response> courseOpt = courseService.findById(courseId);
        Optional<UserDto.Response> teacherOpt = userService.findById(userId);

        if (courseOpt.isEmpty() || teacherOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Course or Teacher not found");
        }

        UserDto.Response teacher = teacherOpt.get();
        if (teacher.role() != UserRole.TEACHER) {
            return ResponseEntity.badRequest().body("This user is not a teacher");
        }

        courseService.assignTeacherToCourse(courseOpt.get(), teacher);
        return ResponseEntity.ok("Teacher assigned to course");
    }


    @PostMapping("/{courseId}/delete-student/{userId}")
    public ResponseEntity<String> deleteStudentFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        Optional<CourseDto.Response> courseOpt = courseService.findById(courseId);
        Optional<UserDto.Response> studentOpt = userService.findById(userId);

        if (courseOpt.isEmpty() || studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Course or Student not found");
        }

        UserDto.Response student = studentOpt.get();
        if (student.role() != UserRole.STUDENT) {
            return ResponseEntity.badRequest().body("This user is not a student");
        }

        courseService.deleteStudentFromCourse(courseOpt.get(), student);
        return ResponseEntity.ok("Student deleted from course");
    }

    @PostMapping("/{courseId}/unassign-teacher/{userId}")
    public ResponseEntity<String> unassignTeacherFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        Optional<CourseDto.Response> courseOpt = courseService.findById(courseId);
        Optional<UserDto.Response> teacherOpt = userService.findById(userId);

        if (courseOpt.isEmpty() || teacherOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Course or Teacher not found");
        }

        UserDto.Response teacher = teacherOpt.get();
        if (teacher.role() != UserRole.TEACHER) {
            return ResponseEntity.badRequest().body("This user is not a teacher");
        }

        courseService.unassignTeacherFromCourse(courseOpt.get(), teacher);
        return ResponseEntity.ok("Teacher unassigned from course");
    }


    @GetMapping("/{courseId}/allStudents")
    public ResponseEntity<List<UserDto.Response>> allStudentsCourse(@PathVariable Long courseId) {
        try {
            List<UserDto.Response> students = courseService.findAllStudents(courseId);
            return ResponseEntity.ok(students);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }


    @GetMapping("/{courseId}/teacher")
    public ResponseEntity<UserDto.Response> teachersCourse(@PathVariable Long courseId) {
        Optional<CourseDto.Response> responseOpt = courseService.findById(courseId);
        if (responseOpt.isPresent()) {
            Optional<UserDto.Response> teacher = courseService.findTeacher(courseId);
            if (teacher.isPresent()) {
                return ResponseEntity.ok(teacher.get());
            }
        }
        return ResponseEntity.badRequest().body(null);
    }


    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteById(courseId);
        return ResponseEntity.ok("Course deleted");
    }
}
