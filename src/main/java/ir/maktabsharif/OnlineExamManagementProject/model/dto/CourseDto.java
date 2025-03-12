package ir.maktabsharif.OnlineExamManagementProject.model.dto;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.Student;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public interface CourseDto {

    record CreateCourseRequestDto(
            @NotBlank(message = "title must not be blank")
            String title,
            @NotBlank(message = "course must have code")
            String courseCode,
            @NotNull(message = "when course will started? ")
            LocalDate startDate,
            @NotNull(message = "when course will end? ")
            LocalDate endDate

    ){}

    record Response(
            Long id,
            String title,
            String courseCode,
            LocalDate startDate,
            LocalDate endDate
    ){}

    record CourseStudentsListDto(
            List<Student> students,
            Long id,
            String courseCode
    ){}

    record CourseTeacherDto(
            Teacher teacher,
            Long id,
            String courseCode
    ){}

    record EditRequest(
            @NotNull(message = "for edit you must enter course ID")
            Long id,
            @NotBlank(message = "title must not be blank")
            String title,
            @NotBlank(message = "course must have code")
            String courseCode,
            @NotNull(message = "when course will started? ")
            LocalDate startDate,
            @NotNull(message = "when course will end? ")
            LocalDate endDate
    ){}

}
