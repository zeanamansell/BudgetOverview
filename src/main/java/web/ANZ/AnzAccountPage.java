package web.ANZ;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AnzAccountPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public AnzAccountPage (WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("transaction-search-input"))); //Waits until search bar has loaded
    }

    public void exportCSVStatement() throws InterruptedException {
        openExportParams();

        selectExportParams();

        downloadCSVStatement();

        Thread.sleep(5000);
    }

    public void openExportParams() {
        //Export CSV statements
        WebElement exportSettingsButton = driver.findElement(By.id("transactions-export-panel-toggle"));
        exportSettingsButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("transaction-export-submit")));
//        Thread.sleep(3000);
    }
    public void selectExportParams() {
        Select dateRangeDropbox = new Select(driver.findElement(By.name("date-range")));
        dateRangeDropbox.selectByVisibleText("March 2019");

        Select fileFormat = new Select(driver.findElement(By.id("transactions-export-format")));
        fileFormat.selectByVisibleText("CSV - Comma Separated Values");
    }

    public void downloadCSVStatement() {
        WebElement exportButton = driver.findElement(By.id("transaction-export-submit"));
        exportButton.click();
    }

    public void logOut() {
        //Logoff
        WebElement logOffButton = driver.findElement(By.id("logout"));
        logOffButton.click();
    }
}
