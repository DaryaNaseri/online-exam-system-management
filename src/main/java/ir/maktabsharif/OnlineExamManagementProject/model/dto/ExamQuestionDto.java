package ir.maktabsharif.OnlineExamManagementProject.model.dto;

public interface ExamQuestionDto {

    record Response(
            Long examId,
            String examTitle,
            Long questionId,
            String questionContent,
            Double score
    ){

    }

    record ScoreResponse(
            Long examId,
            String examTitle,
            Double score){}
}
