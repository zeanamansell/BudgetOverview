package bank.CSVStatements;

import java.util.ArrayList;
import java.util.List;

public class ProcessBankStatement {
    private List<String> descriptionValues;
    private List<String> amountValues;
    private List<String> masterDescriptionValues;
    private List<String> masterCategoryValues;
    private List<String> monthlyCategories;
    private List<Float> monthlySpend;

    public ProcessBankStatement(List<String> descriptionValues, List<String> amountValues, List<String> masterDescriptionValues, List<String> masterCategoryValues) {
        this.descriptionValues = descriptionValues;
        this.amountValues = amountValues;
        this.masterDescriptionValues = masterDescriptionValues;
        this.masterCategoryValues = masterCategoryValues;
    }


    public ProcessBankStatement invoke() {
        List<String> monthlyCategorisedItems = categoriseMonthlyItems();
        monthlyCategories = getCategories(monthlyCategorisedItems);
        monthlySpend = calculateMonthlySpend(monthlyCategorisedItems);

        return this;
    }

    public List<Float> calculateMonthlySpend(List<String> monthlyCategorisedItems) {
        //Calculating how much was spent in each category
        List<Float> monthlySpend = new ArrayList<Float>();

        //Populate the monthly spend list with all zeros the size of the monthly categorised items list

        for (int i = 0; i < monthlyCategorisedItems.size(); i++) {
            monthlySpend.add((float) 0);
        }

        for (int i = 0; i < monthlyCategories.size(); i++) {
            for (int k = 0; k < monthlyCategorisedItems.size(); k++) {
                if (monthlyCategories.get(i).equals(monthlyCategorisedItems.get(k))) {
                    float totalSpend = Float.parseFloat(amountValues.get(k)) + monthlySpend.get(i);
                    monthlySpend.set(i, totalSpend);
                }
            }
        }
        return monthlySpend;
    }

    public List<String> getCategories(List<String> monthlyCategorisedItems) {
        //Put the categories into a list where they occur only once in the list
        List <String> monthlyCategories = new ArrayList<String>();

        for (String category: monthlyCategorisedItems) {
            if (!monthlyCategories.contains(category)) {
                monthlyCategories.add(category);
            }
        }
        return  monthlyCategories;
    }

    public List<String> categoriseMonthlyItems() {
        //Put the categories that relate to the items in the statement into a list of categories that were used that month
        List<String> monthlyCategorisedItems = new ArrayList<String>();

        for (String item: descriptionValues) {
            if (masterDescriptionValues.contains(item)) {
                int i = masterDescriptionValues.indexOf(item);
                monthlyCategorisedItems.add(masterCategoryValues.get(i));
            } else {
                monthlyCategorisedItems.add(item);
            }
        }

        return monthlyCategorisedItems;
    }

    public List<String> getMonthlyCategories() {

        return monthlyCategories;
    }

    public List<Float> getMonthlySpend() {

        return monthlySpend;
    }
}
