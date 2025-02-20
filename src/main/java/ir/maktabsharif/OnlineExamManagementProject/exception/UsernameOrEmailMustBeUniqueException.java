package ir.maktabsharif.OnlineExamManagementProject.exception;

public class UsernameOrEmailMustBeUniqueException extends RuntimeException {
    public UsernameOrEmailMustBeUniqueException(String message) {
        super(message);
    }
}
