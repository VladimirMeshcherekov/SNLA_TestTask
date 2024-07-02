package сustomException;

public class WrongCardPinException extends Exception{
    public WrongCardPinException (String message) {
        super(message);
    }
}
