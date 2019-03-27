import com.opencsv.CSVReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.notification.RunListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;
import sun.security.x509.EDIPartyName;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.not;

public class ChromeBankStatement {

    private WebDriver driver;
    private static ChromeOptions chromeOptions;

    @BeforeClass
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setUpBrowser() {
        driver = new ChromeDriver();
        driver.get("https://www.anz.co.nz/personal/");
    }

    @After
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void readCsvFile() {

        //Reading the description and amount from the downloaded statement.
        String statementFilePath = "/Users/zeanamansell/Downloads/06-0821-0886101-00_Transactions_2019-03.csv";
        List<String> descriptionValues = new ArrayList<String>();
        List<String> amountValues = new ArrayList<String>();

        try {
            CSVReader reader = new CSVReader(new FileReader(statementFilePath));

            List<String[]> allValues = reader.readAll();

            for (int i = 1; i < allValues.size(); i++) {
                String[] line = allValues.get(i);
                String description = line[3];
                String amount = line[5];
                descriptionValues.add(description);
                amountValues.add(amount);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Reading the description and category from the master file
        String masterFilePath = "/Users/zeanamansell/Desktop/BudgetAppFiles/master2.csv";
        List<String> masterDescriptionValues = new ArrayList<String>();
        List<String> masterCategoryValues = new ArrayList<String>();

        try {
            CSVReader reader = new CSVReader(new FileReader(masterFilePath));

            List<String[]> allValuesMaster = reader.readAll();

            for (int i = 1; i < allValuesMaster.size(); i++) {
                String[] line = allValuesMaster.get(i);
                String masterDescription = line[0];
                String masterCategory = line[1];
                masterDescriptionValues.add(masterDescription);
                masterCategoryValues.add(masterCategory);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        //Put the categories into a list where they occur only once in the list
        List<String> monthlyCategories = new ArrayList<String>();

        for (String category: monthlyCategorisedItems) {
            if (!monthlyCategories.contains(category)) {
                monthlyCategories.add(category);
            }
        }
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

        for (int i = 0; i < monthlyCategories.size(); i++) {
            System.out.println(monthlyCategories.get(i) + ": " + monthlySpend.get(i));
        }

    }

    @Test
    public void logIn() throws InterruptedException {
        // Click login button
        WebElement loginButton = driver.findElement(By.cssSelector("#skip_logon"));
        loginButton.click();

        //Enter login details and click login
        String customerNumber = "82816810";
        String password = "BC7C8EDC9A";

        WebElement idInput = driver.findElement(By.id("user-id"));
        WebElement passwordInput = driver.findElement(By.id("password"));

        idInput.sendKeys(customerNumber);
        passwordInput.sendKeys(password);

        WebElement loginSubmitButton = driver.findElement(By.id("submit"));
        loginSubmitButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ib-app-container > div > div.d-flex.align-items-center > h1")));

        //Navigate to Visa Debit account
        WebElement yourAccountsDropbox = driver.findElement(By.cssSelector("#main-menu > ul > li.menu-item.menu-item-transumm > a > span"));
        yourAccountsDropbox.click();
        List<WebElement> accountOptions = driver.findElements(By.className("account-name"));
        accountOptions.get(0).click();
        Thread.sleep(3000);

        //Export CSV statements
        WebElement exportSettingsButton = driver.findElement(By.id("transactions-export-panel-toggle"));
        exportSettingsButton.click();
        Thread.sleep(3000);

        Select dateRangeDropbox = new Select(driver.findElement(By.name("date-range")));
        dateRangeDropbox.selectByVisibleText("March 2019");

//        WebElement startDateInput = driver.findElement(By.name("start-date"));
//        startDateInput.sendKeys("01/03/2019");
//
//        WebElement endDateInput = driver.findElement(By.name("end-date"));
//        endDateInput.sendKeys("25/03/2019");

        Select fileFormat = new Select(driver.findElement(By.id("transactions-export-format")));
        fileFormat.selectByVisibleText("CSV - Comma Separated Values");

        WebElement exportButton = driver.findElement(By.id("transaction-export-submit"));
        exportButton.click();

        Thread.sleep(5000);

        //Logoff
        WebElement logOffButton = driver.findElement(By.id("logout"));
        logOffButton.click();

    }

}