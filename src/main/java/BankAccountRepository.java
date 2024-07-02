import java.util.ArrayList;

public interface BankAccountRepository {
    public void saveToFile(ArrayList<BankAccount> list);
    public  ArrayList<BankAccount> loadFromFile ();
}
