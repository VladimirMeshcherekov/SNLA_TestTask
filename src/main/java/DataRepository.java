import java.util.ArrayList;

public interface DataRepository {
    public void saveToFile(ArrayList<BankAccount> list);
    public  ArrayList<BankAccount> loadFromFile ();
}
