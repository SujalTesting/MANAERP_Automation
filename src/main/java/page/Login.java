package page;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login {

private static WebDriver driver;
    
    private By userEmail = By.id("email");
    private By password = By.id("password");
    private By rememberCheckBox = By.cssSelector("[id='remember']");
    private By submitButton = By.cssSelector("[type='submit']");

    public Login(WebDriver driver) {
        Login.driver = driver;
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

    public static String getCurrentURL() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));  // Wait up to 15 seconds
        wait.until(ExpectedConditions.urlToBe("https://redefineerp-develop.netlify.app/admin/home"));

        String actualUrl = driver.getCurrentUrl();
        System.out.println("Current URL after login: " + actualUrl);
        
        return actualUrl;
    }
}

