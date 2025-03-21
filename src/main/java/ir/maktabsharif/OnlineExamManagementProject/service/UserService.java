package ir.maktabsharif.OnlineExamManagementProject.service;

import ir.maktabsharif.OnlineExamManagementProject.model.enums.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.enums.UserRole;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.base.BaseService;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<User> {

    UserDto.Response save(UserDto.SignupRequest entity);

    User registerAdmin(RegistrationStatus status, String email, String password, String username, UserRole role);
    User registerTeacher(RegistrationStatus status, String email, String password, String username, UserRole role);
    User registerStudent(RegistrationStatus status, String email, String password, String username, UserRole role);

    List<UserDto.Response> findAll();

    UserDto.Response approveUser(Long id);

    Optional<UserDto.Response> findById(Long id);

    List<User> searchUsersByRole(String role);

//    UserDto.Response authenticate(UserDto.LoginRequest loginRequest);

    UserDto.Response editUserInfo(UserDto.EditUserRequest editUserRequest);

    List<User> searchUsersByFirstNameLastNameUsername(String searchText);

    List<UserDto.Response> filterByRoleAndSearchedWord(String role, String searchedWord);

}
