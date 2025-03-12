package ir.maktabsharif.OnlineExamManagementProject.model.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface AnswerDto {

    record SaveRequest(
            @NotNull
            Long studentId,
            @NotNull
            Long examId,
            @NotNull
            Long questionId,
            List<Long> selectedChoiceIds,
            String descriptiveAnswer
    ) {
        public SaveRequest(Long questionId, List<Long> selectedChoices, Long examId, Long studentId) {
            this(studentId, examId, questionId, selectedChoices, "");
        }

        public SaveRequest(Long studentId, Long examId, Long questionId, String descriptiveAnswer) {
            this(studentId, examId, questionId, List.of(), descriptiveAnswer);
        }
    }

    record Response(
            Long studentId,
            Long examId,
            Long questionId,
            boolean completed
    ){}

    record AnswerResponse(
            Long studentId,
            Long examId,
            Long questionId,
            List<OptionDto.Response> selectedChoiceIds,
            String descriptiveAnswer
    ){
        public AnswerResponse(Long studentId, Long examId, Long questionId, String descriptiveAnswer) {
            this(studentId, examId, questionId, List.of(), descriptiveAnswer);
        }

        public AnswerResponse(Long studentId, Long examId, Long questionId, List<OptionDto.Response> selectedChoiceIds) {
            this(studentId, examId, questionId, selectedChoiceIds, "");
        }
    }

}
