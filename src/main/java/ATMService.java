import lombok.Setter;

import java.math.BigDecimal;

public class ATMService {

    Bank bankService;
    BankAccount currentBankAccount;
    @Setter private BigDecimal cashBalance;

    private int failedPinAttempts;

    public ATMService (Bank bankService, BigDecimal cashBalance) {
        this.bankService = bankService;
        this.cashBalance = cashBalance;
    }

    public void setCurrentBankAccount(String cardNum){
        currentBankAccount = bankService.getAccountByCardNum(cardNum);
    }

    public void incrementFailedAttempts ()
    {
        failedPinAttempts++;
        if(failedPinAttempts >=3){
            bankService.blockAccount(currentBankAccount);
        }
    }

    public BigDecimal getBalance(){
        return bankService.getBalance(currentBankAccount);
    }

    public void deposit(BigDecimal value){
        bankService.deposit(currentBankAccount, value);
        cashBalance = cashBalance.add(value);
    }

    public WithdrawStatus tryToWithdraw(BigDecimal withdrawValue){

        if(withdrawValue.compareTo(cashBalance) > 0 && !bankService.tryToWithdraw(currentBankAccount, withdrawValue) ){
            return WithdrawStatus.NotEnoughMoneyOnAccount;
        }

        if(withdrawValue.compareTo(cashBalance) > 0){
            return WithdrawStatus.NotEnoughMoneyOnATM;
        }

        if(!bankService.tryToWithdraw(currentBankAccount, withdrawValue)){
            return WithdrawStatus.NotEnoughMoneyOnAccount;
        }

        return WithdrawStatus.Success;

     //   return bank.tryToWithdraw(currentBankAccount, withdrawValue);
    }

    public boolean isCardBlocked(){
        return bankService.isCardBlocked(currentBankAccount);
    }

    public String getValidCardNum(String inputCard){
        String cardNum = inputCard.replaceAll("-", "");
        if (cardNum.length() != 16 && cardNum.matches("\\d{16}") == false) {
            return null;
        }
        return cardNum;
    }

}
