package web.ANZ;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AnzHomePage {
    private WebDriver driver;

    public AnzHomePage (WebDriver driver) {
        this.driver = driver;
        this.driver.get("https://www.anz.co.nz/personal/");
    }

    public AnzLogInPage goToLoginPage() {
        // Click login button
        WebElement loginButton = this.driver.findElement(By.id("skip_logon"));
        loginButton.click();
        return PageFactory.initElements(this.driver, AnzLogInPage.class);
    }
}
