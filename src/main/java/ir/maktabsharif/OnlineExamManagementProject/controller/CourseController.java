package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.CourseCodeMustBeUniqueException;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
import ir.maktabsharif.OnlineExamManagementProject.service.Impl.CourseServiceImpl;
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
    public ResponseEntity<CourseDto.Response> createCourse(@Valid @RequestBody CourseDto.CreateCourseRequestDto course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CourseDto.Response response = courseService.create(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDto.Response>> allCourses() {
        List<CourseDto.Response> courses = courseService.findAll();
        return ResponseEntity.status(HttpStatus.FOUND).body(courses);
    }


    @PostMapping("/{courseId}/add-student/{userId}")
    public ResponseEntity<CourseDto.CourseStudentsListDto> addStudentToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        CourseDto.CourseStudentsListDto courseStudentsListDto = courseService.addStudentToCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(courseStudentsListDto);
    }


    @PostMapping("/{courseId}/assign-teacher/{userId}")
    public ResponseEntity<CourseDto.CourseTeacherDto> assignTeacherToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        CourseDto.CourseTeacherDto courseTeacherDto = courseService.assignTeacherToCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(courseTeacherDto);
    }


    @PostMapping("/{courseId}/delete-student/{userId}")
    public ResponseEntity<Void> deleteStudentFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {

        courseService.deleteStudentFromCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{courseId}/unassign-teacher/{userId}")
    public ResponseEntity<Void> unassignTeacherFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        courseService.unassignTeacherFromCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/{courseId}/allStudents")
    public ResponseEntity<List<UserDto.Response>> allStudentsCourse(@PathVariable Long courseId) {
        List<UserDto.Response> students = courseService.findAllStudents(courseId);
        return ResponseEntity.status(HttpStatus.FOUND).body(students);
    }


    @GetMapping("/{courseId}/teacher")
    public ResponseEntity<UserDto.Response> teachersCourse(@PathVariable Long courseId) {
        UserDto.Response teacher = courseService.findTeacher(courseId);
        return ResponseEntity.status(HttpStatus.FOUND).body(teacher);
    }


    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteById(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/teacher-courses/{teacherId}")
    public ResponseEntity<List<CourseDto.Response>> getMyCourses(@PathVariable Long teacherId) {
        List<CourseDto.Response> coursesByTeacherId = courseService.findCoursesByTeacherId(teacherId);
        return ResponseEntity.status(HttpStatus.FOUND).body(coursesByTeacherId);
    }


}
