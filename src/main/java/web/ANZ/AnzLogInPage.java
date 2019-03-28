package web.ANZ;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AnzLogInPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public AnzLogInPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 10);
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("footer-copyright")));
    }

    public void enterLoginDetails(String userCustomerNumber, String customerPassword) {
        //Enter login details and click login
        String customerNumber = userCustomerNumber;
        String password = customerPassword;

        WebElement idInput = driver.findElement(By.id("user-id"));
        WebElement passwordInput = driver.findElement(By.id("password"));

        idInput.sendKeys(customerNumber);
        passwordInput.sendKeys(password);
    }

    public AnzUserHomePage logIn() {
        WebElement loginSubmitButton = driver.findElement(By.id("submit"));
        loginSubmitButton.click();
        return PageFactory.initElements(this.driver, AnzUserHomePage.class);
    }
}
