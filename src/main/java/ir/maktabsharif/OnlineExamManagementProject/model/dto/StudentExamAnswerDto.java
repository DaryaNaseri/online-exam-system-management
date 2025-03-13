package ir.maktabsharif.OnlineExamManagementProject.model.dto;

import java.time.LocalDateTime;

public interface StudentExamAnswerDto {

    record Request(
            Long studentId,
            Long examId,
            Long questionId,
            double score
    ){}

    record Response(
            String StudentUsername,
            String teacherName,
            String ExamTitle,
            LocalDateTime ExamTime,
            boolean isGraded
    ){}
}
