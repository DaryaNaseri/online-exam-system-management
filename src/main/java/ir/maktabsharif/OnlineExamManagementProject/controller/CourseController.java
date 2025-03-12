package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamService;
import ir.maktabsharif.OnlineExamManagementProject.service.Impl.CourseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_COURSE')")
    public ResponseEntity<CourseDto.Response> createCourse(
            @Valid @RequestBody CourseDto.CreateCourseRequestDto course,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CourseDto.Response response = courseService.create(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('VIEW_COURSE_LIST')")
    public ResponseEntity<List<CourseDto.Response>> allCourses() {
        List<CourseDto.Response> courses = courseService.findAll();
        return ResponseEntity.status(HttpStatus.FOUND).body(courses);
    }


    @PostMapping("/{courseId}/add-student/{userId}")
    @PreAuthorize("hasAuthority('ADD_STUDENT_TO_COURSE')")
    public ResponseEntity<CourseDto.CourseStudentsListDto> addStudentToCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId) {
        CourseDto.CourseStudentsListDto courseStudentsListDto = courseService.addStudentToCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(courseStudentsListDto);
    }


    @PostMapping("/{courseId}/assign-teacher/{userId}")
    @PreAuthorize("hasAuthority('ASSIGN_TEACHER_TO_COURSE')")
    public ResponseEntity<CourseDto.CourseTeacherDto> assignTeacherToCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId) {
        CourseDto.CourseTeacherDto courseTeacherDto = courseService.assignTeacherToCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(courseTeacherDto);
    }


    @PostMapping("/{courseId}/delete-student/{userId}")
    @PreAuthorize("hasAuthority('EDIT_STUDENT_LIST')")
    public ResponseEntity<Void> deleteStudentFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId) {

        courseService.deleteStudentFromCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("/{courseId}/unassign-teacher/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> unassignTeacherFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId) {
        courseService.unassignTeacherFromCourse(courseId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/{courseId}/allStudents")
    @PreAuthorize("hasAuthority('VIEW_STUDENTS_OF_COURSE')")
    public ResponseEntity<List<UserDto.Response>> allStudentsCourse(
            @PathVariable Long courseId) {
        List<UserDto.Response> students = courseService.findAllStudents(courseId);
        return ResponseEntity.status(HttpStatus.FOUND).body(students);
    }


    @GetMapping("/{courseId}/teacher")
    @PreAuthorize("hasAuthority('VIEW_TEACHER_OF_COURSE')")
    public ResponseEntity<UserDto.Response> teachersCourse(
            @PathVariable Long courseId) {
        UserDto.Response teacher = courseService.findTeacher(courseId);
        return ResponseEntity.status(HttpStatus.FOUND).body(teacher);
    }


    @PutMapping("update")
    @PreAuthorize("hasAuthority('EDIT_COURSE')")
    public ResponseEntity<CourseDto.Response> updateCourse(
            @RequestBody CourseDto.EditRequest courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(courseDTO));
    }


    @DeleteMapping("/delete/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId) {
        courseService.deleteById(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/teacher-courses/{teacherId}")
    @PreAuthorize("hasAuthority('VIEW_TEACHER_COURSE')")
    public ResponseEntity<List<CourseDto.Response>> getMyCourses(
            @PathVariable Long teacherId) {
        List<CourseDto.Response> coursesByTeacherId = courseService.findCoursesByTeacherId(teacherId);
        return ResponseEntity.status(HttpStatus.FOUND).body(coursesByTeacherId);
    }

}
