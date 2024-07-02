import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class BankAccount {
    public  String cardNum;
    private BigDecimal balance;
    private int pin;
    @JsonProperty("blocked")
    private boolean isBlocked;
    private String blockDate;

    public BankAccount(){}

    public BankAccount(String CardNum, int pin, BigDecimal balance){
        this.cardNum = CardNum;
        this.pin = pin;
        this.balance = balance;
        this.isBlocked = false;
    }

    public boolean isPinValid(int pin){
        return this.pin == pin;
    }

    public void deposit(BigDecimal depositValue){
         balance = balance.add(depositValue);
    }

    public boolean tryToWithdraw(BigDecimal withdrawValue )
    {
        if(balance.compareTo(withdrawValue) >=0)
        {
            balance = balance.subtract(withdrawValue);
            return true;
        }
        return false;
    }

    public void blockCard(){
        this.isBlocked = true;
        this.blockDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void unblock(){
        isBlocked = false;
    }

}
