package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.DuplicateExamTitleException;
import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Course;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Exam;
import ir.maktabsharif.OnlineExamManagementProject.repository.CourseRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.ExamRepository;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
import ir.maktabsharif.OnlineExamManagementProject.service.ExamService;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;
    private CourseService courseService;
    private CourseRepository courseRepository;

    public ExamServiceImpl(ExamRepository examRepository, CourseService courseService, CourseRepository courseRepository) {
        this.examRepository = examRepository;
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    public List<ExamDto.Response> findByCourse(Long courseId) {
        Course foundCourseById = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found"));

        return examRepository.findExamByCourse(foundCourseById).stream().map(this::convertToExamResponse).toList();

    }


    private ExamDto.Response convertToExamResponse(Exam exam) {
        return new ExamDto.Response(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getDuration()
        );
    }


    public ExamDto.Response create(Long courseId, ExamDto.CreateRequest examDto) {
        if (isExists(examDto.title(), courseId)) {
            throw new DuplicateExamTitleException("An exam with this title already exists in this course.");
        }
        Exam save = examRepository.save(Exam.builder()
                .title(examDto.title())
                .description(examDto.description())
                .duration(examDto.duration())
                        .course(courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found")))
                .build());
        return convertToExamResponse(save);
    }

    private Boolean isExists(String title, Long courseId) {
        Course foundCourse = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found"));
        return examRepository.existsByTitleAndCourse(title, foundCourse);
    }


    public void delete(Long examId) {
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new NotFoundException("Exam not found"));
        examRepository.delete(exam);
    }

    public ExamDto.Response update(ExamDto.EditRequest examDto) {

        Exam found = examRepository.findById(examDto.id()).orElseThrow(() -> new NotFoundException("Exam not found"));

        found.setTitle(Util.getUpdatedValue(examDto.title(), found.getTitle()));
        found.setDescription(Util.getUpdatedValue(examDto.description(), found.getDescription()));
        found.setDuration(Util.getUpdatedValue(examDto.duration(), found.getDuration()));

        Exam save = examRepository.save(found);

        return convertToExamResponse(save);

    }


}
