package CustomExceptions;

public class NotEnoughMoneyOnAccountException extends Exception{
    public NotEnoughMoneyOnAccountException (String message) {
        super(message);
    }
}
