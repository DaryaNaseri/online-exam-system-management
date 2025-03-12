package ir.maktabsharif.OnlineExamManagementProject.model.dto;

import jakarta.validation.constraints.*;

public interface ExamDto {

    record CreateRequest(
            @NotBlank(message = "Title must not be blank")
            String title,

            @NotBlank(message = "Description must not be blank")
            String description,

            @NotNull(message = "How long does this exam take?")
            Integer duration
    ) {
    }


    record EditRequest(
            @NotNull(message = "Id must not be null")
            Long id,

            String title,

            String description,

            Integer duration
    ) {
    }

    record Response(Long id,
                    String title,
                    String description,
                    Integer duration
    ) {
    }

    record SessionResponse(
            Long id,
            String title,
            String description,
            Integer duration,
            boolean completed
    ){}

}
