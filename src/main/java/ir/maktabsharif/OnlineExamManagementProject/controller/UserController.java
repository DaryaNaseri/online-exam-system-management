package ir.maktabsharif.OnlineExamManagementProject.controller;

import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidPasswordException;
import ir.maktabsharif.OnlineExamManagementProject.exception.NotFoundException;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.CourseDto;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;
import ir.maktabsharif.OnlineExamManagementProject.model.dto.UserDto;
import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
    public ResponseEntity<UserDto.Response> registerUser(@Valid @RequestBody UserDto.SignupRequest signupRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserDto.Response response = userService.save(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


//    @PostMapping("/login")
//    public ResponseEntity<UserDto.Response> loginUser(@Valid @RequestBody UserDto.LoginRequest loginRequestDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        UserDto.Response userResponse = userService.authenticate(loginRequestDto);
//        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto.Response>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.findAll());
    }


    @PutMapping("/approve/{id}")
    public ResponseEntity<UserDto.Response> approveUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.approveUser(id));
    }


    @PutMapping("/edit-user")
    public ResponseEntity<UserDto.Response> editUser(@Valid @RequestBody UserDto.EditUserRequest editRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDto.Response updatedUser = userService.editUserInfo(editRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }


    @GetMapping("/search-by-role")
    public ResponseEntity<List<UserDto.Response>> getUsersByRole(@RequestParam String role) {
        List<UserDto.Response> list = userService.searchUsersByRole(role).stream().map(Util::convertToUserResponse).toList();

        return ResponseEntity.status(HttpStatus.FOUND).body(list);
    }


    @GetMapping("/search")
    public ResponseEntity<List<UserDto.Response>> searchUsers(@RequestParam String searchedText) {
        List<UserDto.Response> list = userService.searchUsersByFirstNameLastNameUsername(searchedText).stream().map(Util::convertToUserResponse).toList();
        return ResponseEntity.status(HttpStatus.FOUND).body(list);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserDto.Response>> filterUsers(@RequestParam String role, @RequestParam String searchedText) {
        List<UserDto.Response> responseList = userService.filterByRoleAndSearchedWord(role, searchedText);
        return ResponseEntity.status(HttpStatus.FOUND).body(responseList);
    }


}
