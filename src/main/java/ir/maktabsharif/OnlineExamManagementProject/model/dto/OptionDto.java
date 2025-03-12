package ir.maktabsharif.OnlineExamManagementProject.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface OptionDto {
    record CreateRequest(
            @NotBlank(message = "text must be initiate")
            String text,
            @NotNull(message = "is this option is correct or wrong (true/false)")
            Boolean isCorrect
    ) {
    }


record Response(
        Long id,
        String Text
){}
}
