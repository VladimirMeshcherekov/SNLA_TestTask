import java.math.BigDecimal;

public class Main {

    public static void main (String[] args) {
        BankService bankService = new BankServiceImpl();
         ATMService atmService = new ATMServiceImpl(bankService, new BigDecimal(500));
         ATMView consoleView = new ATMConsoleView(atmService);
         consoleView.start();
    }
}