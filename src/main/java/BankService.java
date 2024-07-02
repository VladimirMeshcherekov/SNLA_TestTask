import java.math.BigDecimal;

public interface BankService {
    public BankAccount getAccountByCardNum (String cardNum);
    public BigDecimal getBalance (BankAccount account);
    public void deposit (BankAccount account, BigDecimal depositValue);
    public boolean withdraw (BankAccount account, BigDecimal withdrawValue);
    public void blockAccount (BankAccount account);
    public boolean isCardBlocked (BankAccount account);
}
