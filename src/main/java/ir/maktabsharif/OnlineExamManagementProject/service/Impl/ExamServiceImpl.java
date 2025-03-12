package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.DuplicateExamTitleException;
import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidInputException;
import ir.maktabsharif.OnlineExamManagementProject.exception.ResourceNotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.TeacherNotAssignedToCourseException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.*;
import ir.maktabsharif.OnlineExamManagementProject.repository.*;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamService;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;
    private CourseService courseService;
    private CourseRepository courseRepository;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;
    private StudentExamRepository studentExamRepository;

    public ExamServiceImpl(StudentExamRepository studentExamRepository, StudentRepository studentRepository, ExamRepository examRepository, CourseService courseService, CourseRepository courseRepository, TeacherRepository teacherRepository) {
        this.examRepository = examRepository;
        this.studentExamRepository = studentExamRepository;
        this.courseService = courseService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<ExamDto.Response> findByCourse(Long courseId, Long teacherId) {
        Course foundCourseById = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User foundUserById = teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!(foundUserById instanceof Teacher teacher)) {
            throw new InvalidInputException("User is not a teacher");
        }

        if (!foundCourseById.getTeacher().equals(foundUserById)) {
            throw new TeacherNotAssignedToCourseException(teacherId, courseId);
        }

        List<Exam> examByCourse = examRepository.findExamByCourse(foundCourseById);

        if (!examByCourse.isEmpty()) {
            return examByCourse.stream().map(this::convertToExamResponse).toList();
        }
        throw new InvalidInputException("there is no exam in this course");

    }


    private ExamDto.Response convertToExamResponse(Exam exam) {
        return new ExamDto.Response(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getDuration()
        );
    }


    public ExamDto.Response create(Long courseId, Long teacherId, ExamDto.CreateRequest examDto) {

        if (!courseService.isTeacherOfCourse(teacherId, courseId)) {
            throw new TeacherNotAssignedToCourseException(teacherId, courseId);
        }

        if (isTitleExistsInCourse(examDto.title(), courseId)) {
            throw new DuplicateExamTitleException("An exam with this title already exists in this course.");
        }

        Exam save = examRepository.save(Exam.builder()
                .title(examDto.title())
                .description(examDto.description())
                .duration(examDto.duration())
                .course(courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found")))
                .teacher(teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("User not found")))
                .build());
        return convertToExamResponse(save);
    }

    @Override
    public List<ExamDto.Response> findAvailableExams(Long courseId, Long studentId) {
        Course foundCourse = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found"));

        List<Exam> exams = examRepository.findExamByCourse(foundCourse);

        List<StudentExam> studentExams = studentExamRepository.findByStudentId(studentId);


        List<Long> completedExamIdsByStudent = studentExamRepository.findCompletedExamIdsByStudentId(studentId);

        List<Exam> availableExams = exams.stream()
                .filter(exam -> studentExams.stream()
                        .noneMatch(se ->
                                se.getExam().getId().equals(exam.getId()) &&
                                        se.isCompleted()))
                .toList();


        return availableExams.stream().map(this::convertToExamResponse).toList();
    }

    private Boolean isTitleExistsInCourse(String title, Long courseId) {
        Course foundCourse = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return examRepository.existsByTitleAndCourse(title, foundCourse);
    }


    public void delete(Long examId, Long teacherId, Long courseId) {
        if (!courseService.isTeacherOfCourse(teacherId, courseId)) {
            throw new TeacherNotAssignedToCourseException(teacherId, courseId);
        }
        Course foundCourse = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Exam exam = examRepository.findById(examId).orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if (!foundCourse.getExams().contains(exam)) {
            throw new InvalidInputException("This exam does not belong to this course");
        }
        examRepository.delete(exam);
    }

    public ExamDto.Response update(ExamDto.EditRequest examDto, Long teacherId, Long courseId) {

        if (!courseService.isTeacherOfCourse(teacherId, courseId)) {
            throw new TeacherNotAssignedToCourseException(teacherId, courseId);
        }

        Course foundCourse = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Exam exam = Exam.builder()
                .title(examDto.title())
                .description(examDto.description())
                .duration(examDto.duration())
                .build();

        if (!foundCourse.getExams().contains(exam)) {
            throw new InvalidInputException("this Exam does not belong to this Course");
        }

        Exam found = examRepository.findById(examDto.id()).orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        found.setTitle(Util.getUpdatedValue(examDto.title(), found.getTitle()));
        found.setDescription(Util.getUpdatedValue(examDto.description(), found.getDescription()));
        found.setDuration(Util.getUpdatedValue(examDto.duration(), found.getDuration()));

        Exam save = examRepository.save(found);

        return convertToExamResponse(save);

    }


}
