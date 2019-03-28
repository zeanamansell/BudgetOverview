package bank.CSVStatements;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadMasterFile {
    private List<String> masterDescriptionValues;
    private List<String> masterCategoryValues;


    public ReadMasterFile invoke() {
        //Reading the description and category from the master file
        String masterFilePath = "/Users/zeanamansell/Desktop/BudgetAppFiles/master2.csv";

        masterDescriptionValues = new ArrayList<String>();
        masterCategoryValues = new ArrayList<String>();

        try {
            List<String[]> allValuesMaster = readMasterCSVFile(masterFilePath);

            getDescriptionAndCategoryValues(allValuesMaster);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void getDescriptionAndCategoryValues(List<String[]> allValuesMaster) {
        for (int i = 1; i < allValuesMaster.size(); i++) {
            String[] line = allValuesMaster.get(i);
            String masterDescription = line[0];
            String masterCategory = line[1];
            masterDescriptionValues.add(masterDescription);
            masterCategoryValues.add(masterCategory);
        }
    }

    public List<String[]> readMasterCSVFile(String masterFilePath) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(masterFilePath));

        return reader.readAll();
    }

    public List<String> getMasterDescriptionValues() {

        return masterDescriptionValues;
    }

    public List<String> getMasterCategoryValues() {

        return masterCategoryValues;
    }
}
