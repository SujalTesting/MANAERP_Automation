package pom.pageobjectclasses.villas.projectmodule;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import dataModels.ProjectDataModal;
import dataModels.ProjectDataModal.PaymentScheduleOptions;

public class PaymentSchedule {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // =========================================================================
    // LOCATORS
    // =========================================================================
    
    /** Specific Locator for the Plot Card's "Add Schedule" button */
    private final By PLOT_SCHEDULE_TRIGGER = By.xpath("(//button[normalize-space(text())='Add Schedule'])[1]");
    
    /** Specific Locator for the Construction Card's "Add Schedule" button */
    private final By CONSTRUCTION_SCHEDULE_TRIGGER = By.xpath("(//button[normalize-space(text())='Add Schedule'])[2]");
    
    // Modal Elements (Used for both Edit and Add operations)
    private final By SCHEDULE_STAGE_NAME_FIELD = By.xpath("//input[@placeholder='Enter Payment Stage']"); // Text Input: Payment Stage
    
    // Locator for the Cost Type dropdown trigger inside the modal
    private final By STAGE_TYPE_DROPDOWN = By.xpath("//div[@role='dialog']//button[contains(., 'Select Cost Type')] | //div[@role='dialog']//button[@role='combobox']");
    
    private final By PERCENTAGE_FIELD = By.xpath("//input[@placeholder='Enter Amount/Percentage']"); 
    private final By DAY_OFFSET_FIELD = By.xpath("//input[@placeholder='Enter Timeline in Days']"); 
    private final By DESCRIPTION_FIELD = By.xpath("//input[@placeholder='Enter Description']"); 

    /** Locator for the Modal's 'Add/Save Schedule' button, matching user's implied context */
    private final By SAVE_SCHEDULE_BUTTON = By.xpath("//div[@role='dialog']//button[normalize-space(text())='Add Schedule' or normalize-space(text())='Save' or normalize-space(text())='Submit']");
    
    // Navigation and Edit Locators
    private final By NEXT_DOCUMENTS_BUTTON = By.xpath("//button[normalize-space()='Next: Access & Others']");
    private final By UPDATE_COST_TYPE_DROPDOWN = By.xpath("//button[contains(@data-testid,'select-') and contains(@data-testid,'-trigger')] | //div[@role='dialog']//button[@role='combobox']");
    private final By UPDATE_SCHEDULE_BUTTON = By.xpath("//button[contains(@data-testid,'btn-update-schedule')] | //button[normalize-space(text())='Update Schedule']");

    
    public PaymentSchedule(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(45)); 
        this.js = (JavascriptExecutor) driver;
        System.out.println("✅ Initialized ProjectPaymentSchedulePage.");
    }
    
    // =========================================================================
    // MASTER FLOW COORDINATOR (Unchanged)
    // =========================================================================
    
    /**
     * Main function to add, delete, and edit payment schedules for both Plot and Construction phases.
     */
    public void newAddSchedules(ProjectDataModal data) throws InterruptedException, RuntimeException, TimeoutException {
        
        // --- 1. PLOT PAYMENT SCHEDULE PHASE ---
        System.out.println("\n*** Starting PLOT Payment Schedule Setup ***");
        
        executeSchedulePhase(
            "Plot", 
            PLOT_SCHEDULE_TRIGGER, 
            data.getPlotAddScheduleVilla(), 
            data.getPlotScheduleOperationVilla()
        );

        // --- 2. CONSTRUCTION PAYMENT SCHEDULE PHASE ---
        System.out.println("\n*** Starting CONSTRUCTION Payment Schedule Setup ***");
        
        executeSchedulePhase(
            "Construction", 
            CONSTRUCTION_SCHEDULE_TRIGGER, 
            data.getConstructionAddScheduleVilla(), 
            data.getConstructionScheduleOperationVilla() 
        );
        
        // --- 3. FINAL NAVIGATION ---
        clickElement(NEXT_DOCUMENTS_BUTTON);
        System.out.println("➡️ Clicked 'Next: Access & Others' to move to the final step.");
    }
    
    /**
     * Executes the full Add-Delete-Edit cycle for a specific schedule type.
     */
    private void executeSchedulePhase(
        String scheduleType, 
        By modalTriggerLocator, 
        ProjectDataModal.AddScheduleDetails addData, 
        ProjectDataModal.ScheduleOperations ops) throws RuntimeException, TimeoutException {
        
        // 1. EXECUTE ADD 
        System.out.println("Executing ADD for " + scheduleType + " Schedule.");
        addNewSchedules(addData, modalTriggerLocator); 
        
        // 2. EXECUTE EDIT/DELETE
        System.out.println("Executing DELETE/EDIT operations for " + scheduleType + " Schedule.");
        addScheduleMasterActions(ops);
        
        System.out.println("*** Completed " + scheduleType + " Payment Schedule Setup ***");
    }

    // =========================================================================
    // ADD SCHEDULES METHOD (Functionality preserved)
    // =========================================================================
    
    /**
     * Adds new schedules. Requires the specific button locator to open the modal.
     * @param data The data containing the schedules to add.
     * @param modalTriggerLocator The specific locator (Plot or Construction button) to open the modal.
     */
    public void addNewSchedules(ProjectDataModal.AddScheduleDetails data, By modalTriggerLocator) {
        if (data == null || data.getCharges() == null || data.getCharges().isEmpty()) {
            System.out.println("⚠️ No additional charges provided in JSON. Skipping Add Charges.");
            return;
        }
        
        System.out.println("▶️ Starting to add " + data.getCharges().size() + " additional schedules...");
        ProjectDataModal.PaymentScheduleOptions lastCharge = null; 

        for (ProjectDataModal.PaymentScheduleOptions charge : data.getCharges()) {
            try {
                // 1. Click the specific 'Add Schedule' button. The clickElement helper now has a pre-click wait.
                clickElement(modalTriggerLocator); 
                
                // 2. Enter Stage Name (Wait for this field to appear to confirm the modal is open)
                WebElement stageNameField = wait.until(ExpectedConditions.presenceOfElementLocated(SCHEDULE_STAGE_NAME_FIELD));
                stageNameField.sendKeys(charge.getPaymentStage());
                
                // 3. Select Cost Type
                selectCustomDropdown(STAGE_TYPE_DROPDOWN, charge.getCostType());
                
                // 4. Enter Amount/Percentage
                WebElement percentageField = wait.until(ExpectedConditions.presenceOfElementLocated(PERCENTAGE_FIELD));
                percentageField.sendKeys(charge.getAmountPercentage());
                
                // 5. Enter Timeline in Days
                WebElement timeLineField = wait.until(ExpectedConditions.presenceOfElementLocated(DAY_OFFSET_FIELD));
                timeLineField.sendKeys(charge.getTimelineDays());
                
                // 6. Enter Description
                WebElement descriptionField = wait.until(ExpectedConditions.presenceOfElementLocated(DESCRIPTION_FIELD));
                descriptionField.sendKeys(charge.getDescription());
                
                // 7. Click Add Charge button in the modal
                clickElement(SAVE_SCHEDULE_BUTTON); 
                
                System.out.println("✅ Successfully added schedule: " + charge.getPaymentStage());
                
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } 
                
                lastCharge = charge;

            } catch (Exception e) {
                System.out.println("❌ Failed to add schedule for '" + charge.getPaymentStage() + "': Expected modal fields did not appear or timed out.");
                throw new RuntimeException("Failed to add schedule: " + charge.getPaymentStage(), e);
            }
        }
        System.out.println("✅ Completed adding all schedules.");

        // Robust scrolling logic (unchanged)
        if (lastCharge != null) {
            String lastChargeName = lastCharge.getPaymentStage();
            
            String actionButtonXPath = String.format(
                "//span[normalize-space(text())='%s']/ancestor::tr/td[last()]//button", lastChargeName
            );
            
            try {
                WebElement actionButton = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(actionButtonXPath))
                );
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({ block: 'center', inline: 'end' });", 
                    actionButton
                );
                
                System.out.println("➡️ Scrolled action button for '" + lastChargeName + "' into view using JavaScript.");
                try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            } catch (Exception e) {
                System.out.println("⚠️ Could not scroll action button for '" + lastChargeName + "' into view: " + e.getMessage());
            }
        }
    }

    // =========================================================================
    // EDIT AND DELETE OPERATIONS (Unchanged)
    // =========================================================================
    
    /**
     * Executes delete and edit operations using the provided data structure.
     */
    public void addScheduleMasterActions(ProjectDataModal.ScheduleOperations ops) throws RuntimeException, TimeoutException {
        
        if (ops == null) {
            System.out.println("⚠️ No schedule operations (Edit/Delete) specified in JSON data.");
            return;
        }

        if (ops.getChargeToDelete() != null) {
            String nameToDelete = ops.getChargeToDelete().getChargeName(); 
            String modalAction = ops.getChargeToDelete().getModalConfirmationAction(); 
            
            System.out.println("Executing DELETE operation for schedule: " + nameToDelete + " (Confirming with: " + modalAction + ")");
            
            deleteScheduleItemByStageName(nameToDelete, modalAction); 
        }

        if (ops.getChargeToEdit() != null && ops.getUpdatedChargeData() != null) {
            String nameToEdit = ops.getChargeToEdit();
            System.out.println("Executing EDIT operation for schedule: " + nameToEdit);
            
            editScheduleItemByStageName(nameToEdit, ops.getUpdatedChargeData());
        }
    }

    public void editScheduleItemByStageName(String chargeName, ProjectDataModal.PaymentScheduleOptions updatedChargeData) {
        
        By itemRowLocator = By.xpath(String.format("//span[normalize-space(text())='%s']/ancestor::tr", chargeName));

        if (driver.findElements(itemRowLocator).isEmpty()) {
            System.out.println("✅ Schedule item '" + chargeName + "' not present. Skipping edit operation.");
            return;
        }
            
        String actionButtonXPathTemplate = 
            "//span[normalize-space(text())='%s']/ancestor::tr//td[last()]//button"; 
        By actionMenuLocator = By.xpath(String.format(actionButtonXPathTemplate, chargeName));
        
        String editOptionXPath = "//*[normalize-space(text())='Edit' and not(self::td) and not(self::th)]"; 
        
        try {
            // 1. Open the Action Menu and click 'Edit'
            WebElement actionButton = wait.until(ExpectedConditions.presenceOfElementLocated(actionMenuLocator));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", actionButton);
            js.executeScript("arguments[0].click();", actionButton); 
            System.out.println("📝 Clicked action menu for schedule: " + chargeName);

            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // 2. Click the 'Edit' option
            WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(editOptionXPath)));
            js.executeScript("arguments[0].click();", editOption); 
            System.out.println("📝 Clicked 'Edit' option for schedule: " + chargeName);

            // ----------------------------------------------------------------------
            // Safely interact with fields in the edit modal
            // ----------------------------------------------------------------------
            
            // 2. Update Stage Name
            WebElement stageNameField = wait.until(ExpectedConditions.presenceOfElementLocated(SCHEDULE_STAGE_NAME_FIELD));
            stageNameField.clear(); 
            stageNameField.sendKeys(updatedChargeData.getPaymentStage());
            
            // 3. Update Cost Type
            selectCustomDropdown(UPDATE_COST_TYPE_DROPDOWN, updatedChargeData.getCostType());
            
            // 4. Update Amount/Percentage
            WebElement percentageField = wait.until(ExpectedConditions.presenceOfElementLocated(PERCENTAGE_FIELD));
            percentageField.clear(); 
            percentageField.sendKeys(updatedChargeData.getAmountPercentage());
            
            // 5. Update Timeline
            WebElement timeLineField = wait.until(ExpectedConditions.presenceOfElementLocated(DAY_OFFSET_FIELD));
            timeLineField.clear(); 
            timeLineField.sendKeys(updatedChargeData.getTimelineDays());
            
            // 6. Update Description
            WebElement descriptionField = wait.until(ExpectedConditions.presenceOfElementLocated(DESCRIPTION_FIELD));
            descriptionField.clear(); 
            descriptionField.sendKeys(updatedChargeData.getDescription());

            // 8 & 9. Submit the form and wait for the modal to close
            submitChargeEdit();
            
            System.out.println("✅ Schedule '" + chargeName + "' updated successfully.");
            
        } catch (Exception e) {
            System.out.println("❌ Error during EDIT operation for schedule '" + chargeName + "': " + e.getMessage());
            throw new RuntimeException("Failed to edit schedule: " + chargeName, e);
        }
    }
    
    private void submitChargeEdit() {
        WebElement updateButton = wait.until(ExpectedConditions.visibilityOfElementLocated(UPDATE_SCHEDULE_BUTTON));
        
        js.executeScript("arguments[0].click();", updateButton);
        System.out.println("Attempting to click Update Schedule button...");
        
        wait.until(ExpectedConditions.invisibilityOfElementLocated(UPDATE_SCHEDULE_BUTTON));
        System.out.println("Update Schedule button clicked Successfully...");
    }

    public void deleteScheduleItemByStageName(String chargeName, String modalAction) { 
        
        By itemRowLocator = By.xpath(String.format("//span[normalize-space(text())='%s']/ancestor::tr", chargeName));

        if (driver.findElements(itemRowLocator).isEmpty()) {
            System.out.println("✅ Schedule item '" + chargeName + "' already deleted (or not present). Skipping deletion.");
            return;
        }

        String actionButtonXPath = String.format(
            "//span[normalize-space(text())='%s']/ancestor::tr//td[last()]//button", chargeName
        );
        
        String deleteOptionXPath = "//*[normalize-space(text())='Delete' and not(self::td) and not(self::th)]"; 
        
        String confirmButtonXPath = String.format("//button[normalize-space(text())='%s']", modalAction);
        
        try {
            By actionButtonLocator = By.xpath(actionButtonXPath);
            
            // Step 1: Click the action button (Dropdown Trigger)
            WebElement actionButton = wait.until(ExpectedConditions.elementToBeClickable(actionButtonLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionButton);

            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Step 2: Click the 'Delete' option in the dropdown menu
            By deleteOptionLocator = By.xpath(deleteOptionXPath);
            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(deleteOptionLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteOption);

            System.out.println("📝 Clicked 'Delete' option for schedule: " + chargeName);

            // Step 3: Handle the confirmation modal (Click Delete or Cancel)
            By confirmButtonLocator = By.xpath(confirmButtonXPath); 
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(confirmButtonLocator));
            confirmButton.click();
            
            System.out.println("✅ Executed modal action: " + modalAction + " for schedule: " + chargeName);
            
            // Step 4: Wait for the item to disappear only if modalAction is "Delete"
            if (modalAction.equalsIgnoreCase("Delete")) {
                 wait.until(ExpectedConditions.invisibilityOfElementLocated(itemRowLocator));
                 System.out.println("✅ Schedule item '" + chargeName + "' successfully deleted.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error performing delete operation on schedule '" + chargeName + "': " + e.getMessage());
            throw new RuntimeException("Failed to perform delete operation on schedule: " + chargeName, e);
        }
    }
    
    // =========================================================================
    // HELPERS (Crucially modified to include a stability wait)
    // =========================================================================

    private void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        
        // ⭐ THE FIX: Introduce a small, explicit wait before the click action.
        // This ensures any previous UI transition (like a page load or scroll animation)
        // is fully complete, preventing the click from triggering and immediately closing the modal.
        try { 
            System.out.println("    [Helper] Pausing 500ms for UI stability before clicking: " + locator);
            Thread.sleep(500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        js.executeScript("arguments[0].click();", element);
    }

    private void selectCustomDropdown(By dropdown, String value) {
        try {
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdownButton);
            dropdownButton.click(); 

            // Combined locator for dropdown options
            By optionLocator = By.xpath("//div[@role='dialog']//*[contains(text(),'" + value + "')] | " +
                                        "//div[@role='presentation']//*[contains(text(),'" + value + "')] | " +
                                        "//div[contains(text(),'" + value + "')]");
            
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
            
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
            js.executeScript("arguments[0].click();", option);
            
            System.out.println("✅ Selected custom option: " + value);
            
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to select '" + value + "' from custom dropdown. Locator: " + dropdown.toString() + ". Error: " + e.getMessage(), e);
        }
    }
}
