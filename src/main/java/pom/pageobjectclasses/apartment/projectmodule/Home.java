package pom.pageobjectclasses.apartment.projectmodule;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Home {

    WebDriver driver;
    WebDriverWait wait;

    public Home(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void checkDashBoardURL() {
        try {
            // Wait for URL
            wait.until(ExpectedConditions.urlContains("/dashboard-home"));
            Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard-home"), "Dashboard URL mismatch");

            // Wait for page title
            wait.until(ExpectedConditions.titleIs("Dashboard Home | MANA-ERP"));
            Assert.assertEquals(driver.getTitle(), "Dashboard Home | MANA-ERP");

            // Admin QA Heading
            WebElement heading = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Admin QA')]")));
            Assert.assertTrue(heading.isDisplayed(), "Admin QA heading missing");

            // Logos
            WebElement manaLogo = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[contains(@src,'erpLogo-white.svg')]")));
            WebElement braincellLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'bg-white') and contains(@class,'rounded')]//img")));
            Assert.assertTrue(manaLogo.isDisplayed() && braincellLogo.isDisplayed(), "Logos missing");

            // Module Cards
            String[] modules = { "Project Module", "Sales Module", "Marketing Module", "HR Module", "CRM Module",
                    "Legal Module", "Finance Module", "Admin Module" };

            for (String mod : modules) {
                WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h3[normalize-space()='" + mod + "']")));
                Assert.assertTrue(card.isDisplayed(), mod + " card missing");

                if (mod.equals("Project Module")) {
                    card.click();
                    System.out.println("--- Clicked on " + mod + " ---");
                    break;
                }
            }

        } catch (Exception e) {
            Assert.fail("Dashboard check failed: " + e.getMessage());
        }
    }

    public void navigateToProjectCreation() {
        By addProjectButtonLocator = By.xpath("//button[normalize-space()='Add Project']");
        WebElement addProjectButton = wait.until(ExpectedConditions.elementToBeClickable(addProjectButtonLocator));
        addProjectButton.click();
    }
}
