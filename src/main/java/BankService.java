import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class BankService extends Bank {

    private ArrayList<BankAccount> accounts = new ArrayList<>();

    DataRepository repository = new CardRepository();
    public BankService () {

        accounts = repository.loadFromFile();
        // accounts.add(new BankAccount("123", 123, new BigDecimal(100)));
        //accounts.add(new BankAccount("1234", 123, new BigDecimal(2)));
        //accounts.add(new BankAccount("12345", 123, new BigDecimal(3)));
    }

    public BankAccount getAccountByCardNum (String cardNum) {
        Optional<BankAccount> result = accounts.stream()
                .filter(account -> account.cardNum.equals(cardNum))
                .findFirst();
        return result.orElse(null);
    }

    public BigDecimal getBalance (BankAccount account) {
        return account.getBalance();
    }

    public void deposit (BankAccount account, BigDecimal depositValue) {
        account.deposit(depositValue);
       updateCardRepository();
    }

    public boolean tryToWithdraw (BankAccount account, BigDecimal withdrawValue) {
        boolean withdrawStatus = account.tryToWithdraw(withdrawValue);
        if(withdrawStatus == true){
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

        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(LocalDateTime.parse(account.getBlockDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME) , currentTime);

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
