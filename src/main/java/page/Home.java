package page;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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
            // 1. Wait for and locate the element
            WebElement moduleCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[normalize-space()='" + mod + "']"))
                
            );

            // 2. Assert that the element is displayed
            Assert.assertTrue(moduleCard.isDisplayed(), mod + " card is not displayed!");

            // 3. NEW LOGIC: Check if this is the module we want to click
            if (mod.equals("Project Module")) {
                moduleCard.click();
                System.out.println("--- Successfully clicked on: " + mod + " ---");
                
                // Use 'break' to exit the loop immediately after clicking, 
                // as the goal is achieved.
                break; 
            }
        }

        
    }

    /**
     * Navigates from the home/dashboard page to the project creation screen
     * by clicking the 'Add Project' button.
     */
    public void navigateToProjectCreation() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Using elementToBeClickable is more robust for buttons as it checks for
        // both visibility and enabled state.
        // NOTE: If a unique ID or data-testid is available, it would be a more stable locator.
        By addProjectButtonLocator = By.xpath("//button[normalize-space()='Add Project']");
        
        WebElement addProjectButton = wait.until(ExpectedConditions.elementToBeClickable(addProjectButtonLocator));
        
        addProjectButton.click();
    }
}      

