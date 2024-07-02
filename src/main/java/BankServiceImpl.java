import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BankServiceImpl implements BankService {

    private ArrayList<BankAccount> accounts = new ArrayList<>();

    BankAccountRepository repository = new JsonBankAccountRepository();

    public BankServiceImpl () {
        accounts = repository.loadFromFile();
    }

    public BankAccount getAccountByCardNum (String cardNum) {
        return accounts.stream()
                .filter(account -> account.getCardNum().equals(cardNum))
                .findFirst()
                .orElse(null);
    }

    public BigDecimal getBalance (BankAccount account) {
        return account.getBalance();
    }

    public void deposit (BankAccount account, BigDecimal depositValue) {
        account.deposit(depositValue);
       updateCardRepository();
    }

    public boolean withdraw (BankAccount account, BigDecimal withdrawValue) {
        boolean withdrawStatus = account.tryToWithdraw(withdrawValue);
        if(withdrawStatus){
            updateCardRepository();
        }
        return withdrawStatus;
    }

    public void blockAccount (BankAccount account) {
        account.blockCard();
        updateCardRepository();
    }

    public boolean isCardBlocked (BankAccount account) {
        boolean blockStatus = account.isBlocked();
        if (!blockStatus) {
            return false;
        }

        Duration duration = Duration.between(LocalDateTime.parse(account.getBlockDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME) , LocalDateTime.now());

        if (duration.toHours() >= 24) {
            account.unblock();
            updateCardRepository();
            return false;
        }
        return true;
    }

    private void updateCardRepository(){
        repository.saveToFile(accounts);
    }
}
