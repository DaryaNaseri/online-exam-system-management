package ir.maktabsharif.OnlineExamManagementProject.service.Impl;

import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidInputException;
import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidPasswordException;
import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.exception.UsernameOrEmailMustBeUniqueException;
import ir.maktabsharif.OnlineExamManagementProject.model.*;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Admin;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Student;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;
import ir.maktabsharif.OnlineExamManagementProject.repository.AdminRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.StudentRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.TeacherRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.UserRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.auth.RoleRepository;
import ir.maktabsharif.OnlineExamManagementProject.service.UserService;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
private AdminRepository adminRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           StudentRepository studentRepository,
                           TeacherRepository teacherRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.adminRepository = adminRepository;
    }


    @Override
    public UserDto.Response save(UserDto.SignupRequest entity) {
        if (userRepository.findByUsername(entity.username()).isPresent()) {
            throw new UsernameOrEmailMustBeUniqueException("this username has been taken");
        } else if (userRepository.findUserByEmail(entity.email()).isPresent()) {
            throw new UsernameOrEmailMustBeUniqueException("this email has been used");
        }
        return saveUser(entity);
    }

    @Override
    public User registerAdmin(RegistrationStatus status,String email, String password, String username, UserRole role) {
        Admin user = new Admin();
        user.setStatus(status);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return adminRepository.save(user);
    }


    private UserDto.Response saveUser(UserDto.SignupRequest entity) {
        User user = new User();
        String password = passwordEncoder.encode(entity.password());

        if (entity.userRole() == UserRole.STUDENT) {
            user = new Student(entity.email(), password, entity.username(), entity.userRole(), RegistrationStatus.PENDING);
            user.getRoles().add(roleRepository.findByName(entity.userRole().name()));
            studentRepository.save((Student) user);

        } else if (entity.userRole() == UserRole.TEACHER) {
            user = new Teacher(entity.email(), password, entity.username(), entity.userRole(), RegistrationStatus.PENDING);
            user.getRoles().add(roleRepository.findByName(entity.userRole().name()));
            teacherRepository.save((Teacher) user);

        }

        return Util.convertToUserResponse(user);

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
    public List<User> searchUsersByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new RuntimeException("searchText cannot be null or empty");
        }

        UserRole userRole = safeGetUserRole(role);

        return userRepository.findByRole(userRole);
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
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new NotFoundException("User not found with username: " + loginRequest.username()));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password for user: " + loginRequest.username());
        }

        return Util.convertToUserResponse(user);
    }


    @Override
    public UserDto.Response editUserInfo(UserDto.EditUserRequest editUserRequest) {

        User user = userRepository.findById(editUserRequest.id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Util.getUpdatedValue(editUserRequest.role(), user.getRole()));
        user.setUsername(Util.getUpdatedValue(editUserRequest.username(), user.getUsername()));
        user.setEmail(Util.getUpdatedValue(editUserRequest.email(), user.getEmail()));
        user.setFirstName(Util.getUpdatedValue(editUserRequest.firstName(), user.getFirstName()));
        user.setLastName(Util.getUpdatedValue(editUserRequest.lastName(), user.getLastName()));
        user.setStatus(Util.getUpdatedValue(editUserRequest.status(), user.getStatus()));

        if (editUserRequest.password() != null &&
                !passwordEncoder.matches(editUserRequest.password(), user.getPassword())) {

            user.setPassword(passwordEncoder.encode(editUserRequest.password()));
        }


        userRepository.save(user);

        return Util.convertToUserResponse(user);
    }


    @Override
    public List<User> searchUsersByFirstNameLastNameUsername(String searchText) {
        return userRepository.findBySearchedWord(searchText.trim().toLowerCase());
    }

    @Override
    public List<UserDto.Response> filterByRoleAndSearchedWord(String role, String searchedWord) {
        if (role == null || role.trim().isEmpty()) {
            throw new RuntimeException("searchText cannot be null or empty");
        }

        UserRole userRole = safeGetUserRole(role);
        List<User> users = userRepository.filterByRoleAndSearchedWord(userRole, searchedWord.trim().toLowerCase());
        return users.stream().map(Util::convertToUserResponse).collect(Collectors.toList());
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
