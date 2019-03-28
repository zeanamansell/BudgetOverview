import bank.CSVStatements.ProcessBankStatement;
import bank.CSVStatements.ReadAnzStatement;
import bank.CSVStatements.ReadMasterFile;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import web.ANZ.AnzAccountPage;
import web.ANZ.AnzHomePage;
import web.ANZ.AnzLogInPage;
import web.ANZ.AnzUserHomePage;

import java.util.List;
import java.util.Scanner;

public class HelloWorld {

    private static WebDriver driver;
    private static AnzHomePage anzHomePage;
    private static AnzLogInPage anzLogInPage;
    private static AnzUserHomePage anzUserHomePage;
    private static AnzAccountPage anzAccountPage;
    private static ReadAnzStatement readAnzStatement;
    private static ReadMasterFile readMasterFile;
    private static ProcessBankStatement processBankStatement;

    public static void main(String[] args) throws InterruptedException {
        //Enter login details and click login
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter Customer Number: ");
        String customerNumber = userInput.next();

        System.out.println("Enter Password: ");
        String password = userInput.next();

        userInput.close();

        setUpClass();
        setUpBrowser();

        getAnzBankStatement(customerNumber, password);

        getBudgetOverview();

        tearDownBrowser();
    }

    private static void getBudgetOverview() {
        readAnzStatement = new ReadAnzStatement().invoke();

        List<String> descriptionValues = readAnzStatement.getDescriptionValues();
        List<String> amountValues = readAnzStatement.getAmountValues();

        readMasterFile = new ReadMasterFile().invoke();

        List<String> masterDescriptionValues = readMasterFile.getMasterDescriptionValues();
        List<String> masterCategoryValues = readMasterFile.getMasterCategoryValues();

        processBankStatement = new ProcessBankStatement(descriptionValues, amountValues, masterDescriptionValues, masterCategoryValues).invoke();

        List<String> monthlyCategories = processBankStatement.getMonthlyCategories();
        List<Float> monthlySpend = processBankStatement.getMonthlySpend();

        printBudgetOutline(monthlyCategories, monthlySpend);
    }

    //
    private static void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static void setUpBrowser() {
        driver = new ChromeDriver();
        anzHomePage = PageFactory.initElements(driver, AnzHomePage.class);
    }

    private static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    private static void getAnzBankStatement(String customerNumber, String password) throws InterruptedException {
        anzLogInPage = anzHomePage.goToLoginPage();
        anzLogInPage.enterLoginDetails(customerNumber, password);

        anzUserHomePage = anzLogInPage.logIn();
        anzAccountPage = anzUserHomePage.goToAccount();
        anzAccountPage.exportCSVStatement();
        anzAccountPage.logOut();
    }


    private static void printBudgetOutline(List<String> monthlyCategories, List<Float> monthlySpend) {
        System.out.println("Overview of spending for March 2019: ");
        for (int i = 0; i < monthlyCategories.size(); i++) {
            System.out.println(monthlyCategories.get(i) + ": " + monthlySpend.get(i));
        }
    }
}
