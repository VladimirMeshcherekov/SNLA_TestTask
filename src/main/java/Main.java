import java.math.BigDecimal;

public class Main {

    public static void main (String[] args) {
        BankService bankService = new BankServiceServiceImpl();
         ATMService atmService = new ATMService(bankService, new BigDecimal(500));
         ATMView consoleView = new ConsoleView(atmService, bankService);
         consoleView.start();
    }
}