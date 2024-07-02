package сustomException;

public class CardNotExistException extends Exception{
    public CardNotExistException (String message) {
        super(message);
    }
}
