package сustomException;

public class NotEnoughMoneyOnAccountException extends Exception{
    public NotEnoughMoneyOnAccountException (String message) {
        super(message);
    }
}
