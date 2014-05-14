package main.java.validations;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
public class EntityNotFoundRuntimeException extends RuntimeException {

    public EntityNotFoundRuntimeException() {
    }

    public EntityNotFoundRuntimeException(String message) {
        super(message);
    }

    public EntityNotFoundRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
