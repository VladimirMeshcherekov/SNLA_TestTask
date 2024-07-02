import сustomException.*;

import java.math.BigDecimal;

public interface ATMService {
    public void setCurrentBankAccount (String cardNum);
    public void incrementFailedAttempts ();
    public BigDecimal getBalance ();
    public void deposit (BigDecimal value) throws NegativeAmountException, ExceededMaximumDepositAmountException;
    public boolean withdraw (BigDecimal withdrawValue) throws NegativeAmountException, NotEnoughMoneyOnAccountException, NotEnoughMoneyOnATMException;
    public boolean isCardBlocked ();
    public String getValidCardNum (String inputCard) throws InvalidCardNumberException;
    public boolean enterPin (int pin);
    public boolean enterCard (String currentCardNum) throws CardNotExistException;
}
