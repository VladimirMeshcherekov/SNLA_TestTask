package сustomException;

public class ExceededMaximumDepositAmountException extends Exception{
    public ExceededMaximumDepositAmountException (String message) {
        super(message);
    }
}
