package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ProjectUnitsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS ---
    private final By stepTitleLocator = By.xpath("//*[normalize-space(@class)='step-active' and contains(text(), 'Project Units')]");
    private final By addBlockButton = By.xpath("//button[normalize-space()='Add Block']");
    private final By blockNameField = By.xpath("//input[@placeholder='Enter Block Name']"); // Appears after clicking 'Add Block'
    private final By createFloorsButton = By.xpath("//button[normalize-space()='Create Floors']");
    
    // Locator to find all the created floor rows for verification
    private final By floorListLocator = By.xpath("//div[contains(@class, 'floor-row-class')]"); // NOTE: Replace with the actual class of a floor row

    public ProjectUnitsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // Critical Wait: Ensures we are on the correct page before proceeding
        wait.until(ExpectedConditions.visibilityOfElementLocated(stepTitleLocator));
    }

    // --- ACTIONS ---

    /**
     * Completes the entire flow for adding one block and its floors.
     * @param blockName The name of the block to be created (e.g., "Block A").
     */
    public void addBlockWithFloors(String blockName) {
        wait.until(ExpectedConditions.elementToBeClickable(addBlockButton)).click();
        
        // Wait for the input field to appear and then type the name
        WebElement blockNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(blockNameField));
        blockNameInput.sendKeys(blockName);
        
        driver.findElement(createFloorsButton).click();
    }

    /**
     * Verifies that the correct number of floors have been automatically created.
     * @param expectedCount The number of floors we expect to find.
     * @return The actual number of floors found on the page.
     */
    public int getActualFloorCount(int expectedCount) {
        // Wait until the expected number of floors appear on the page
        wait.until(ExpectedConditions.numberOfElementsToBe(floorListLocator, expectedCount));
        
        // Get the list of floor elements and return how many there are
        List<WebElement> floorElements = driver.findElements(floorListLocator);
        return floorElements.size();
    }
}
