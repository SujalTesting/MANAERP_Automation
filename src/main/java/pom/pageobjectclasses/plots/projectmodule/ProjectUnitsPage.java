package pom.pageobjectclasses.plots.projectmodule;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import dataModels.ProjectDataModal.Block;
import dataModels.ProjectDataModal.Unit;


/**
 * Handles the Project Units creation step (Step 2 of the Project Module flow).
 */
public class ProjectUnitsPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js; 

    // --- LOCATORS ---
    private final By unitsPageTitle = By.xpath("(//div[normalize-space()='Project Units'])[2]");
    private final By phaseBtn = By.xpath("//button[normalize-space()='Add Phase']"); 
    private final By blockNameField = By.xpath("//input[@name='blockName']"); 
//    private final By expectedFloorCountField = By.xpath("//input[@name='expectedFloorCount']"); 
    private final By savePhaseBtn = By.cssSelector("button[type='submit']");
    private final By addUnitBtn = By.xpath("//button[./span[normalize-space(text())='Add Unit']]");
    private final By nextStepBtn = By.xpath("//button[normalize-space(text())='Next: Cost Setup']"); 

    // Add Unit Form Loactors (Inputs/Dropdowns in the modal)
    // Unit Details
    private final By plotNo = By.xpath("//input[@name='unit_no']"); // <--- CRITICAL WAIT TARGET
    private final By unitTypeDropdown = By.xpath("//*[normalize-space(text())='Select type']/ancestor::button[1]");
 // Common locator for floating dropdown options menu, used to wait for menu closure
    private final By dimensionField = By.xpath("//input[@name='dimension']");
    private final By facingDropdown = By.xpath("//*[normalize-space(text())='Select facing']/ancestor::button[1]");
    private final By areaSQFTField = By.xpath("//input[@name='area']");
    private final By priceSQFTField = By.xpath("//input[@name='sqft_rate']");
    private final By plcField = By.xpath("//input[@name='plc_per_sqft']");
    private final By minPriceField = By.xpath("//input[@name='min_rate_per_sqft']");

    // Dimension Details
    private final By eastSide = By.xpath("//input[@name='east_d']");
    private final By westSide = By.xpath("//input[@name='west_d']");
    private final By northSide = By.xpath("//input[@name='north_d']");
    private final By southSide = By.xpath("//input[@name='south_d']");
    
 // Status Details 
    private final By unitStatusDropdown = By.xpath("//*[normalize-space(text())='Select status']/ancestor::button[1]"); // Corrected locator
    private final By releaseStatusDropdown = By.xpath("//*[normalize-space(text())='Select release status']/ancestor::button[1]"); // Corrected locator
    private final By mortgageTypeDropdown = By.xpath("//*[normalize-space(text())='Select mortgage type']/ancestor::button[1]"); // Corrected locator
    private final By sharingtypeField = By.xpath("//input[@name='sharingType']");
    
    // Additional Details
    private final By surveyNoField = By.xpath("//input[@name='survey_no']");
    private final By kathaNoField = By.xpath("//input[@name='Katha_no']");
    private final By pidNoField = By.xpath("//input[@name='PID_no']");
    private final By inventoryTypeDropdown = By.xpath("//*[normalize-space(text())='Select inventory type']/ancestor::button[1]"); // Corrected locator


    // Button in the Add Unit Modal
    private final By saveUnitBtn = By.xpath("//button[normalize-space(text())='Add Unit']");
    private final By cancelModalBtn = By.xpath("//button[normalize-space(text())='Cancel']");
    
    //Import Units Process
    private final By moreActionIcon = By.xpath("(//button[.//*[local-name()='svg']])[13]");
    private final By importUnitsBtn = By.xpath("//*[normalize-space()='Import Units']");
    private final By selectFileBtn = By.xpath("//*[normalize-space()='Select Files']");
    

    public ProjectUnitsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(unitsPageTitle));
        System.out.println("✅ Confirmed arrival on Project Units Setup Page (Step 2).");
    }

    // =========================================================================
    // MASTER ACTION: The method called from the test script (Step 2 Orchestrator)
    // =========================================================================
    public void createAllBlocksAndUnits(List<Block> blocksToCreate) throws RuntimeException {
        if (blocksToCreate == null || blocksToCreate.isEmpty()) {
            System.out.println("⚠️ No blocks specified in JSON data. Skipping Project Units Step.");
            return;
        }
        
        System.out.println("▶️ Starting creation of " + blocksToCreate.size() + " block(s)...");

        clickElement(phaseBtn);
        System.out.println("☑️ Clicked 'Add Phase' button to begin block entry.");


        for (Block block : blocksToCreate) {
            createBlock(block);
        }

        try {
            clickElement(nextStepBtn);
            System.out.println("✅ Successfully moved to the Next Setup Step (Cost Setup).");
        } catch (Exception e) {
            System.out.println("⚠️ Could not find or click the 'Next: Cost Setup' button. Test continues.");
        }
    }
    
    // =========================================================================
    // PRIMARY ACTION: Create a single Block and its related Units
    // =========================================================================
    private void createBlock(Block block) throws RuntimeException {
        try {
            enterBlockName(block.getBlockName());

            clickElement(savePhaseBtn);
            System.out.println("✅ Saved Block/Phase: " + block.getBlockName());
            
            Thread.sleep(2000); 

            if (block.getUnits() != null) {
                for (Unit unit : block.getUnits()) {
                    addSingleUnit(unit);
                }
            } else {
                System.out.println("⚠️ Block '" + block.getBlockName() + "' has no units defined in JSON.");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to create Block or Units for: " + block.getBlockName() + ". Error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Handles the full workflow for adding a single unit via modal.
     */
    private void addSingleUnit(Unit unit) throws InterruptedException {
        WebElement clickAddunit = wait.until(ExpectedConditions.presenceOfElementLocated(addUnitBtn));
        clickAddunit.click();
        System.out.println("Opened Add Unit modal for unit: " + unit.getUnitNo());
        
        // ⭐ CRITICAL FIX: Wait explicitly for an element inside the modal to ensure it's loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(plotNo));
        
        fillUnitDetails(unit); 
        fillDimensionDetails(unit); 
        fillStatusDetails(unit);
        fillAdditionalDetails(unit);

        clickElement(saveUnitBtn);
        clickElement(cancelModalBtn);
        
//        //Import Units Process
//        clickElement(moreActionIcon);
//        clickElement(importUnitsBtn);
//        clickElement(selectFileBtn);
        System.out.println("✅ Saved Unit: " + unit.getUnitNo());
        
        // Wait for modal to close completely before the next iteration
        Thread.sleep(1500); 
    }
    
    // =========================================================================
    // DETAIL FILLER METHODS (Internal Helpers)
    // =========================================================================
    
    private void enterBlockName(String name) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(blockNameField));
        element.sendKeys(name);
    }

    private void fillUnitDetails(Unit unit) {
        wait.until(ExpectedConditions.presenceOfElementLocated(plotNo)).sendKeys(unit.getUnitNo());
        
        driver.findElement(unitTypeDropdown).click();
        WebElement selectTypeOption = driver.findElement(By.xpath("(//*[text()='Odd'])[2]"));
        selectTypeOption.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(dimensionField)).sendKeys(unit.getDimension());
        //driver.findElement(facingDropdown).click();
        driver.findElement(facingDropdown).sendKeys("East");
        
//        WebElement facingOption = driver.findElement(By.xpath("//*[normalize-space(text())='East']"));
//        js.executeScript("arguments[0].click();", facingOption);
        wait.until(ExpectedConditions.presenceOfElementLocated(areaSQFTField)).sendKeys(unit.getAreaSqft());
        wait.until(ExpectedConditions.presenceOfElementLocated(priceSQFTField)).sendKeys(unit.getPriceSqft());
        wait.until(ExpectedConditions.presenceOfElementLocated(plcField)).sendKeys(unit.getPlcPerSqft());
        wait.until(ExpectedConditions.presenceOfElementLocated(minPriceField)).sendKeys(unit.getMinRatePerSqft());
        System.out.println("  - Filled Unit Details.");
    }
    
    
    private void fillDimensionDetails(Unit unit) {
        wait.until(ExpectedConditions.presenceOfElementLocated(eastSide)).sendKeys(unit.getEastSide());
        wait.until(ExpectedConditions.presenceOfElementLocated(westSide)).sendKeys(unit.getWestSide());
        wait.until(ExpectedConditions.presenceOfElementLocated(northSide)).sendKeys(unit.getNorthSide());
        wait.until(ExpectedConditions.presenceOfElementLocated(southSide)).sendKeys(unit.getSouthSide());
        System.out.println("  - Filled Dimension Details.");
    }
    
    private void fillStatusDetails(Unit unit) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // --- Unit Status Dropdown ---
        WebElement unitStatusElement = driver.findElement(unitStatusDropdown);
        js.executeScript("arguments[0].scrollIntoView(true);", unitStatusElement);
        unitStatusElement.sendKeys("Available");
        // WebElement statusOption = driver.findElement(By.xpath("//*[normalize-space(text())='Available']"));
        // js.executeScript("arguments[0].click();", statusOption);

        // --- Release Status Dropdown ---
        WebElement releaseElement = driver.findElement(releaseStatusDropdown);
        js.executeScript("arguments[0].scrollIntoView(true);", releaseElement);
        releaseElement.sendKeys("Yes");
        // WebElement releaseOption = driver.findElement(By.xpath("//*[normalize-space(text())='Yes']"));
        // js.executeScript("arguments[0].click();", releaseOption);

        // --- Mortgage Type Dropdown ---
        WebElement mortgageElement = driver.findElement(mortgageTypeDropdown);
        js.executeScript("arguments[0].scrollIntoView(true);", mortgageElement);
        mortgageElement.sendKeys("Bank");
        // WebElement mortgageOption = driver.findElement(By.xpath("//*[normalize-space(text())='Bank']"));
        // js.executeScript("arguments[0].click();", mortgageOption);

        // --- Sharing Type Field ---
        WebElement sharingType = wait.until(ExpectedConditions.presenceOfElementLocated(sharingtypeField));
        sharingType.sendKeys(unit.getSharingType());

        System.out.println("  - Filled Status Details.");
    }


    private void fillAdditionalDetails(Unit unit) {
        wait.until(ExpectedConditions.presenceOfElementLocated(surveyNoField)).sendKeys(unit.getSurveyNo());
        wait.until(ExpectedConditions.presenceOfElementLocated(kathaNoField)).sendKeys(unit.getKathaNo());
        wait.until(ExpectedConditions.presenceOfElementLocated(pidNoField)).sendKeys(unit.getPidNo());

        WebElement inventoryTypeElement = driver.findElement(inventoryTypeDropdown);
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", inventoryTypeElement);
        inventoryTypeElement.sendKeys("MAA");;
//    	WebElement inventoryOption = driver.findElement(By.xpath("//*[normalize-space(text())='MAA']"));
//        js.executeScript("arguments[0].click();", inventoryOption);
//        WebElement addUnitElement = driver.findElement(saveUnitBtn);
//        js.executeScript("arguments[0].scrollIntoView({block:'center'});", addUnitElement);
//        addUnitElement.click();;
        
        
        System.out.println("  - Filled Additional Details.");
    }
    
    // =========================================================================
    // HELPER METHODS (Critical for Scrolling and Clicking)
    // =========================================================================
    
    private void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        js.executeScript("arguments[0].click();", element);
    }
    
}
