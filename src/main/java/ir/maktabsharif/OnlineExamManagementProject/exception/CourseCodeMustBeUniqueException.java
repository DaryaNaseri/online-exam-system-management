package ir.maktabsharif.OnlineExamManagementProject.exception;

public class CourseCodeMustBeUniqueException extends RuntimeException {
    public CourseCodeMustBeUniqueException(String message) {
        super(message);
    }
}
