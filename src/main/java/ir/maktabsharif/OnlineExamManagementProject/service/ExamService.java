package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ExamService {

    List<ExamDto.Response> findByCourse(Long courseId,Long teacherId);

    ExamDto.Response update(ExamDto.EditRequest examDto,Long teacherId,Long courseId);

    void delete(Long examId,Long teacherId,Long courseId);

    ExamDto.Response create(Long courseId,Long teacherId,ExamDto.CreateRequest examDto);

    List<ExamDto.Response> findAvailableExams (Long courseId,Long studentId);

    Double completedExamTotalScore(Long examId, Long studentId);
}
