import java.math.BigDecimal;
import java.util.Scanner;

public class ATMView {
    Scanner scanner = new Scanner(System.in);
    ATMService service;
    Bank bankService;
    String currentCardNum = "";
    final BigDecimal MAX_DEPOSIT_VALUE = new BigDecimal(1000000);

    public ATMView (ATMService service, Bank bankService) {
        this.service = service;
        this.bankService = bankService;
        start();
    }

    public void start () {
        while (true) {
            if (currentCardNum == null || currentCardNum.isEmpty()) {
                System.out.println("Inject card");
                String CardNum = scanner.nextLine();
                currentCardNum = CardNum;
            }

            currentCardNum = service.getValidCardNum(currentCardNum);
            if(currentCardNum == null){
                System.out.println("Card num is not valid");
                continue;
            }

            if (bankService.getAccountByCardNum(currentCardNum) == null && !currentCardNum.isEmpty()) {
                System.out.println("Card not exist");
                currentCardNum = null;
                continue;
            }

            service.setCurrentBankAccount(currentCardNum);

            if(service.isCardBlocked()){
                System.out.println("Card is blocked, ejecting");
                service.setCurrentBankAccount(null);
                currentCardNum = null;
                continue;
            }

            System.out.println("Enter pin");
            String cardPinInput = scanner.nextLine();
          //  scanner.nextLine();
            if(cardPinInput.length() != 4 || cardPinInput.matches("\\d{4}") == false){
                System.out.println("pin is not valid");
                continue;
            }

            if (bankService.getAccountByCardNum(currentCardNum).isPinValid(Integer.parseInt(cardPinInput)) == false) {
                System.out.println("Pincode wrong");
                service.incrementFailedAttempts();
                continue;
            }




            boolean keepUsing = true;
            while (keepUsing) {

                System.out.println("1: Check Balance");
                System.out.println("2: Deposit");
                System.out.println("3: Withdraw");
                System.out.println("4: Eject Card");

                int currentAction = scanner.nextInt();
                scanner.nextLine();

                switch (currentAction) {
                    case 1:
                        System.out.println("Balance: " + service.getBalance());
                        break;
                    case 2:
                        System.out.println("Enter deposit value (max: 1 000 000)");
                        String depositInput = scanner.nextLine();
                        BigDecimal depositValue;

                        try {
                            depositValue = new BigDecimal(depositInput);
                        }catch (Exception e){
                            System.out.println("Deposit value input is not valid");
                            break;
                        }

                        if(depositValue.signum() == -1){
                            System.out.println("You cant deposit negative value((");
                            break;
                        }

                        if(depositValue.compareTo(MAX_DEPOSIT_VALUE) > 0){
                            System.out.println("Deposit error: max deposit value is 1 000 000");
                            break;
                        }
                        service.deposit(new BigDecimal(depositInput));
                        break;
                    case 3:
                        System.out.println("Enter withdraw value");
                        String withdrawInput = scanner.nextLine();
                        BigDecimal withdrawValue;

                        try{
                            withdrawValue = new BigDecimal(withdrawInput);
                        }catch (Exception e){
                            System.out.println("Withdraw value input is not valid");
                            break;
                        }

                        if(withdrawValue.signum() == -1){
                            System.out.println("You cant withdraw negative value((");
                            break;
                        }

                        switch (service.tryToWithdraw(withdrawValue)){
                            case Success ->   System.out.println("Success withdraw");
                            case NotEnoughMoneyOnAccount -> System.out.println("Not enough money on account");
                            case NotEnoughMoneyOnATM -> System.out.println("Not enough money on ATM");
                        }

                        break;
                    case 4:
                        keepUsing = false;
                        service.setCurrentBankAccount(null);
                        currentCardNum = null;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }


            }
        }
    }
}
