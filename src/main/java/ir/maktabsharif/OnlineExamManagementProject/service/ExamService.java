package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.dto.ExamDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ExamService {

    List<ExamDto.Response> findByCourse(Long courseId);

    ExamDto.Response update(ExamDto.EditRequest examDto);

    void delete(Long examId);

    ExamDto.Response create(Long courseId,ExamDto.CreateRequest examDto);

}
