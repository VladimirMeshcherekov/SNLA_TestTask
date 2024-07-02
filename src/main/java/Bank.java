import java.math.BigDecimal;

public abstract class Bank {
    public abstract BankAccount getAccountByCardNum (String cardNum);
    public abstract BigDecimal getBalance (BankAccount account);
    public abstract void deposit (BankAccount account, BigDecimal depositValue);
    public abstract boolean tryToWithdraw (BankAccount account, BigDecimal withdrawValue);
    public abstract void blockAccount (BankAccount account);
    public abstract boolean isCardBlocked (BankAccount account);
}
