package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.TitleOrCourseCodeMustBeUniqueException;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
import ir.maktabsharif.OnlineExamManagementProject.service.Impl.CourseServiceImpl;
import ir.maktabsharif.OnlineExamManagementProject.service.Impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserServiceImpl userServiceImpl;

    public CourseController(CourseServiceImpl courseService, UserServiceImpl userServiceImpl) {
        this.courseService = courseService;
        this.userServiceImpl = userServiceImpl;
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

        CourseDto.Response course = courseService.findById(courseId).orElse(null);
        UserDto.Response student = userServiceImpl.findById(userId).orElse(null);
        if (student == null || course == null) {
            return ResponseEntity.badRequest().body("Course or Student not found");
        } else if (student.role() != UserRole.STUDENT) {
            return ResponseEntity.badRequest().body("this id is not a student");
        }
        courseService.addStudentToCourse(course, student);
        return ResponseEntity.ok("Student added to course");
    }

    @PostMapping("/{courseId}/assign-teacher/{userId}")
    public ResponseEntity<String> assignTeacherToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        CourseDto.Response courseResponse = courseService.findById(courseId).orElse(null);
        UserDto.Response teacher = userServiceImpl.findById(userId).orElse(null);
        if (courseResponse == null && teacher == null) {
            return ResponseEntity.badRequest().body("Course or Teacher not found");
        } else if (teacher.role() != UserRole.TEACHER) {
            return ResponseEntity.badRequest().body("this id is not a teacher");
        }
        courseService.assignTeacherToCourse(courseResponse, teacher);
        return ResponseEntity.ok("Teacher assigned to course");
    }

    @PostMapping("/{courseId}/delete-student/{userId}")
    public ResponseEntity<String> deleteStudentFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {

        CourseDto.Response course = courseService.findById(courseId).orElse(null);
        UserDto.Response student = userServiceImpl.findById(userId).orElse(null);

        if (student == null || course == null) {
            return ResponseEntity.badRequest().body("Course or Student not found");
        } else if (student.role() != UserRole.STUDENT) {
            return ResponseEntity.badRequest().body("this id is not a student");
        }
        courseService.deleteStudentFromCourse(course, student);
        return ResponseEntity.ok("Student deleted from course");
    }

    @PostMapping("/{courseId}/unassign-teacher/{userId}")
    public ResponseEntity<String> unassignTeacherFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {

        CourseDto.Response course = courseService.findById(courseId).orElse(null);
        UserDto.Response teacher = userServiceImpl.findById(userId).orElse(null);

        if (teacher == null || course == null) {
            return ResponseEntity.badRequest().body("Course or Teacher not found");
        } else if (teacher.role() != UserRole.TEACHER) {
            return ResponseEntity.badRequest().body("this id is not a teacher");
        }
        courseService.unassignTeacherFromCourse(course, teacher);
        return ResponseEntity.ok("Teacher unassigned from course");
    }

    @GetMapping("/{courseId}/allStudents")
    public List<UserDto.Response> allStudentsCourse(@PathVariable Long courseId) {
        CourseDto.Response course = courseService.findById(courseId).orElse(null);
        if (course != null) {
            return courseService.findAllStudents(courseId);
        } else throw new IllegalArgumentException("Course not found");
    }

    @GetMapping("/{courseId}/teacher")
    public UserDto.Response teachersCourse(@PathVariable Long courseId) {
        CourseDto.Response response = courseService.findById(courseId).orElse(null);
        if (response != null) {
            Optional<UserDto.Response> teacher = courseService.findTeacher(courseId);
            if (teacher.isPresent()) {
                return teacher.get();
            }
        }
        throw new IllegalArgumentException("Course not found");
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteById(courseId);
        return ResponseEntity.ok("Course deleted from course");
    }
}
