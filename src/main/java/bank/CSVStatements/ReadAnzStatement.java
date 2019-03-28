package bank.CSVStatements;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadAnzStatement {
    private List<String> descriptionValues;
    private List<String> amountValues;


    public ReadAnzStatement invoke() {
        //READING THE DOWNLOADED FILE

        //Reading the description and amount from the downloaded statement.
        String statementFilePath = "/Users/zeanamansell/Downloads/06-0821-0886101-00_Transactions_2019-03.csv";

        descriptionValues = new ArrayList<String>();
        amountValues = new ArrayList<String>();

        try {

            List<String[]> allValues = readCSVStatement(statementFilePath);

            getDescriptionAndAmountValues(allValues);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void getDescriptionAndAmountValues(List<String[]> allValues) {
        for (int i = 1; i < allValues.size(); i++) {
            String[] line = allValues.get(i);
            String description = line[3];
            String amount = line[5];
            descriptionValues.add(description);
            amountValues.add(amount);
        }
    }

    public List<String[]> readCSVStatement(String statementFilePath) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(statementFilePath));

        return reader.readAll();
    }
    public List<String> getDescriptionValues() {

        return descriptionValues;
    }

    public List<String> getAmountValues() {

        return amountValues;
    }
}
