package CustomExceptions;

public class NotEnoughMoneyOnATMException extends Exception{
    public NotEnoughMoneyOnATMException (String message) {
        super(message);
    }
}
