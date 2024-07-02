import CustomExceptions.*;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleView implements ATMView {
    Scanner scanner = new Scanner(System.in);
    ATMService atmService;
    String currentCardNum = null;

    public ConsoleView (ATMService atmService, BankService bankService) {
        this.atmService = atmService;
    }

    public void start () {
        while (true) {

            if (currentCardNum == null) {
                System.out.println("Inject card");
                currentCardNum = scanner.nextLine();
            }

            try {
                currentCardNum = atmService.getValidCardNum(currentCardNum);
            } catch (InvalidCardNumberException e) {
                System.out.println("Card num is not valid");
                currentCardNum = null;
                continue;
            }


            if (!atmService.tryToEnterCard(currentCardNum) && !currentCardNum.isEmpty()) {
                System.out.println("Card not exist");
                currentCardNum = null;
                continue;
            }

            atmService.setCurrentBankAccount(currentCardNum);

            if(atmService.isCardBlocked()){
                System.out.println("Card is blocked, ejecting");
                atmService.setCurrentBankAccount(null);
                currentCardNum = null;
                continue;
            }

            System.out.println("Enter pin");
            String cardPinInput = scanner.nextLine();
            if(cardPinInput.length() != 4 || !cardPinInput.matches("\\d{4}")){
                System.out.println("pin is not valid");
                continue;
            }

            if (!atmService.tryToEnterPin(Integer.parseInt(cardPinInput))) {
                System.out.println("Pincode wrong");
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
                        System.out.println("Balance: " + atmService.getBalance());
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

                        try {
                            atmService.deposit(new BigDecimal(depositInput));
                        } catch (NegativeAmountException e) {
                            System.out.println("You cant deposit negative value((");
                        } catch (ExceededMaximumDepositAmountException e) {
                            System.out.println("Deposit error: max deposit value is 1 000 000");
                        }
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

                        try {
                            if(atmService.tryToWithdraw(withdrawValue)){
                                System.out.println("Success withdraw");
                            }
                        } catch (NegativeAmountException e) {
                            System.out.println("You cant withdraw negative value((");
                        } catch (NotEnoughMoneyOnAccountException e) {
                            System.out.println("Not enough money on account");
                        } catch (NotEnoughMoneyOnATMException e) {
                            System.out.println("Not enough money on ATM");
                        }

                        break;
                    case 4:
                        keepUsing = false;
                        EjectCard();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        }
    }

    private void EjectCard(){
        atmService.setCurrentBankAccount(null);
        currentCardNum = null;
    }

}
