import bank.CSVStatements.ProcessBankStatement;
import bank.CSVStatements.ReadAnzStatement;
import bank.CSVStatements.ReadMasterFile;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import web.ANZ.AnzAccountPage;
import web.ANZ.AnzHomePage;
import web.ANZ.AnzLogInPage;
import web.ANZ.AnzUserHomePage;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;

public class ChromeBankStatement {

    private WebDriver driver;
    private AnzHomePage anzHomePage;
    private AnzLogInPage anzLogInPage;
    private AnzUserHomePage anzUserHomePage;
    private AnzAccountPage anzAccountPage;
    private ReadAnzStatement readAnzStatement;
    private ReadMasterFile readMasterFile;
    private ProcessBankStatement processBankStatement;
    private String statementMonth;
    private String statementYear;

    private static ChromeOptions chromeOptions;

    @BeforeClass
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setUpBrowser() {
        driver = new ChromeDriver();
        this.anzHomePage = PageFactory.initElements(this.driver, AnzHomePage.class);
    }

    @After
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    public void getBudgetOutline() throws InterruptedException {

        getAnzBankStatement();

        getBudgetOverview();

    }

    public void getBudgetOverview() {
        readAnzStatement = new ReadAnzStatement(statementMonth, statementYear);

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


    public void getAnzBankStatement() throws InterruptedException {
        String userCustomerNumber = "82816810";
        String customerNumber = "BC7C8EDC9A";
        String statementMonth = "March";
        String statementYear = "2019";
        anzLogInPage = anzHomePage.goToLoginPage();
        anzLogInPage.enterLoginDetails(userCustomerNumber, customerNumber);

        anzUserHomePage = anzLogInPage.logIn();
        anzAccountPage = anzUserHomePage.goToAccount();
        anzAccountPage.exportCSVStatement(statementMonth, statementYear);
        anzAccountPage.logOut();
    }

    public void printBudgetOutline(List<String> monthlyCategories, List<Float> monthlySpend) {
        System.out.println("Overview of spending for March 2019: ");
        for (int i = 0; i < monthlyCategories.size(); i++) {
            System.out.println(monthlyCategories.get(i) + ": " + monthlySpend.get(i));
        }
    }

}