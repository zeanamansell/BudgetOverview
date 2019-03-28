package web.ANZ;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import sun.security.util.PropertyExpander;

import java.util.List;

public class AnzUserHomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    public AnzUserHomePage (WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 10);
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ib-app-container > div > div.d-flex.align-items-center > h1")));

    }

    public AnzAccountPage goToAccount() {
        //Navigate to Visa Debit account
        WebElement yourAccountsDropbox = driver.findElement(By.cssSelector("#main-menu > ul > li.menu-item.menu-item-transumm > a > span"));
        yourAccountsDropbox.click();
        List<WebElement> accountOptions = driver.findElements(By.className("account-name"));
        accountOptions.get(0).click();

        return PageFactory.initElements(this.driver, AnzAccountPage.class);
    }
}
