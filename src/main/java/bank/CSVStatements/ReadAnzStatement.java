package bank.CSVStatements;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ReadAnzStatement {
    private List<String> descriptionValues;
    private List<String> amountValues;
    private String statementMonth;
    private String statementYear;
    
    public ReadAnzStatement(String statementMonth, String statementYear) {
        this.statementMonth = statementMonth;
        this.statementYear = statementYear;

        String statementFilePath = getCSVStatementFile(statementMonth, statementYear);

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
    }

    public String getCSVStatementFile(String statementMonth, String statementYear) {
        //READING THE DOWNLOADED FILE

        //Reading the description and amount from the downloaded statement.

        String fileDownloadDirectoy = "/Users/zeanamansell/Downloads/";
        String monthFileName = "06-0821-0886101-00_Transactions_";
        String file = fileDownloadDirectoy + monthFileName + statementYear + "-" + statementMonth + ".csv";
        return file;
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
