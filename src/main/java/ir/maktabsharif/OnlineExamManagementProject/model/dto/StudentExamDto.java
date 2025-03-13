package ir.maktabsharif.OnlineExamManagementProject.model.dto;

public interface StudentExamDto {

    record Response(
            String number,
            String username,
            Double score
    ){

    }

}
