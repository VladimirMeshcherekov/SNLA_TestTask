import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CardRepository implements DataRepository {

    private String filePath = "Cards.json";
    public CardRepository(){}

    public void saveToFile (ArrayList<BankAccount> list){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(new File(filePath), list);
        } catch (IOException e) {
            System.out.println("Unable to load data to file");
            throw new RuntimeException(e);
        }
    }

    public  ArrayList<BankAccount> loadFromFile () {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println("Unable to read data from file");
            return objectMapper.readValue(new File(filePath), new TypeReference<ArrayList<BankAccount>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
