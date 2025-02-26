package ir.maktabsharif.OnlineExamManagementProject.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.maktabsharif.OnlineExamManagementProject.model.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

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

}
