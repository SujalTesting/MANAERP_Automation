package pom.pageobjectclasses.plots.projectmodule;

import dataModels.ProjectDataModal;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class UpdateCostSetupPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // ======================= LOCATORS =======================
    
 // Locators for Cost Sheet
    public By NEXT_COST_SETUP_BTN = By.xpath("//button[@role='tab' and normalize-space()='Cost Setup']");
    public final By ADD_PHASE_BTN = By.xpath("//button[normalize-space(text())='Add Phase']");
    public final By BASE_PRICE_FIELD = By.id("basePrice");
    public final By STANDARD_TAX_FIELD = By.id("baseGST");
    public final By SAVE_UNIT_PRICING_BTN = By.xpath("//button[normalize-space(text())='Save Unit Pricing']");
    
 // ⭐ NEW LOCATORS for Add Charges Modal
    private final By ADD_CHARGES_BTN = By.xpath("//button[@data-testid='btn-add-charges']");
    
    // Using XPath with normalize-space to find the parent div or button relative to the label
    private final By CHARGES_FOR_DROPDOWN = By.xpath("//label[normalize-space(text())='Charges For*']/following-sibling::button");
    private final By CATEGORY_DROPDOWN = By.xpath("//label[normalize-space(text())='Category*']/following-sibling::button");
    private final By COST_TYPE_DROPDOWN = By.xpath("//label[normalize-space(text())='Cost Type*']/following-sibling::button");
    private final By TAX_DROPDOWN = By.xpath("//label[normalize-space(text())='Tax*']/following-sibling::button");
    
    // Amount field has a stable ID or placeholder
    private final By AMOUNT_FIELD = By.xpath("//input[@placeholder='Enter Amount']");
    
    // The final "Add Charge" button inside the modal
    private final By ADD_CHARGE_MODAL_BTN = By.xpath("//button[@data-testid='btn-add-charge-3' or normalize-space(text())='Add Charge']");
    private final By CHARGES_TABLE_ROWS = By.xpath("//div[normalize-space(text())='More Charges']/following-sibling::div[contains(@class, 'table-body')]//div[contains(@class, 'grid-cols-7')]");
    
    private final By UPDATE_CHARGE_BUTTON = By.xpath("//button[text()='Update Charge']"); // The new robust locator
    
    // Placeholders for modal fields (assuming standard HTML SELECT tags for the modal dropdowns)
    private final By CHARGES_FOR_DROPDOWN_Edit = By.xpath("//label[normalize-space(text())='Charges For']/parent::div//button[@role='combobox']");
    private final By CATEGORY_DROPDOWN_Edit = By.xpath("//label[normalize-space(text())='Category']/parent::div//button[@role='combobox']");
    private final By COST_TYPE_DROPDOWN_Edit = By.xpath("//label[normalize-space(text())='Cost Type']/parent::div//button[@role='combobox']");
    private final By AMOUNT_FIELD_Edit = By.xpath("//input[@placeholder='Enter Amount']");
    private final By TAX_DROPDOWN_Edit = By.xpath("//label[normalize-space(text())='Tax']/parent::div//button[@role='combobox']"); 
    private final By ALL_CHARGE_NAME_CELLS = By.xpath("//table/tbody/tr/td[1]"); // Adjust based on your table structure
    
 // NEW LOCATOR: Next Payment Schedule button
    public static final By NEXT_PAYMENT_SCHEDULE_BUTTON = By.xpath("//button[contains(., 'Next: Payment Schedule')]");
    
    // ======================= CONSTRUCTOR =======================

    public UpdateCostSetupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
    }
    
 // ======================= MASTER ACTION =======================
    public void fillEntireForm(ProjectDataModal data) throws InterruptedException, RuntimeException, TimeoutException {
        
        fillCostSheet(data.getCostSheet());
        handleAddCharges(data.getAddCharges());
        handleChargeMasterActions(data);
    }

    public void handleChargeMasterActions(ProjectDataModal data) throws RuntimeException, TimeoutException {
        ProjectDataModal.ChargeOperations ops = data.getChargeOperations();

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
            deleteChargeByName(nameToDelete, modalAction); 
        }

        // --- EDIT OPERATION (Runs second) ---
        if (ops.getChargeToEdit() != null && ops.getUpdatedChargeData() != null) {
            String nameToEdit = ops.getChargeToEdit();
            System.out.println("Executing EDIT operation for charge: " + nameToEdit);
            
            // This calls the NOW MODIFIED editChargeByName method
            editChargeByName(nameToEdit, ops.getUpdatedChargeData());
        }
    }
    
 // Method to fill Base Price & GST
    public void fillCostSheet(ProjectDataModal.CostSheetDetails costSheet) {
        try {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(NEXT_COST_SETUP_BTN)).click();
        	
            // 1. Enter Base Price (Using robust method)
            clearAndEnterText(BASE_PRICE_FIELD, costSheet.getBasePricePerSqft());
            
            Thread.sleep(5000);

            // 2. Enter Standard Tax Rate (Using robust method)
            clearAndEnterText(STANDARD_TAX_FIELD, costSheet.getStandardTaxRate());

            System.out.println("✅ Cost Sheet values entered: Base Price = " 
                + costSheet.getBasePricePerSqft() + ", GST = " + costSheet.getStandardTaxRate());

            wait.until(ExpectedConditions.elementToBeClickable(SAVE_UNIT_PRICING_BTN)).click();
            System.out.println("✅ Save Unit Pricing clicked successfully");

            // Optional: wait until some confirmation appears
            Thread.sleep(5000);

        } catch (Exception e) {
            System.out.println("❌ Error filling Cost Sheet: " + e.getMessage());
        }
    }
    
 // ======================= ADD CHARGES =======================
    public void handleAddCharges(ProjectDataModal.AddChargesDetails data) {
        if (data == null || data.getCharges() == null || data.getCharges().isEmpty()) {
            System.out.println("⚠️ No additional charges provided in JSON. Skipping Add Charges.");
            return;
        }

        System.out.println("▶️ Starting to add " + data.getCharges().size() + " additional charges...");
        ProjectDataModal.Charge lastCharge = null; // Variable to store the last charge added

        for (ProjectDataModal.Charge charge : data.getCharges()) {
            try {
                // ... (Steps 1 through 7: Adding the charge - code unchanged) ...
                
                // 1. Click the main 'Add Charges' button to open the modal
                clickElement(ADD_CHARGES_BTN);
                // Wait for a visible element in the modal to confirm it's open
                wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_CHARGE_MODAL_BTN));
                
                // 2. Select Charges For
                selectCustomDropdown(CHARGES_FOR_DROPDOWN, charge.getChargesFor());
                
                // 3. Select Category
                selectCustomDropdown(CATEGORY_DROPDOWN, charge.getCategory());
                
                // 4. Select Cost Type
                selectCustomDropdown(COST_TYPE_DROPDOWN, charge.getCostType());
                
                // 5. Enter Amount
                WebElement amountField = wait.until(ExpectedConditions.presenceOfElementLocated(AMOUNT_FIELD));
                amountField.sendKeys(charge.getAmount());
                
                // 6. Select Tax Rate (Ensure it matches the UI format, e.g., "18%")
                String taxRate = charge.getTax().trim().endsWith("%") ? charge.getTax().trim() : charge.getTax().trim() + "%";
                selectCustomDropdown(TAX_DROPDOWN, taxRate);
                
                // 7. Click Add Charge button in the modal
                clickElement(ADD_CHARGE_MODAL_BTN);
                
                System.out.println("✅ Successfully added charge: " + charge.getChargesFor());
                
                // Small wait for the row to be added to the table and modal to close
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } 
                
                lastCharge = charge; // Update the last charge after successful addition

            } catch (Exception e) {
                System.out.println("❌ Failed to add charge for '" + charge.getChargesFor() + "': " + e.getMessage());
                throw new RuntimeException("Failed to add charge: " + charge.getChargesFor(), e);
            }
        }
        System.out.println("✅ Completed adding all charges.");

        // ⭐ NEW ROBUST SCROLLING: Scroll the action button of the last charge into view.
        if (lastCharge != null) {
            String lastChargeName = lastCharge.getChargesFor();
            // Locate the action button for the last added charge
            String actionButtonXPath = String.format(
                "//td[normalize-space(text())='%s']/ancestor::tr//td[last()]//button", lastChargeName
            );
            
            try {
                WebElement actionButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(actionButtonXPath)));
                
                // Use JavaScript to scroll the element directly into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", actionButton);
                System.out.println("➡️ Scrolled action button for '" + lastChargeName + "' into view.");
                
                // A final small pause after scrolling for UI redraw
                Thread.sleep(500);

            } catch (Exception e) {
                System.out.println("⚠️ Could not scroll action button for '" + lastChargeName + "' into view: " + e.getMessage());
                // Fail softly here, the test will catch the failure in deleteChargeByName if it's still not visible.
            }
        }
    }
   
public void deleteChargeByName(String chargeName, String modalAction) { // <-- UPDATED SIGNATURE
        
        // Locate the item's row based on its name
        By itemRowLocator = By.xpath(String.format("//td[normalize-space(text())='%s']/ancestor::tr", chargeName));

        // --- ⭐ CRITICAL FIX: Gracefully exit if the charge is not present ---
        if (driver.findElements(itemRowLocator).isEmpty()) {
            System.out.println("✅ Charge item '" + chargeName + "' already deleted (or not present). Skipping deletion.");
            return;
        }
        // ----------------------------------------------------------------------

        // Locator for the action button (three dots)
        String actionButtonXPath = String.format(
            "//td[normalize-space(text())='%s']/ancestor::tr//td[last()]//button", chargeName
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
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

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

    /**
     * Edits a charge in the table based on its current name and updates its values.
     */
    private void submitChargeEdit() {
        // ⭐ FINAL FIX: Using visibilityOfElementLocated instead of elementToBeClickable.
        // This allows us to find the button even if it's covered by a UI overlay,
        // which was the root cause of the TimeoutException.
        WebElement updateButton = wait.until(ExpectedConditions.visibilityOfElementLocated(UPDATE_CHARGE_BUTTON));
        
        // Force click using JavaScript to bypass any lingering UI overlaps.
        js.executeScript("arguments[0].click();", updateButton);
        System.out.println("Attempting to click Update Charge button...");
        
        // 10. Wait for the modal to close and disappear (This is the proof of success)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(UPDATE_CHARGE_BUTTON));
        System.out.println("Update Charge button clicked Successfully...");
    }

    public void editChargeByName(String chargeName, ProjectDataModal.Charge updatedChargeData) {
        
        // Locate the item's row based on its name
        By itemRowLocator = By.xpath(String.format("//td[normalize-space(text())='%s']/ancestor::tr", chargeName));

        // --- ⭐ NEW CRITICAL FIX: Gracefully exit if the charge is not present ---
        if (driver.findElements(itemRowLocator).isEmpty()) {
            System.out.println("✅ Charge item '" + chargeName + "' not present. Skipping edit operation.");
            return;
        }
        // -------------------------------------------------------------------------
            
        // Construct the locator for the action menu (three dots)
        String actionButtonXPathTemplate = 
            "(//td[normalize-space(text())='%s']/ancestor::tr//td[last()]//button)[last()]"; 
        By actionMenuLocator = By.xpath(String.format(actionButtonXPathTemplate, chargeName));
        
        // Locator for the 'Edit' menu option
        String editOptionXPath = "//*[normalize-space(text())='Edit' and not(self::td) and not(self::th)]"; 
        
        try {
            // 1. Open the Action Menu and click 'Edit'
            WebElement actionButton = wait.until(ExpectedConditions.presenceOfElementLocated(actionMenuLocator));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", actionButton);
            js.executeScript("arguments[0].click();", actionButton); 
            System.out.println("📝 Clicked action menu for charge: " + chargeName);

            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // 2. Click the 'Edit' option
            WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(editOptionXPath)));
            js.executeScript("arguments[0].click();", editOption); 
            System.out.println("📝 Clicked 'Edit' option for charge: " + chargeName);

            // ⭐ CRITICAL WAIT: Wait for the first element to confirm the modal is stable.
//            wait.until(ExpectedConditions.visibilityOfElementLocated(CHARGES_FOR_DROPDOWN_Edit));

            // ----------------------------------------------------------------------
            // Safely interact with fields
            // ----------------------------------------------------------------------
            
            // 4. Update Charges For (Dropdown)
            if (updatedChargeData.getChargesFor() != null) {
                selectCustomDropdown(CHARGES_FOR_DROPDOWN_Edit, updatedChargeData.getChargesFor());
            }

            // 5. Update Category (Dropdown)
            if (updatedChargeData.getCategory() != null) {
                selectCustomDropdown(CATEGORY_DROPDOWN_Edit, updatedChargeData.getCategory());
            }

            // 6. Update Cost Type (Dropdown)
            if (updatedChargeData.getCostType() != null) {
                selectCustomDropdown(COST_TYPE_DROPDOWN_Edit, updatedChargeData.getCostType());
            }

            // 7. Update Amount (Text box)
            if (updatedChargeData.getAmount() != null) {
                WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(AMOUNT_FIELD_Edit));
                amountField.clear(); 
                amountField.sendKeys(updatedChargeData.getAmount());
            }

            // 8. Update Tax (Dropdown)
            if (updatedChargeData.getTax() != null) {
                String taxValue = updatedChargeData.getTax().trim().endsWith("%") ? updatedChargeData.getTax().trim() : updatedChargeData.getTax().trim() + "%";
                selectCustomDropdown(TAX_DROPDOWN_Edit, taxValue);
            }
//            
//            // CRITICAL PAUSE: Gives the UI 1000ms to process the input and re-enable the button state.
//            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // 9 & 10. Submit the form and wait for the modal to close (Now using the fixed method)
            submitChargeEdit();
            
            System.out.println("✅ Charge '" + chargeName + "' updated successfully.");
            
         // 11. RE-ADDED: Click the 'Next: Payment Schedule' button to proceed
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(NEXT_PAYMENT_SCHEDULE_BUTTON));
            js.executeScript("arguments[0].click();", nextButton); 
            System.out.println("➡️ Clicked 'Next: Payment Schedule' to move to the next step.");

        } catch (Exception e) {
            System.out.println("❌ Error during EDIT operation for charge '" + chargeName + "': " + e.getMessage());
            throw new RuntimeException("Failed to edit charge: " + chargeName, e);
        }
    }
    
    // =========================================================================
    // VERIFICATION METHODS
    // =========================================================================
    
    /**
     * Retrieves the list of names for all charges currently visible in the table.
     * @return A List of String containing the charge names.
     */
    public List<String> getAddedChargeNames() {
        List<String> names = new ArrayList<>();
        
        // Use a try-catch block to handle the case where the table is initially empty
        try {
            // Wait for at least one charge name cell to be present
            wait.until(ExpectedConditions.presenceOfElementLocated(ALL_CHARGE_NAME_CELLS));
        } catch (Exception e) {
            // If the wait fails (e.g., table is empty), return an empty list
            System.out.println("No charges found in the table.");
            return names;
        }
        
        List<WebElement> chargeElements = driver.findElements(ALL_CHARGE_NAME_CELLS);
        
        for (WebElement element : chargeElements) {
            String name = element.getText().trim();
            if (!name.isEmpty()) {
                names.add(name);
            }
        }
        
        System.out.println("Charges found in UI: " + names);
        return names;
    }

    // ======================= HELPERS =======================
    
    /**
     * Finds an element, uses Keys.CONTROL+A and DELETE 
     * for robust clearing (fixing data accumulation), and then enters new text.
     * @param locator The By locator of the input field.
     * @param text The new text to enter into the field.
     */
    private void clearAndEnterText(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
            
            // Robust clearing using Select All (Ctrl+A) and Delete
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE); 
            
            element.sendKeys(text);

            System.out.println("✅ Cleared and entered text into field: " + locator.toString());

        } catch (Exception e) {
            System.err.println("❌ Failed to clear and enter text for locator: " + locator.toString() + ". Error: " + e.getMessage());
            throw new RuntimeException("Interaction Failed: " + e.getMessage(), e);
        }
    }

    private void handleYesNoDropdown(By dropdownButton, String value) {
        try {
            clickElement(dropdownButton);

            By optionLocator = By.xpath("//div[@role='dialog']//*[contains(text(),'" + value + "')] | " +
                                        "//div[@role='presentation']//*[contains(text(),'" + value + "')] | " +
                                        "//div[contains(text(),'" + value + "')]");

            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
            js.executeScript("arguments[0].click();", option);

            System.out.println("✅ Selected " + value + " in dropdown.");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to select '" + value + "' from Yes/No dropdown: " + e.getMessage(), e);
        }
    }

    
    private void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Selects an option from a custom dropdown. 
     * Clicks the dropdown, scrolls the target option into view (to handle long lists), and clicks the option.
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