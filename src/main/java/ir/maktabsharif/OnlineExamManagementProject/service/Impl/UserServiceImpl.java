package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidInputException;
import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidPasswordException;
import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.UsernameOrEmailMustBeUniqueException;
import ir.maktabsharif.OnlineExamManagementProject.model.*;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Student;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;
import ir.maktabsharif.OnlineExamManagementProject.repository.StudentRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.TeacherRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.UserRepository;
import ir.maktabsharif.OnlineExamManagementProject.security.BCryptPasswordEncode;
import ir.maktabsharif.OnlineExamManagementProject.service.UserService;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           StudentRepository studentRepository,
                           TeacherRepository teacherRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }


    @Override
    public void save(UserDto.SignupRequest entity) {
        if (userRepository.findUserByUsername(entity.username()).isPresent()) {
            throw new UsernameOrEmailMustBeUniqueException("this username has been taken");
        } else if (userRepository.findUserByEmail(entity.email()).isPresent()) {
            throw new UsernameOrEmailMustBeUniqueException("this email has been used");
        }
        saveUser(entity, entity.userRole());
    }


    private void saveUser(UserDto.SignupRequest entity, UserRole role) {
        User user;
        if (role == UserRole.STUDENT) {
            user = new Student(entity.email(), entity.username(), entity.password(), role, RegistrationStatus.PENDING);
            studentRepository.save((Student) user);
        } else if (role == UserRole.TEACHER) {
            user = new Teacher(entity.email(), entity.username(), entity.password(), role, RegistrationStatus.PENDING);
            teacherRepository.save((Teacher) user);
        }
    }


    @Override
    public List<UserDto.Response> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new NotFoundException("There are no users in the database");
        }

        return users.stream()
                //user -> convertToUserResponse(user)
                .map(Util::convertToUserResponse)
                .collect(Collectors.toList());
    }



    @Override
    public UserDto.Response approveUser(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setStatus(RegistrationStatus.APPROVED);
            User updatedUser = this.save(user);
            return Util.convertToUserResponse(updatedUser);
        }).orElseThrow(() -> new NotFoundException("There is no such user with this id" + id));
    }


    @Override
    public Optional<UserDto.Response> findById(Long id) {
        return userRepository.findById(id)
                .map(Util::convertToUserResponse);
    }


    @Override
    public List<User> searchUsersByRole(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            throw new RuntimeException("searchText cannot be null or empty");
        }

        UserRole role = safeGetUserRole(searchText);

        return userRepository.findByRole(role);
    }


    private UserRole safeGetUserRole(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid role: " + role);
        }
    }


    @Override
    public UserDto.Response authenticate(UserDto.LoginRequest loginRequest) {

        User user = userRepository.findUserByUsername(loginRequest.username())
                .orElseThrow(() -> new NotFoundException("User not found with username: " + loginRequest.username()));

        if (!BCryptPasswordEncode.verifyBCryptPassword(loginRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password for user: " + loginRequest.username());
        }

        return Util.convertToUserResponse(user);
    }


    @Override
    public UserDto.Response editUserInfo(UserDto.EditUserRequest editUserRequest) {

        User user = userRepository.findById(editUserRequest.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Util.getUpdatedValue(editUserRequest.getRole(), user.getRole()));
        user.setUsername(Util.getUpdatedValue(editUserRequest.getUsername(), user.getUsername()));
        user.setEmail(Util.getUpdatedValue(editUserRequest.getEmail(), user.getEmail()));
        user.setFirstName(Util.getUpdatedValue(editUserRequest.getFirstName(), user.getFirstName()));
        user.setLastName(Util.getUpdatedValue(editUserRequest.getLastName(), user.getLastName()));
        user.setStatus(Util.getUpdatedValue(editUserRequest.getStatus(), user.getStatus()));

        if (editUserRequest.getPassword() != null &&
                !BCryptPasswordEncode.verifyBCryptPassword(editUserRequest.getPassword(), user.getPassword())) {

                user.setPassword(BCryptPasswordEncode.encodeBCryptPassword(editUserRequest.getPassword()));
            }


        userRepository.save(user);

        return Util.convertToUserResponse(user);
    }


    @Override
    public List<User> searchUsersByFirstNameLastNameUsername(String searchText) {
        return userRepository.findBySearchedWord(searchText.trim().toLowerCase());
    }


    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }


    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


}
