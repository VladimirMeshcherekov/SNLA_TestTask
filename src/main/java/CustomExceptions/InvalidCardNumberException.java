package CustomExceptions;

public class InvalidCardNumberException extends Exception {
    public InvalidCardNumberException(String message) {
        super(message);
    }
}
