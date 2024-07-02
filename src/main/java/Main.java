import java.math.BigDecimal;

public class Main {

    public static void main (String[] args) {
        Bank bankService = new BankService();
         ATMService atmService = new ATMService(bankService, new BigDecimal(500));
         ATMView atmView = new ATMView(atmService, bankService);
    }
}