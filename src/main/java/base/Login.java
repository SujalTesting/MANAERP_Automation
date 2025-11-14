package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Login {

private WebDriver driver;
    
    private By userEmail = By.id("email");
    private By password = By.id("password");
    private By rememberCheckBox = By.cssSelector("#rememberMe");
    private By submitButton = By.cssSelector("[type='submit']");

    public Login(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String email) {
        driver.findElement(userEmail).sendKeys(email);
    }

    public void enterPassword(String pass) {
        driver.findElement(password).sendKeys(pass);
    }

    public void clickRememberMe() {
        driver.findElement(rememberCheckBox).click();
    }

    public void clickSubmitButton() {
        driver.findElement(submitButton).click();
    }

   
}

