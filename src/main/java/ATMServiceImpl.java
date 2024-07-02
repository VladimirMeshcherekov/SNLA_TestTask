import lombok.Setter;
import сustomException.*;

import java.math.BigDecimal;

public class ATMServiceImpl implements ATMService {

    BankService bankService;
    BankAccount currentBankAccount;
    @Setter
    private BigDecimal cashBalance;
    final BigDecimal MAX_DEPOSIT_VALUE = new BigDecimal(1000000);

    private int failedPinAttempts;

    public ATMServiceImpl (BankService bankService, BigDecimal cashBalance) {
        this.bankService = bankService;
        this.cashBalance = cashBalance;
    }

    public void setCurrentBankAccount (String cardNum) {
        currentBankAccount = bankService.getAccountByCardNum(cardNum);
    }

    public void incrementFailedAttempts () {
        failedPinAttempts++;
        if (failedPinAttempts >= 3) {
            bankService.blockAccount(currentBankAccount);
        }
    }

    public BigDecimal getBalance () {
        return bankService.getBalance(currentBankAccount);
    }

    public void deposit (BigDecimal value) throws NegativeAmountException, ExceededMaximumDepositAmountException {

        if (value.signum() == -1) {
            throw new NegativeAmountException("deposit value is negative");
        }

        if (value.compareTo(MAX_DEPOSIT_VALUE) > 0) {
            throw new ExceededMaximumDepositAmountException("deposit amount is exceeded");
        }

        bankService.deposit(currentBankAccount, value);
        cashBalance = cashBalance.add(value);
    }

    public boolean withdraw (BigDecimal withdrawValue) throws NegativeAmountException, NotEnoughMoneyOnAccountException, NotEnoughMoneyOnATMException {

        if (withdrawValue.signum() == -1) {
            throw new NegativeAmountException("withdraw value is negative");
        }

        if (withdrawValue.compareTo(cashBalance) > 0 && !bankService.withdraw(currentBankAccount, withdrawValue)) {
            throw new NotEnoughMoneyOnAccountException("not enough money on account");
        }

        if (withdrawValue.compareTo(cashBalance) > 0) {
            throw new NotEnoughMoneyOnATMException("not enough money on ATM");
        }

        if (!bankService.withdraw(currentBankAccount, withdrawValue)) {
            throw new NotEnoughMoneyOnAccountException("not enough money on account");
        }

        return true;

        //   return bank.tryToWithdraw(currentBankAccount, withdrawValue);
    }

    public boolean isCardBlocked () {
        return bankService.isCardBlocked(currentBankAccount);
    }

    public String getValidCardNum (String inputCard) throws InvalidCardNumberException {
        String cardNum = inputCard.replaceAll("-", "");
        if (!cardNum.matches("\\d{16}")) {
            throw new InvalidCardNumberException("Card num is invalid");
        }
        return cardNum;
    }

    public boolean enterPin (int pin) {
        if (currentBankAccount.isPinValid(pin)) {
            return true;
        }
        incrementFailedAttempts();
        return false;
    }

    public boolean enterCard (String currentCardNum) throws CardNotExistException{
        currentBankAccount = bankService.getAccountByCardNum(currentCardNum);
        if(currentBankAccount == null){
            throw new CardNotExistException("Card not exists");
        }
        return true;
    }

}
