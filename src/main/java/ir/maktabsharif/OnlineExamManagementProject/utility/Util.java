package ir.maktabsharif.OnlineExamManagementProject.utility;

import ir.maktabsharif.OnlineExamManagementProject.exception.AccountNotVerifiedException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.OptionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.QuestionDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.DescriptiveQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import ir.maktabsharif.OnlineExamManagementProject.model.enums.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;

public class Util {

    public static  <T> T getUpdatedValue(T newValue, T oldValue) {
        return (newValue != null) ? newValue : oldValue;
    }

    public static UserDto.Response convertToUserResponse(User user) {
        return new UserDto.Response(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getRole(),
                user.getStatus()
        );
    }


    public static void validateUserStatus(User user) {
        if (user.getStatus().equals(RegistrationStatus.PENDING)) {
            throw new AccountNotVerifiedException("Account must be approved first!");
        }
    }

    public static QuestionDto.ResponseDto convertToQuestionResponse(Question question) {
        String qType =null;
        if (question instanceof DescriptiveQuestion){
            qType="descriptive";
        }if (question instanceof MultipleChoiceQuestion){
            qType="multipleChoice";
        }
        return new QuestionDto.ResponseDto(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getTeacher().getId(),
                qType
        );
    }

    public static QuestionDto.ResponseDto mapToQuestionDto(Question question) {
        if (question instanceof MultipleChoiceQuestion mcq) {
            return new QuestionDto.MultiChoiceQuestionResponse(
                    mcq.getId(),
                    mcq.getTitle(),
                    mcq.getContent(),
                    mcq.getTeacher().getId(),
                    mcq.getOptions().stream()
                            .map(opt -> new OptionDto.Response(opt.getId(), opt.getText()))
                            .toList()
            );
        } else if (question instanceof DescriptiveQuestion dq) {
            return new QuestionDto.DescriptiveQuestionResponse(
                    dq.getId(),
                    dq.getTitle(),
                    dq.getContent(),
                    dq.getTeacher().getId()
            );
        } else {
            throw new IllegalStateException("Unexpected question type");
        }
    }

}
