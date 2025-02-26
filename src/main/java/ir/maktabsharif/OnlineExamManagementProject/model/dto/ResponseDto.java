package ir.maktabsharif.OnlineExamManagementProject.model.dto;

public interface ResponseDto {

    record ApiError(
            int status,
            String message,
            String error
    ){}

}
