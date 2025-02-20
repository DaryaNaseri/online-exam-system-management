package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidPasswordException;
import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.UserService;
import ir.maktabsharif.OnlineExamManagementProject.service.Impl.UserServiceImpl;
import ir.maktabsharif.OnlineExamManagementProject.utility.Util;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto.SignupRequest signupRequestDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation failed,please Enter Correct Details" + bindingResult.getAllErrors().toString());
        }
        userService.save(signupRequestDto);
        return ResponseEntity.ok("Signup successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserDto.LoginRequest loginRequestDto,
                                            BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation failed,please Enter Correct Details");
        }

        try {
            UserDto.Response userResponse = userService.authenticate(loginRequestDto);

            session.setAttribute("loggedInUser", userResponse);

            return ResponseEntity.ok("Login successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<UserDto.Response> getAllUsers() {
        return userService.findAll();
    }


    @PutMapping("/approve/{id}")
    public UserDto.Response approveUser(@PathVariable Long id) {
        return userService.approveUser(id);
    }


    @PutMapping("/edit-user")
    public ResponseEntity<String> editUser(
            @Valid @RequestBody UserDto.EditUserRequest editRequest,
            BindingResult bindingResult,
            HttpSession session) {

        UserDto.Response loggedInUser = (UserDto.Response) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
        }

        UserDto.Response updatedUser = userService.editUserInfo(editRequest);

        return ResponseEntity.ok("User information updated successfully");
    }


    @GetMapping("/search-by-role")
    public List<UserDto.Response> getUsersByRole(@RequestParam String role) {
        return userService.searchUsersByRole(role).stream().map(Util::convertToUserResponse).toList();
    }


    @GetMapping("/search")
    public List<UserDto.Response> searchUsers(@RequestParam String searchedText) {
        return userService.searchUsersByFirstNameLastNameUsername(searchedText).stream().map(Util::convertToUserResponse).toList();
    }

    @GetMapping("/filter")
    public List<UserDto.Response> filterUsers(@RequestParam String role ,@RequestParam String searchedText) {
        return userService.filterByRoleAndSearchedWord(role,searchedText);
    }

}
