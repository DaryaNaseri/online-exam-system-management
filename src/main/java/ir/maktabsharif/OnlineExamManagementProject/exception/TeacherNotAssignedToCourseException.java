package ir.maktabsharif.OnlineExamManagementProject.exception;

public class TeacherNotAssignedToCourseException extends RuntimeException {
    public TeacherNotAssignedToCourseException(Long teacherId, Long courseId) {
        super("Teacher with ID " + teacherId + " is not assigned to course with ID " + courseId);
    }
}
