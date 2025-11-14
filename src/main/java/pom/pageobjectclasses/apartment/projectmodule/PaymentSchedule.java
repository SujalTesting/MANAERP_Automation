package pom.pageobjectclasses.apartment.projectmodule;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

// Importing the outer ProjectDataModal class (assuming dataModels is the package)
import dataModels.ProjectDataModal;
// Inner classes are now accessed via dot notation (e.g., ProjectDataModal.PaymentScheduleItem)

// Renamed the class to ProjectPaymentSchedulePage for consistency
public class PaymentSchedule {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // =========================================================================
    // LOCATORS
    // =========================================================================
    
    // Table Elements
    private final By ALL_SCHEDULE_STAGE_CELLS = By.xpath("//table//tbody//tr/td[1]"); 
    // Using the button text for the main page Add Schedule button
    private final By ADD_SCHEDULE_ITEM_BUTTON = By.xpath("//button[normalize-space(text())='Add Schedule']");
    
    // Modal Elements (Used for both Edit and Add operations)
    private final By SCHEDULE_STAGE_NAME_FIELD = By.xpath("//input[@placeholder='Enter Payment Stage']"); // Text Input: Payment Stage
    private final By STAGE_TYPE_DROPDOWN = By.xpath("//button[@role='combobox' and .//span[normalize-space(text())='Select Cost Type']]");
    private final By PERCENTAGE_FIELD = By.xpath("//input[@placeholder='Enter Amount/Percentage']"); // Text Input: Amount/Percentage
     
    private final By DAY_OFFSET_FIELD = By.xpath("//input[@placeholder='Enter Timeline in Days']"); // Text Input: Timeline (Days)
    // Using index [2] to target the modal button if two buttons have the same text
    private final By SAVE_SCHEDULE_BUTTON = By.xpath("(//button[normalize-space(text())='Add Schedule'])[2]");
    private final By DESCRIPTION_FIELD = By.xpath("//input[@placeholder='Enter Description']"); 
    // Navigation Buttons
    private final By NEXT_DOCUMENTS_BUTTON = By.xpath("//button[normalize-space()='Next: Access & Others']");
    private final By Update_Cost_TYPE_DROPDOWN = By.xpath("//button[contains(@data-testid,'select-') and contains(@data-testid,'-trigger')]");
    private final By updateScheduleButton = By.xpath("//button[contains(@data-testid,'btn-update-schedule')]");

    
    // Constructor name matched to the new class name
    public PaymentSchedule(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 
        this.js = (JavascriptExecutor) driver;
        System.out.println("✅ Initialized ProjectPaymentSchedulePage.");
    }
    
    // =========================================================================
    // MASTER FLOW COORDINATOR
    // =========================================================================
    
    public void newAddSchedules(ProjectDataModal data) throws InterruptedException, RuntimeException, TimeoutException {
    	
        addNewSchedules(data.getAddScheduleApartmentOrPlot());
        addScheduleMasterActions(data);
    }
    
    public void addScheduleMasterActions(ProjectDataModal data) throws RuntimeException, TimeoutException {
        ProjectDataModal.ScheduleOperations ops = data.getScheduleOperationApartmentOrPlot();

//        if (ops == null) {
//            System.out.println("⚠️ No charge operations (Edit/Delete) specified in JSON data.");
//            return;
//        }

        // --- DELETE OPERATION (Runs first) ---
        if (ops.getChargeToDelete() != null) {
            String nameToDelete = ops.getChargeToDelete().getChargeName(); 
            String modalAction = ops.getChargeToDelete().getModalConfirmationAction(); 
            
            System.out.println("Executing DELETE operation for charge: " + nameToDelete + " (Confirming with: " + modalAction + ")");
            
            // This calls the working delete method
            deleteScheduleItemByStageName(nameToDelete, modalAction); 
        }

        // --- EDIT OPERATION (Runs second) ---
        if (ops.getChargeToEdit() != null && ops.getUpdatedChargeData() != null) {
            String nameToEdit = ops.getChargeToEdit();
            System.out.println("Executing EDIT operation for charge: " + nameToEdit);
            
            // This calls the NOW MODIFIED editChargeByName method
            editScheduleItemByStageName(nameToEdit, ops.getUpdatedChargeData());
        }
    }
    
    public void addNewSchedules(ProjectDataModal.AddScheduleDetails data) {
        if (data == null || data.getCharges() == null || data.getCharges().isEmpty()) {
            System.out.println("⚠️ No additional charges provided in JSON. Skipping Add Charges.");
            return;
        }

        System.out.println("▶️ Starting to add " + data.getCharges().size() + " additional charges...");
        ProjectDataModal.PaymentScheduleOptions lastCharge = null; // Variable to store the last charge added

        for (ProjectDataModal.PaymentScheduleOptions charge : data.getCharges()) {
            try {
                // 1. Click the main 'Add Charges' button to open the modal
                clickElement(ADD_SCHEDULE_ITEM_BUTTON);
                // Wait for a visible element in the modal to confirm it's open
                // wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_CHARGE_MODAL_BTN));
                
                // 2. Select Charges For
                WebElement stageNameField = wait.until(ExpectedConditions.presenceOfElementLocated(SCHEDULE_STAGE_NAME_FIELD));
                stageNameField.sendKeys(charge.getPaymentStage());
                
                // 3. Select Category
                selectCustomDropdown(STAGE_TYPE_DROPDOWN, charge.getCostType());
                
                // 4. Select Cost Type
                WebElement percentageField = wait.until(ExpectedConditions.presenceOfElementLocated(PERCENTAGE_FIELD));
                percentageField.sendKeys(charge.getAmountPercentage());
                
                // 5. Enter Amount
                WebElement timeLineField = wait.until(ExpectedConditions.presenceOfElementLocated(DAY_OFFSET_FIELD));
                timeLineField.sendKeys(charge.getTimelineDays());
                
                // 6. Select Tax Rate (Ensure it matches the UI format, e.g., "18%")
                WebElement descriptionField = wait.until(ExpectedConditions.presenceOfElementLocated(DESCRIPTION_FIELD));
                descriptionField.sendKeys(charge.getDescription());
                
                // 7. Click Add Charge button in the modal
                clickElement(SAVE_SCHEDULE_BUTTON);
                
                System.out.println("✅ Successfully added charge: " + charge.getPaymentStage());
                
                // Small wait for the row to be added to the table and modal to close
                // This wait is crucial for the DOM to update before we search for the element
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } 
                
                lastCharge = charge; // Update the last charge after successful addition

            } catch (Exception e) {
                System.out.println("❌ Failed to add charge for '" + charge.getPaymentStage() + "': " + e.getMessage());
                throw new RuntimeException("Failed to add charge: " + charge.getPaymentStage(), e);
            }
        }
        System.out.println("✅ Completed adding all charges.");

        // ⭐ NEW ROBUST SCROLLING: Scroll the action button of the last charge into view.
        if (lastCharge != null) {
            String lastChargeName = lastCharge.getPaymentStage();
            
            // --- THE FIX IS HERE ---
            // New XPath targets the text inside the <span> element for accuracy
            String actionButtonXPath = String.format(
                "//span[normalize-space(text())='%s']/ancestor::tr/td[last()]/button", lastChargeName
            );
            
            try {
                // Wait for the element to be VISIBLE (not just present) after the charges are added
                WebElement actionButton = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(actionButtonXPath))
                );
                
                // Use robust JavaScript scroll command with 'inline: end' for horizontal tables
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({ block: 'center', inline: 'end' });", 
                    actionButton
                );
                
                System.out.println("➡️ Scrolled action button for '" + lastChargeName + "' into view using JavaScript.");
                
                // A final small pause after scrolling for UI redraw
                Thread.sleep(500);

            } catch (Exception e) {
                System.out.println("⚠️ Could not scroll action button for '" + lastChargeName + "' into view: " + e.getMessage());
                // Fail softly here, the test will catch the failure in deleteChargeByName if it's still not visible.
            }
        }
    }

    /**
     * Edits an existing Payment Schedule item by finding its unique Stage Name in the table.
     * Removed internal scrolling logic as per request.
     */
//    public void editScheduleItemByStageName(String stageName, ProjectDataModal.PaymentScheduleItem updatedScheduleData) {

    
    private void submitChargeEdit() {
        // ⭐ FINAL FIX: Using visibilityOfElementLocated instead of elementToBeClickable.
        // This allows us to find the button even if it's covered by a UI overlay.
        WebElement updateButton = wait.until(ExpectedConditions.visibilityOfElementLocated(updateScheduleButton));
        
        // Force click using JavaScript to bypass any lingering UI overlaps.
        js.executeScript("arguments[0].click();", updateButton);
        System.out.println("Attempting to click Update Charge button...");
        
        // 10. Wait for the modal to close and disappear (This is the proof of success)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(updateScheduleButton));
        System.out.println("Update Charge button clicked Successfully...");
    }

    public void editScheduleItemByStageName(String chargeName, ProjectDataModal.PaymentScheduleOptions updatedChargeData) {
        
        // Locate the item's row based on its name, correctly targeting the <span>
        By itemRowLocator = By.xpath(String.format("//span[normalize-space(text())='%s']/ancestor::tr", chargeName));

        // --- CRITICAL FIX: Gracefully exit if the charge is not present ---
        if (driver.findElements(itemRowLocator).isEmpty()) {
            System.out.println("✅ Charge item '" + chargeName + "' not present. Skipping edit operation.");
            return;
        }
        // -------------------------------------------------------------------------
            
        // Construct the locator for the action menu (three dots).
        // ✅ FIX: Simplified XPath by removing unnecessary parentheses and the final [last()] index
        String actionButtonXPathTemplate = 
            "//span[normalize-space(text())='%s']/ancestor::tr//td[last()]//button"; 
        By actionMenuLocator = By.xpath(String.format(actionButtonXPathTemplate, chargeName));
        
        // Locator for the 'Edit' menu option
        String editOptionXPath = "//*[normalize-space(text())='Edit' and not(self::td) and not(self::th)]"; 
        
        try {
            // 1. Open the Action Menu and click 'Edit'
            WebElement actionButton = wait.until(ExpectedConditions.presenceOfElementLocated(actionMenuLocator));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", actionButton);
            js.executeScript("arguments[0].click();", actionButton); 
            System.out.println("📝 Clicked action menu for charge: " + chargeName);

            // Increased pause to ensure the popover menu is rendered
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // 2. Click the 'Edit' option
            WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(editOptionXPath)));
            js.executeScript("arguments[0].click();", editOption); 
            System.out.println("📝 Clicked 'Edit' option for charge: " + chargeName);

            // ----------------------------------------------------------------------
            // Safely interact with fields in the edit modal
            // ----------------------------------------------------------------------
            
         // 2. Update Stage Name
            WebElement stageNameField = wait.until(ExpectedConditions.presenceOfElementLocated(SCHEDULE_STAGE_NAME_FIELD));
            stageNameField.clear(); // Clear existing text before sending new keys
            stageNameField.sendKeys(updatedChargeData.getPaymentStage());
            
            // 3. Update Cost Type
            selectCustomDropdown(Update_Cost_TYPE_DROPDOWN, updatedChargeData.getCostType());
            
            // 4. Update Amount/Percentage
            WebElement percentageField = wait.until(ExpectedConditions.presenceOfElementLocated(PERCENTAGE_FIELD));
            percentageField.clear(); // Clear existing text
            percentageField.sendKeys(updatedChargeData.getAmountPercentage());
            
            // 5. Update Timeline
            WebElement timeLineField = wait.until(ExpectedConditions.presenceOfElementLocated(DAY_OFFSET_FIELD));
            timeLineField.clear(); // Clear existing text
            timeLineField.sendKeys(updatedChargeData.getTimelineDays());
            
            // 6. Update Description
            WebElement descriptionField = wait.until(ExpectedConditions.presenceOfElementLocated(DESCRIPTION_FIELD));
            descriptionField.clear(); // Clear existing text
            descriptionField.sendKeys(updatedChargeData.getDescription());

            // 8 & 9. Submit the form and wait for the modal to close
            submitChargeEdit();
            
            System.out.println("✅ Charge '" + chargeName + "' updated successfully.");
            
         // 10. Click the 'Next: Documents' button to proceed
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(NEXT_DOCUMENTS_BUTTON));
            js.executeScript("arguments[0].click();", nextButton); 
            System.out.println("➡️ Clicked 'Next: Access & Others' to move to the next step.");

        } catch (Exception e) {
            System.out.println("❌ Error during EDIT operation for charge '" + chargeName + "': " + e.getMessage());
            throw new RuntimeException("Failed to edit charge: " + chargeName, e);
        }
    }

    
//    /**
//     * Deletes a Payment Schedule item by finding its unique Stage Name in the table.
//     * Removed internal scrolling logic as per request.
//     */
    public void deleteScheduleItemByStageName(String chargeName, String modalAction) { // <-- UPDATED SIGNATURE
        
        // Locate the item's row based on its name
        By itemRowLocator = By.xpath(String.format("//span[normalize-space(text())='%s']/ancestor::tr", chargeName));

        // --- ⭐ CRITICAL FIX: Gracefully exit if the charge is not present ---
        if (driver.findElements(itemRowLocator).isEmpty()) {
            System.out.println("✅ Charge item '" + chargeName + "' already deleted (or not present). Skipping deletion.");
            return;
        }
        // ----------------------------------------------------------------------

        // Locator for the action button (three dots)
        String actionButtonXPath = String.format(
            "//span[normalize-space(text())='%s']/ancestor::tr//td[last()]//button", chargeName
        );
        
        // Locator for the 'Delete' option in the dropdown menu
        String deleteOptionXPath = "//*[normalize-space(text())='Delete' and not(self::td) and not(self::th)]"; 
        
        // ⭐ NEW: Specific locator for the confirmation modal button
        String confirmButtonXPath = String.format("//button[normalize-space(text())='%s']", modalAction);
        
        try {
            By actionButtonLocator = By.xpath(actionButtonXPath);
            
            // Step 1: Click the action button (Dropdown Trigger)
            WebElement actionButton = wait.until(ExpectedConditions.elementToBeClickable(actionButtonLocator));
            // Use JS executor for reliability
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionButton);

            // Pause for menu to fully render
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Step 2: Click the 'Delete' option in the dropdown menu
            By deleteOptionLocator = By.xpath(deleteOptionXPath);
            System.out.println("Attempting to locate Delete option with XPath: " + deleteOptionXPath);
            
            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(deleteOptionLocator));
            // Use JS click for reliability
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteOption);

            System.out.println("📝 Clicked 'Delete' option for charge: " + chargeName);

            // Step 3: Handle the confirmation modal (Click Delete or Cancel)
            By confirmButtonLocator = By.xpath(confirmButtonXPath); 
            System.out.println("Attempting to click modal button: " + modalAction);
            
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(confirmButtonLocator));
            confirmButton.click();
            
            System.out.println("✅ Executed modal action: " + modalAction + " for charge: " + chargeName);
            
            // Step 4: Wait for the item to disappear only if modalAction is "Delete"
            if (modalAction.equalsIgnoreCase("Delete")) {
                 wait.until(ExpectedConditions.invisibilityOfElementLocated(itemRowLocator));
                 System.out.println("✅ Charge item '" + chargeName + "' successfully deleted.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error performing delete operation on charge '" + chargeName + "': " + e.getMessage());
            throw new RuntimeException("Failed to perform delete operation on charge: " + chargeName, e);
        }
    }
    
    // =========================================================================
    // HELPERS
    // =========================================================================

    private void clickElement(By locator) {
        // The helper click still scrolls to center and clicks, ensuring all main buttons are reachable.
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Selects an option from a custom dropdown. 
     */
    private void selectCustomDropdown(By dropdown, String value) {
        try {
            // 1. Click the main dropdown button to open the options list/dialog
            // Using a standard click since the element is guaranteed to be clickable by the wait
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
            // Scroll the button itself into view
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdownButton);
            dropdownButton.click(); 

            // This combined locator (using contains(text())) is used for wide compatibility 
            // across unit converters and authority names.
            By optionLocator = By.xpath("//div[@role='dialog']//*[contains(text(),'" + value + "')] | " +
                                        "//div[@role='presentation']//*[contains(text(),'" + value + "')] | " +
                                        "//div[contains(text(),'" + value + "')]");
            
            // 2. Wait for the correct option to be visible and clickable
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
            
            // 3. SCROLL THE OPTION INTO VIEW (handles internal list scrolling) and click it.
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
            js.executeScript("arguments[0].click();", option);
            
            System.out.println("✅ Selected custom option: " + value);
            
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to select '" + value + "' from custom dropdown. Locator: " + dropdown.toString() + ". Error: " + e.getMessage(), e);
        }
    }
}
