package ir.maktabsharif.OnlineExamManagementProject.security;

import ir.maktabsharif.OnlineExamManagementProject.model.RegistrationStatus;
import ir.maktabsharif.OnlineExamManagementProject.model.UserRole;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.User;
import ir.maktabsharif.OnlineExamManagementProject.model.permision.Authority;
import ir.maktabsharif.OnlineExamManagementProject.model.permision.Role;
import ir.maktabsharif.OnlineExamManagementProject.repository.UserRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.auth.AuthorityRepository;
import ir.maktabsharif.OnlineExamManagementProject.repository.auth.RoleRepository;
import ir.maktabsharif.OnlineExamManagementProject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private UserService userService;
    private AuthorityRepository authorityRepository;
    private RoleRepository roleRepository;

    public DataInitializer(UserService userService, RoleRepository roleRepository, AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        User admin = userService
                .registerAdmin(
                        RegistrationStatus.ACTIVE,
                        "admin@gamil.com",
                        "Admin1",
                        "adminadmin",
                        UserRole.ADMIN
                );

        Authority viewUserList = new Authority();
        viewUserList.setName("VIEW_USER_LIST");
        authorityRepository.save(viewUserList);

        Authority approveUser = new Authority();
        approveUser.setName("APPROVE_USER");
        authorityRepository.save(approveUser);

        Authority manageUserRoles = new Authority();
        manageUserRoles.setName("MANAGE_USER_ROLES");
        authorityRepository.save(manageUserRoles);

        Authority filterAndSearchUsers = new Authority();
        filterAndSearchUsers.setName("FILTER_AND_SEARCH_USERS");
        authorityRepository.save(filterAndSearchUsers);

        Authority viewCourseList = new Authority();
        viewCourseList.setName("VIEW_COURSE_LIST");
        authorityRepository.save(viewCourseList);

        Authority createCourse = new Authority();
        createCourse.setName("CREATE_COURSE");
        authorityRepository.save(createCourse);

        Authority assignTeacherToCourse = new Authority();
        assignTeacherToCourse.setName("ASSIGN_TEACHER_TO_COURSE");
        authorityRepository.save(assignTeacherToCourse);

        Authority addStudentToCourse = new Authority();
        addStudentToCourse.setName("ADD_STUDENT_TO_COURSE");
        authorityRepository.save(addStudentToCourse);

        Authority editStudentList = new Authority();
        editStudentList.setName("EDIT_STUDENT_LIST");
        authorityRepository.save(editStudentList);

        Authority deleteCourse = new Authority();
        deleteCourse.setName("DELETE_COURSE");
        authorityRepository.save(deleteCourse);

        Authority viewTeacherOfCourse = new Authority();
        viewTeacherOfCourse.setName("VIEW_TEACHER_OF_COURSE");
        authorityRepository.save(viewTeacherOfCourse);

        Authority viewStudentsOfCourse = new Authority();
        viewStudentsOfCourse.setName("VIEW_STUDENTS_OF_COURSE");
        authorityRepository.save(viewStudentsOfCourse);

        Authority editCourse = new Authority();
        editCourse.setName("EDIT_COURSE");
        authorityRepository.save(editCourse);

        Authority allExamsOfCourse = new Authority();
        allExamsOfCourse.setName("ALL_EXAMS_OF_COURSE");
        authorityRepository.save(allExamsOfCourse);

        Authority createExam = new Authority();
        createExam.setName("CREATE_EXAM");
        authorityRepository.save(createExam);

        Authority editExam = new Authority();
        editExam.setName("EDIT_EXAM");
        authorityRepository.save(editExam);

        Authority deleteExam = new Authority();
        deleteExam.setName("DELETE_EXAM");
        authorityRepository.save(deleteExam);

        Authority viewTeacherCourse = new Authority();
        viewTeacherCourse.setName("VIEW_TEACHER_COURSE");
        authorityRepository.save(viewTeacherCourse);

//        Authority addQuestionToExam = new Authority();
//        addQuestionToExam.setName("ADD_QUESTION_TO_EXAM");
//        authorityRepository.save(addQuestionToExam);
//
//        Authority giveGrades = new Authority();
//        giveGrades.setName("GIVE_GRADES");
//        authorityRepository.save(giveGrades);
//
//        Authority takeExam = new Authority();
//        takeExam.setName("TAKE_EXAM");
//        authorityRepository.save(takeExam);
//
//        Authority viewResult = new Authority();
//        viewResult.setName("VIEW_RESULTS");
//        authorityRepository.save(viewResult);


        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole.getAuthorities().add(approveUser);
        adminRole.getAuthorities().add(viewUserList);
        adminRole.getAuthorities().add(manageUserRoles);
        adminRole.getAuthorities().add(filterAndSearchUsers);
        adminRole.getAuthorities().add(viewCourseList);
        adminRole.getAuthorities().add(createCourse);
        adminRole.getAuthorities().add(assignTeacherToCourse);
        adminRole.getAuthorities().add(addStudentToCourse);
        adminRole.getAuthorities().add(editStudentList);
        adminRole.getAuthorities().add(deleteCourse);
        adminRole.getAuthorities().add(editCourse);
        adminRole.getAuthorities().add(viewTeacherOfCourse);
        adminRole.getAuthorities().add(viewStudentsOfCourse);
        roleRepository.save(adminRole);

        Role teacherRole = new Role();
        teacherRole.setName("ROLE_TEACHER");
        teacherRole.getAuthorities().add(viewTeacherCourse);
        teacherRole.getAuthorities().add(allExamsOfCourse);
        teacherRole.getAuthorities().add(editExam);
        teacherRole.getAuthorities().add(deleteExam);
        teacherRole.getAuthorities().add(createExam);
        roleRepository.save(teacherRole);

        Role studentRole = new Role();
        studentRole.setName("ROLE_STUDENT");
        roleRepository.save(studentRole);

        admin.getRoles().add(adminRole);
        userRepository.save(admin);


    }
}

