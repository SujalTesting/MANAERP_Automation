package pom.pageobjectclasses.plots.projectmodule;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException; 
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import dataModels.ProjectDataModal; 

public class Home {

    WebDriver driver;
    // Set default wait to a higher value for general use
    WebDriverWait wait; 
    JavascriptExecutor js;
    Actions actions;

    public Home(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }

    // =========================================================================
    // LOCATORS
    // =========================================================================
    // Sidebar button 
    private final By sidebarProjectsButton = By.xpath("//button[normalize-space()='Projects']");
    
    // Project Type Dropdown locator (Kept as requested)
    private final By projectTypeDropdown = By.xpath("//button[.//span[normalize-space()='Project Type']]");
    
    // Dynamic Pagination Link
    private final String paginationButtonXpath = "//a[@data-slot='pagination-link'][normalize-space()='%d']";

    // View Project Button in Card 
    private final String viewProjectButtonXpath = "//h4[normalize-space(text())='%s']/ancestor::div[@data-slot='card']//button[normalize-space(text())='View Project']";


    // =========================================================================
    // HELPER METHOD (Master Action)
    // =========================================================================

    /**
     * Executes the custom dynamic dropdown selection logic used across the application.
     * Uses JavaScript click on both the dropdown and the option for maximum reliability.
     * @param dropdown The locator for the main dropdown button/input.
     * @param value The text value of the option to select.
     */
    private void selectCustomDropdown(By dropdown, String value) {
        try {
            System.out.println("Attempting to select custom dropdown value: " + value);
            
            // 1. Click the main dropdown button using JavaScript for reliability
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdownButton);
            js.executeScript("arguments[0].click();", dropdownButton); 
            
            // This combined locator handles options within different container roles
            By optionLocator = By.xpath("//div[@role='dialog']//*[contains(text(),'" + value + "')] | " +
                                        "//div[@role='presentation']//*[contains(text(),'" + value + "')] | " +
                                        "//div[contains(text(),'" + value + "')]");
            
            // 2. Wait for the correct option to be visible and clickable
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
            
            // 3. SCROLL the option into view and click it using JavaScript
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
            js.executeScript("arguments[0].click();", option);
            
            System.out.println("✅ Selected custom option: " + value);
            
            // Wait for the filter results to update (assuming a common loader exists)
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loader')]")));

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to select '" + value + "' from custom dropdown. Locator: " + dropdown.toString() + ". Error: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to click a specific pagination button.
     * @param pageNumber The page number to navigate to.
     */
    private void clickPagination(int pageNumber) {
        System.out.println("Navigating to Page Number: " + pageNumber);
        
        // Locate and click the specific page number button using the refined XPath
        By pageLocator = By.xpath(String.format(paginationButtonXpath, pageNumber));
        WebElement pageButton = wait.until(ExpectedConditions.elementToBeClickable(pageLocator));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", pageButton);
        pageButton.click();
        
        // Wait for the page content to load after pagination click
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loader')]")));
    }


    // =========================================================================
    // Dashboard Check 
    // =========================================================================

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

                // --- CODE BLOCK RESTORED AND LEFT AS IS PER USER REQUEST ---
                if (mod.equals("Project Module")) {
                    card.click();
                    System.out.println("--- Clicked on " + mod + " ---");
                    break;
                }
                // -----------------------------------------------------------
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


    // =========================================================================
    // VIEW EXISTING PROJECT (FIXED: ADDED SIDEBAR CLICK FOR ROBUSTNESS)
    // =========================================================================

    /**
     * Locates an existing project by filtering 
     * by type, handling pagination, and clicking 'View Project' using the card structure.
     * Clicks the sidebar 'Projects' button to ensure navigation is complete before filtering.
     * @param details Data model containing projectName, projectType, and pageNumber.
     */
    public void viewExistingProject(ProjectDataModal.ProjectSelectionDetails details) {
        WebDriverWait standardWait = new WebDriverWait(driver, Duration.ofSeconds(30)); 

        try {
            // FIX: Explicitly click the sidebar button to ensure we are on the Projects page
            // if the navigation from the dashboard card in the previous test failed to complete.
            System.out.println("Attempting to click sidebar Projects button to ensure correct page.");
            WebElement sidebarButton = standardWait.until(ExpectedConditions.elementToBeClickable(sidebarProjectsButton));
            js.executeScript("arguments[0].click();", sidebarButton); // Use JS click for reliability

            // 1. Apply Project Type Filter using the custom dropdown method
            if (details.getProjectType() != null && !details.getProjectType().isEmpty()) {
                selectCustomDropdown(projectTypeDropdown, details.getProjectType());
            } else {
                System.out.println("No Project Type filter provided, continuing without filtering.");
            }

            // 2. Handle Pagination
            if (details.getPageNumber() > 1) {
                clickPagination(details.getPageNumber());
            } else {
                 System.out.println("Assuming Page 1 selected or default page.");
            }

            // 3. Validate Project Name existence and locate the 'View Project' button
            String projectName = details.getProjectName();
            if (projectName == null || projectName.isEmpty()) {
                 Assert.fail("Project Name is mandatory in the data model for viewing an existing project.");
            }
            
            // Build the dynamic XPath
            By viewButtonLocator = By.xpath(String.format(viewProjectButtonXpath, projectName));
            
            // Wait for the button to be clickable, scroll it into view, and click it
            WebElement viewButton = standardWait.until(ExpectedConditions.elementToBeClickable(viewButtonLocator));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", viewButton);
            viewButton.click();
            
            System.out.println("Successfully clicked 'View Project' for: " + projectName);

        } catch (TimeoutException e) {
            // Provide a clear error message specifically for navigation timeout
            Assert.fail("Failed to navigate to Projects page or find project details after clicking the sidebar button. Current URL: " + driver.getCurrentUrl(), e);
        } catch (Exception e) {
            Assert.fail("Failed to view existing project '" + details.getProjectName() + "'. Error: " + e.getMessage());
        }
    }
}
