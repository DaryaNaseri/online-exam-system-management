//package ir.maktabsharif.OnlineExamManagementProject.security;
//
//import ir.maktabsharif.OnlineExamManagementProject.model.entity.*;
//import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.DescriptiveQuestion;
//import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.MultipleChoiceQuestion;
//import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
//import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
//import ir.maktabsharif.OnlineExamManagementProject.model.enums.RegistrationStatus;
//import ir.maktabsharif.OnlineExamManagementProject.model.enums.UserRole;
//import ir.maktabsharif.OnlineExamManagementProject.model.permision.Authority;
//import ir.maktabsharif.OnlineExamManagementProject.model.permision.Role;
//import ir.maktabsharif.OnlineExamManagementProject.repository.*;
//import ir.maktabsharif.OnlineExamManagementProject.repository.auth.AuthorityRepository;
//import ir.maktabsharif.OnlineExamManagementProject.repository.auth.RoleRepository;
//import ir.maktabsharif.OnlineExamManagementProject.service.CourseService;
//import ir.maktabsharif.OnlineExamManagementProject.service.UserService;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Set;
//
//@Component
//@AllArgsConstructor
//public class DataInitializer implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private UserService userService;
//    private AuthorityRepository authorityRepository;
//    private RoleRepository roleRepository;
//    private CourseRepository courseRepository;
//    private CourseService courseService;
//    private ExamRepository examRepository;
//    private QuestionRepository questionRepository;
//    private ExamQuestionRepository examQuestionRepository;
//    private StudentExamRepository studentExamRepository;
//
//
//    @Override
//    public void run(String... args) throws Exception {
//
//
//        Authority viewUserList = createAuthority("VIEW_USER_LIST");
//        Authority approveUser = createAuthority("APPROVE_USER");
//        Authority manageUserRoles = createAuthority("MANAGE_USER_ROLES");
//        Authority filterAndSearchUsers = createAuthority("FILTER_AND_SEARCH_USERS");
//        Authority viewCourseList = createAuthority("VIEW_COURSE_LIST");
//        Authority createCourse = createAuthority("CREATE_COURSE");
//        Authority assignTeacherToCourse = createAuthority("ASSIGN_TEACHER_TO_COURSE");
//        Authority addStudentToCourse = createAuthority("ADD_STUDENT_TO_COURSE");
//        Authority editStudentList = createAuthority("EDIT_STUDENT_LIST");
//        Authority deleteCourse = createAuthority("DELETE_COURSE");
//        Authority editCourse = createAuthority("EDIT_COURSE");
//        Authority viewTeacherOfCourse = createAuthority("VIEW_TEACHER_OF_COURSE");
//        Authority viewStudentsOfCourse = createAuthority("VIEW_STUDENTS_OF_COURSE");
//        Authority allExamsOfCourse = createAuthority("ALL_EXAMS_OF_COURSE");
//        Authority createExam = createAuthority("CREATE_EXAM");
//        Authority editExam = createAuthority("EDIT_EXAM");
//        Authority deleteExam = createAuthority("DELETE_EXAM");
//        Authority viewTeacherCourse = createAuthority("VIEW_TEACHER_COURSE");
//
//        Authority viewQuestionBank = createAuthority("VIEW_QUESTION_BANK");
//        Authority addQuestionToExam = createAuthority("ADD_QUESTION_TO_EXAM");
//        Authority searchByTitle = createAuthority("SEARCH_BY_TITLE");
//        Authority updateScore = createAuthority("UPDATE_SCORE");
//        Authority viewExamQuestions = createAuthority("VIEW_EXAM_QUESTIONS");
//        Authority viewTotalScore = createAuthority("VIEW_TOTAL_SCORE");
//
//        Authority viewStudentCourses = createAuthority("VIEW_STUDENTS_COURSES");
//        Authority viewAvailableExams = createAuthority("VIEW_AVAILABLE_EXAM");
//        Authority getExamAccess = createAuthority("GET_EXAM_ACCESS");
//
//
//        Role adminRole = new Role();
//        adminRole.setName("ROLE_ADMIN");
//        adminRole.getAuthorities().addAll(Set.of(
//                approveUser, viewUserList, manageUserRoles, filterAndSearchUsers,
//                viewCourseList, createCourse, assignTeacherToCourse, addStudentToCourse,
//                editStudentList, deleteCourse, editCourse, viewTeacherOfCourse, viewStudentsOfCourse
//        ));
//        roleRepository.save(adminRole);
//
//        Role teacherRole = new Role();
//        teacherRole.setName("ROLE_TEACHER");
//        teacherRole.getAuthorities().addAll(Set.of(
//                viewTeacherCourse, allExamsOfCourse, editExam, deleteExam, createExam,
//                viewQuestionBank, addQuestionToExam, searchByTitle,
//                updateScore, viewExamQuestions, viewTotalScore
//        ));
//        roleRepository.save(teacherRole);
//
//        Role studentRole = new Role();
//        studentRole.setName("ROLE_STUDENT");
//        studentRole.getAuthorities().addAll(Set.of(
//                viewStudentCourses,viewAvailableExams,getExamAccess
//        ));
//        roleRepository.save(studentRole);
//
//        User admin = userService
//                .registerAdmin(
//                        RegistrationStatus.ACTIVE,
//                        "admin@gamil.com",
//                        "Admin1",
//                        "adminadmin",
//                        UserRole.ADMIN
//                );
//        admin.getRoles().add(adminRole);
//        userRepository.save(admin);
//
//
//        User teacher = userService
//                .registerTeacher(
//                        RegistrationStatus.APPROVED,
//                        "teacher@gmail.com",
//                        "Teacher1",
//                        "teacher",
//                        UserRole.TEACHER
//                );
//        teacher.getRoles().add(teacherRole);
//        userRepository.save(teacher);
//
//
//        User student = userService
//                .registerStudent(
//                        RegistrationStatus.APPROVED,
//                        "studennt@gmail.com",
//                        "Student1",
//                        "student",
//                        UserRole.STUDENT
//                );
//        student.getRoles().add(studentRole);
//        userRepository.save(student);
//
//
//        Course course1 = courseRepository.save(
//                Course.builder()
//                        .title("java")
//                        .courseCode("117")
//                        .startDate(LocalDate.of(2020, 2, 3))
//                        .endDate(LocalDate.of(2021, 2, 3))
//                        .teacher((Teacher) teacher)
//                        .build()
//        );
//
//        course1.getStudents().add((Student) student);
//        courseRepository.save(course1);
//
//        Course course2 = courseRepository.save(
//                Course.builder()
//                        .title("python")
//                        .courseCode("120")
//                        .startDate(LocalDate.of(2020, 2, 3))
//                        .endDate(LocalDate.of(2021, 2, 3))
//                        .teacher((Teacher) teacher)
//                        .build()
//        );
//
//        course2.getStudents().add((Student) student);
//        courseRepository.save(course2);
//
//
//
//        Exam exam1 = examRepository.save(
//                Exam.builder()
//                        .title("javaFinalExam")
//                        .description("final exam of this course1")
//                        .duration(30)
//                        .teacher((Teacher) teacher)
//                        .course(course1)
//                        .build());
//
//        Exam exam2 = examRepository.save(
//                Exam.builder()
//                        .title("javaCore")
//                        .description("exam about java core")
//                        .duration(30)
//                        .teacher((Teacher) teacher)
//                        .course(course1)
//                        .build()
//        );
//
//        if (exam1.getCourse().equals(course1)){
//            studentExamRepository.save(
//                    new StudentExam((Student) student,exam1,false));
//        }
//        if (exam2.getCourse().equals(course1)){
//            studentExamRepository.save(
//                    new StudentExam((Student) student,exam2,false));
//        }
//
//        Question question1 = questionRepository.save(
//                DescriptiveQuestion.builder()
//                        .title("OOPsConcepts")
//                        .content("count OOP concepts.")
//                        .teacher((Teacher) teacher)
//                        .build()
//        );
//
//        ExamQuestion examQuestion1 = examQuestionRepository.save(
//                new ExamQuestion(exam1, question1, 2.0));
//
//        Question question2 = questionRepository.save(
//                MultipleChoiceQuestion.builder()
//                        .title("mainMethod")
//                        .content("which one is not in java main method?")
//                        .teacher((Teacher) teacher)
//                        .build()
//        );
//        ((MultipleChoiceQuestion) question2).setOptions(
//                List.of(
//                        Options.builder()
//                                .question((MultipleChoiceQuestion) question2)
//                                .isCorrect(true)
//                                .text("class")
//                                .build(),
//                        Options.builder()
//                                .question((MultipleChoiceQuestion) question2)
//                                .isCorrect(false)
//                                .text("main")
//                                .build(),
//                        Options.builder()
//                                .question((MultipleChoiceQuestion) question2)
//                                .isCorrect(false)
//                                .text("public")
//                                .build(),
//                        Options.builder()
//                                .question((MultipleChoiceQuestion) question2)
//                                .isCorrect(false)
//                                .text("void")
//                                .build(),
//                        Options.builder()
//                                .question((MultipleChoiceQuestion) question2)
//                                .isCorrect(false)
//                                .text("static")
//                                .build())
//        );
//        questionRepository.save(question2);
//
//        ExamQuestion examQuestion2 = examQuestionRepository.save(
//                new ExamQuestion(exam1, question2, 2.0)
//        );
//
//        Question question3 = questionRepository.save(
//                DescriptiveQuestion.builder()
//                        .title("primitiveDataTypes")
//                        .content("how many primitive type we have.")
//                        .teacher((Teacher) teacher)
//                        .build()
//        );
//
//        ExamQuestion examQuestion3 = examQuestionRepository.save(
//                new ExamQuestion(exam1, question3, 5.0));
//
//        Question question4 = questionRepository.save(
//                DescriptiveQuestion.builder()
//                        .title("UnaryOperator")
//                        .content("count unary operator.")
//                        .teacher((Teacher) teacher)
//                        .build()
//        );
//
//        ExamQuestion examQuestion4 = examQuestionRepository.save(
//                new ExamQuestion(exam1, question4, 1.0));
//
//
//    }
//
//    private Authority createAuthority(String name) {
//        Authority authority = new Authority();
//        authority.setName(name);
//        return authorityRepository.save(authority);
//    }
//
//}
//
