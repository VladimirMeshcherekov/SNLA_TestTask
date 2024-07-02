import сustomException.*;

import java.math.BigDecimal;
import java.util.Scanner;

public class ATMConsoleView implements ATMView {
    private Scanner scanner = new Scanner(System.in);
    private ATMService atmService;
    private String currentCardNum = null;

    public ATMConsoleView (ATMService atmService) {
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


            try {
                if (!atmService.enterCard(currentCardNum) && !currentCardNum.isEmpty()) {
                }
            } catch (CardNotExistException e) {
                System.out.println("Card is not exist");
                currentCardNum = null;
                continue;
            }

            atmService.setCurrentBankAccount(currentCardNum);

            if(atmService.isCardBlocked()){
                System.out.println("Card is blocked, ejecting");
                ejectCard();
                continue;
            }

            System.out.println("Enter pin");
            String cardPinInput = scanner.nextLine();
            if(cardPinInput.length() != 4 || !cardPinInput.matches("\\d{4}")) {
                System.out.println("pin is not valid");
                continue;
            }

            if (!atmService.enterPin(Integer.parseInt(cardPinInput))) {
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
                            atmService.deposit(depositValue);
                        } catch (NegativeAmountException e) {
                            System.out.println("You cant deposit negative value((");
                        } catch (ExceededMaximumDepositAmountException e) {
                            System.out.println("Deposit error: max deposit value is 1 000 000");
                        }catch (Exception e){
                            System.out.println("Deposit value input is not valid");
                            break;
                        }
                        break;
                    case 3:
                        System.out.println("Enter withdraw value");
                        String withdrawInput = scanner.nextLine();
                        BigDecimal withdrawValue;

                        try {
                            withdrawValue = new BigDecimal(withdrawInput);
                            if(atmService.withdraw(withdrawValue)){
                                System.out.println("Success withdraw");
                            }
                        } catch (NegativeAmountException e) {
                            System.out.println("You cant withdraw negative value((");
                        } catch (NotEnoughMoneyOnAccountException e) {
                            System.out.println("Not enough money on account");
                        } catch (NotEnoughMoneyOnATMException e) {
                            System.out.println("Not enough money on ATM");
                        }catch (Exception e){
                            System.out.println("Withdraw value input is not valid");
                            break;
                        }

                        break;
                    case 4:
                        keepUsing = false;
                        ejectCard();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        }
    }

    private void ejectCard (){
        atmService.setCurrentBankAccount(null);
        currentCardNum = null;
    }

}
