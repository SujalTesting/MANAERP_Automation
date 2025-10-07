package page;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Home {

    WebDriver driver;

    public Home(WebDriver driver) {
        this.driver = driver;
    }

    public void checkDashBoardURL() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Wait for dashboard URL
        wait.until(ExpectedConditions.urlToBe("https://manaerp-qa.netlify.app/dashboard-home"));

        // Assert page URL
        String expectedUrl = "https://manaerp-qa.netlify.app/dashboard-home";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "Home URL verification after login failed");

        // Assert page title (wait for title)
        wait.until(ExpectedConditions.titleIs("Dashboard Home | MANA-ERP"));
        Assert.assertEquals(driver.getTitle(), "Dashboard Home | MANA-ERP");

        // Assert greeting message
        WebElement adminQaHeading = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Admin QA')]"))
        );
        Assert.assertTrue(adminQaHeading.isDisplayed(), "Admin QA heading is not displayed!");

        // Assert logos visible
        WebElement manaLogo = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[contains(@src,'erpLogo-white.svg')]"))
        );

        WebElement braincellLogo = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'bg-white') and contains(@class,'rounded')]//img"))
        );

        Assert.assertTrue(manaLogo.isDisplayed(), "MANA logo is not displayed!");
        Assert.assertTrue(braincellLogo.isDisplayed(), "Braincell logo is not displayed!");

        // Assert all module cards are displayed with wait
        String[] modules = {"Project Module", "Sales Module", "Marketing Module", "HR Module", "CRM Module", "Legal Module", "Finance Module", "Admin Module"};
        for (String mod : modules) {
            WebElement moduleCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[normalize-space()='" + mod + "']"))
            );
            Assert.assertTrue(moduleCard.isDisplayed(), mod + " card is not displayed!");
        }

        // Assert user profile icon displayed using wait (update selector if needed)
        WebElement profileIcon = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".profile-icon-selector"))
        );
        Assert.assertTrue(profileIcon.isDisplayed(), "User profile icon is not visible!");
    }
}      

