package ir.maktabsharif.OnlineExamManagementProject.utility;

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

}
