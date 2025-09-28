package za.co.capitec.capitecdisputeportalserver.exception;

public class DisputeNotFoundException extends RuntimeException {
    public DisputeNotFoundException(String message) {
        super(message);
    }
    
    public DisputeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
