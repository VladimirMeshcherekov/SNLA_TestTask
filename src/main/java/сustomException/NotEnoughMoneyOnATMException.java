package сustomException;

public class NotEnoughMoneyOnATMException extends Exception{
    public NotEnoughMoneyOnATMException (String message) {
        super(message);
    }
}
