package page;

import dataModels.ProjectDataModal;
import dataModels.ProjectDataModal.AreaConverter;
import dataModels.ProjectDataModal.ApprovalDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class ProjectSetupPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // ======================= LOCATORS =======================
    private final By projectTypeButton(String type) {
        return By.xpath("//span[normalize-space()='" + type + "']/ancestor::div[contains(@class, 'cursor-pointer')]");
    }
    private final By projectNameField = By.xpath("//input[@name='projectName']");
    private final By projectWebsiteURL = By.xpath("//input[@name='projectWebsiteurl']");
    private final By marketedBy = By.cssSelector("input[name='marketedby']");
    private final By projectExtentField = By.xpath("//input[@id='extent']");
    private final By projectExtentPlusIcon = By.xpath("//button[.//*[local-name()='svg' and @data-testid='AddIcon']]");
    
    // ⭐ FIX 1: Restoring robust locators for Area Converter Dropdowns
    private final By projectExtentDropdown1 = By.xpath("(//div[contains(@class, 'control')])[1]");
    private final By projectExtentInput1 = By.xpath("(//input[@id='extent'])[2]");
    private final By projectExtentDropdown2 = By.xpath("(//div[contains(@class, 'control')])[2]");
    private final By projectExtentInput2 = By.id("extentSecondary");
    private final By convertButtonProjectExtent = By.xpath("(//button[contains(text(),'Convert to Sqmt')])[1]");

    private final By saleableAreaField = By.xpath("//input[@id='area']");
    private final By saleableAreaPlusIcon = By.xpath("(//button[.//*[local-name()='svg' and @data-testid='AddIcon']])[2]");
    // ⭐ FIX 1: Restoring robust locators for Area Converter Dropdowns
    private final By saleableAreaDropdown1 = By.xpath("(//div[contains(@class, 'control')])[3]");
    private final By saleableAreaInput1 = By.xpath("(//input[@id='area'])[2]");
    private final By saleableAreaDropdown2 = By.xpath("(//div[contains(@class, 'control')])[4]");
    private final By saleableAreaInput2 = By.xpath("//input[@id='areaSecondary']");
    private final By convertButtonSaleableArea = By.xpath("//button[normalize-space()='Convert to Sqmt']");

 // APPROVAL BUTTONS / DROPDOWNS
    public final By planningApprovalButton = By.xpath("(//button[@role='combobox'])[1]");
    public final By reraApprovalButton = By.xpath("//button[.//span[normalize-space()='Select RERA approval status']]");
    
    // ⭐ Locator updated to target the placeholder text inside the button
    private final By planningAuthorityNameDropdown = By.xpath("//button/span[text()='Select Planning Authority']");

 // BANK DETAILS
    private final By bankDropdown = By.xpath("//button[.//span[contains(text(),'Select bank accounts')]]"); 

    // INPUT FIELDS
    public By planningApprovalNoField = By.name("bmrdaNo");
    public By reraNoField = By.name("hdmaNo");
    public By planningApprovalDateField = By.id("authorityStartDate");
    public By reraStartDateField = By.id("hdmaStartDate");
    public By reraEndDateField = By.id("hdmaEndDate");

    private By calendarMonthDropdown = By.xpath("//div[@role='dialog']//select[1]");
    private By calendarYearDropdown = By.xpath("//div[@role='dialog']//select[2]");
    
    public final By PINCODE_FIELD = By.name("pincode");
    public final By ADDRESS_FIELD = By.name("address");
    public final By CREATE_BUTTON = By.xpath("//button[@data-testid='btn-create' or text()='Create']");
    
 // Locators for Cost Sheet
    public By NEXT_COST_SETUP_BTN = By.xpath("//button[normalize-space(span)='Next: Cost Setup']");
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
    
    private final By UPDATE_CHARGE_BUTTON = By.cssSelector("[data-testid^='btn-update-charge-']"); // The new robust locator
    private final By EDIT_OPTION_IN_MENU = By.xpath("//*[normalize-space(text())='Edit' and not(self::td) and not(self::th)]");
    
    // Function to find the action menu button (three dots) by charge name
    private final Function<String, By> ACTION_MENU_BUTTON_BY_NAME = (name) -> 
        By.xpath(String.format("//td[normalize-space(text())='%s']/ancestor::tr//td[last()]//button", name));

    // Placeholders for modal fields (assuming standard HTML SELECT tags for the modal dropdowns)
//    private final By CHARGES_FOR_DROPDOWN = By.name("chargesFor");
//    private final By CATEGORY_DROPDOWN = By.name("category");
//    private final By COST_TYPE_DROPDOWN = By.name("costType");
//    private final By AMOUNT_FIELD = By.id("amount");
//    private final By TAX_DROPDOWN = By.name("tax"); 
    private final By ALL_CHARGE_NAME_CELLS = By.xpath("//table/tbody/tr/td[1]"); // Adjust based on your table structure
//    private final By ADD_CHARGES_BTN = By.xpath("//button[contains(text(), 'Add Charges')]");
//    private final By ADD_CHARGE_MODAL_BTN = By.xpath("//div[@role='dialog']//button[contains(text(), 'Add Charge')]");
    
    // ======================= CONSTRUCTOR =======================
    public ProjectSetupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
    }
    
    public WebDriver getDriver() {
        return this.driver;
    }

    // ======================= MASTER ACTION =======================
    public void fillEntireForm(ProjectDataModal data) throws InterruptedException, RuntimeException, TimeoutException {
    	enterProjectName(data.getBasicDetails().getProjectName());
        // 2️⃣ Immediately assert it
        String enteredName = driver.findElement(projectNameField).getAttribute("value").trim();
        if (!enteredName.equals(data.getBasicDetails().getProjectName())) {
            throw new RuntimeException("Project Name mismatch! Expected: " +
                    data.getBasicDetails().getProjectName() + ", Found: " + enteredName);
        }
    	selectProjectType(data.getBasicDetails().getProjectType());
        handleProjectExtent(data.getProjectExtent());
        handleSaleableArea(data.getSaleableArea());
        enterProjectWebsiteURL(data.getBasicDetails().getProjectUrl());
        enterMarketedBy(data.getBasicDetails().getMarketedBy());
        handleApprovals(data.getApprovals());
        handleBankSelection(data.getBankDetails());
        fillLocationThenCreate(data.getLocationDetails());
        fillCostSheet(data.getCostSheet());
        handleAddCharges(data.getAddCharges());
        handleChargeMasterActions(data);
    }
    
    public void handleChargeMasterActions(ProjectDataModal data) throws RuntimeException, TimeoutException {
        ProjectDataModal.ChargeOperations ops = data.getChargeOperations();

        if (ops == null) {
            System.out.println("⚠️ No charge operations (Edit/Delete) specified in JSON data.");
            return;
        }

        // --- DELETE OPERATION ---
        // ⚠️ Logic updated to use the new object structure
        if (ops.getChargeToDelete() != null) {
            
            // Extract data from the new object
            String nameToDelete = ops.getChargeToDelete().getChargeName(); 
            String modalAction = ops.getChargeToDelete().getModalConfirmationAction(); 
            
            System.out.println("Executing DELETE operation for charge: " + nameToDelete + " (Confirming with: " + modalAction + ")");
            
            // Call the updated method
            deleteChargeByName(nameToDelete, modalAction); 
        }

        // --- EDIT OPERATION ---
        if (ops.getChargeToEdit() != null && ops.getUpdatedChargeData() != null) {
            String nameToEdit = ops.getChargeToEdit();
            System.out.println("Executing EDIT operation for charge: " + nameToEdit);
            editChargeByName(nameToEdit, ops.getUpdatedChargeData());
        }
    }

    // ======================= PROJECT EXTENT (Example Structure to Follow) =======================
    public void handleProjectExtent(AreaConverter data) {
        if (data.getIsConverterUsed().equalsIgnoreCase("No")) {
            driver.findElement(projectExtentField).sendKeys(data.getSingleValue());
        } else {
            clickElement(projectExtentPlusIcon);
            wait.until(ExpectedConditions.visibilityOfElementLocated(projectExtentInput1));
            selectCustomDropdown(projectExtentDropdown1, data.getUnit1());
            driver.findElement(projectExtentInput1).sendKeys(data.getValue1());
            selectCustomDropdown(projectExtentDropdown2, data.getUnit2());
            driver.findElement(projectExtentInput2).sendKeys(data.getValue2());
            clickElement(convertButtonProjectExtent);
        }
    }

    // ======================= SALEABLE AREA =======================
    public void handleSaleableArea(AreaConverter data) {
        if (data.getIsConverterUsed().equalsIgnoreCase("No")) {
            driver.findElement(saleableAreaField).sendKeys(data.getSingleValue());
        } else {
            clickElement(saleableAreaPlusIcon);
            wait.until(ExpectedConditions.visibilityOfElementLocated(saleableAreaInput1));
            selectCustomDropdown(saleableAreaDropdown1, data.getUnit1());
            driver.findElement(saleableAreaInput1).sendKeys(data.getValue1());
            selectCustomDropdown(saleableAreaDropdown2, data.getUnit2());
            driver.findElement(saleableAreaInput2).sendKeys(data.getValue2());
            clickElement(convertButtonSaleableArea);
        }
    }
    
 // ======================= APPROVALS (Updated to mirror Saleable Area logic) =======================
    
    public void handleApprovals(ApprovalDetails data) {
        try {
            // ======== PLANNING APPROVAL ========
            handleYesNoDropdown(planningApprovalButton, data.getPlanningApprovalStatus());
            Thread.sleep(500); // allow dropdown to close

            selectPlanningAuthority(data.getPlanningAuthorityName());
            Thread.sleep(500); // allow UI update

            enterApprovalNumber(data.getApprovalNumber(), planningApprovalNoField);
            // selectDateInCalendar(planningApprovalDateField, data.getApprovalDate());

            // Small pause to ensure previous dropdown/overlay closed
            js.executeScript("document.body.click();"); // click outside to close any open dropdowns
            Thread.sleep(800);

            // ======== RERA APPROVAL ========
            handleYesNoDropdown(reraApprovalButton, data.getReraApprovalStatus());
            Thread.sleep(500);

            enterApprovalNumber(data.getReraNumber(), reraNoField);
            // selectDateInCalendar(reraStartDateField, data.getReraStartDate());
            // selectDateInCalendar(reraEndDateField, data.getReraEndDate());

            System.out.println("✅ Completed both Planning and RERA approvals successfully.");
        } catch (Exception e) {
            System.out.println("❌ Error in handleApprovals: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * Robust method to select Planning Authority from dropdown with up/down arrow scrolling
     */
    private void selectPlanningAuthority(String authorityName) {
        if (authorityName == null || authorityName.trim().isEmpty()) {
            System.out.println("No Planning Authority Name provided in JSON.");
            return;
        }

        try {
            // 1️⃣ Click the dropdown button to open options
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(planningAuthorityNameDropdown));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdown);
            js.executeScript("arguments[0].click();", dropdown);
            System.out.println("Clicked Planning Authority dropdown");

            // 2️⃣ Wait for options container to be visible
            By optionsContainer = By.xpath("//div[@role='listbox']"); // adjust if different
            wait.until(ExpectedConditions.visibilityOfElementLocated(optionsContainer));

            // 3️⃣ Loop through visible options and click the matching authority
            boolean found = false;
            while (!found) {
                List<WebElement> options = driver.findElements(By.xpath("//div[@role='option']"));
                for (WebElement option : options) {
                    if (option.getText().trim().equalsIgnoreCase(authorityName)) {
                        js.executeScript("arguments[0].scrollIntoView(true);", option);
                        js.executeScript("arguments[0].click();", option);
                        System.out.println("Selected Planning Authority: " + authorityName);
                        found = true;
                        break;
                    }
                }

                // Scroll if not found (optional: click the dropdown again to load more)
                if (!found) {
                    js.executeScript("arguments[0].scrollIntoView(true);", options.get(options.size() - 1));
                    Thread.sleep(300);
                }
            }

            Thread.sleep(500); // allow UI to settle

        } catch (Exception e) {
            System.out.println("Error selecting Planning Authority: " + e.getMessage());
        }
    }
    
    public void handleBankSelection(ProjectDataModal.BankDetails bankData) {
        try {
            if (bankData == null || bankData.getBanks() == null || bankData.getBanks().isEmpty()) {
                System.out.println("⚠️ No bank accounts provided in JSON.");
                return;
            }

            // 🔹 Take first bank only
            String bankName = bankData.getBanks().get(0).trim();
            System.out.println("⬇️ Selecting bank: " + bankName);

            // 🔹 Wait for dropdown and open
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(bankDropdown));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdown);
            js.executeScript("arguments[0].click();", dropdown);  // safer click via JS
            Thread.sleep(800); // wait for options to load

            // 🔹 Find and click the option safely
            By optionLocator = By.xpath("//div[@role='option']//span[normalize-space()='" + bankName + "']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
            js.executeScript("arguments[0].click();", option); // use JS click instead of normal click

            System.out.println("✅ Selected bank successfully: " + bankName);

        } catch (Exception e) {
            System.out.println("❌ Error selecting bank: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public List<String> getSelectedBanks() {
        List<String> selectedBanks = new ArrayList<>();
        List<WebElement> bankElements = driver.findElements(By.xpath("//div[@role='listbox']//div[@aria-selected='true']"));
        for (WebElement bank : bankElements) {
            selectedBanks.add(bank.getText().trim());
        }
        return selectedBanks;
    }

 // ======================= LOCATION DETAILS =======================
    public void fillLocationThenCreate(ProjectDataModal.LocationDetails data) {
        try {
            // Fill Pincode
            WebElement pincodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pincode")));
            pincodeField.clear();
            pincodeField.sendKeys(data.getPincode());

            // small wait for UI to process inputs
            Thread.sleep(5000);

            // Fill Address
            WebElement addressField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("address")));
            addressField.clear();
            addressField.sendKeys(data.getAddress());

            // Click Create button
            WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(CREATE_BUTTON));
            createBtn.click();
            System.out.println("✅ Pincode & Address filled, and Create button clicked successfully.");

            // Wait for success message
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Project added successfully')]")));
            System.out.println("🎉 " + successMsg.getText());

        } catch (Exception e) {
            System.out.println("❌ Error in filling LocationDetails: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to fill Base Price & GST
    public void fillCostSheet(ProjectDataModal.CostSheetDetails costSheet) {
        try {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(NEXT_COST_SETUP_BTN)).click();
        	
            wait.until(ExpectedConditions.visibilityOfElementLocated(BASE_PRICE_FIELD))
                .sendKeys(costSheet.getBasePricePerSqft());
            
            Thread.sleep(5000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(STANDARD_TAX_FIELD))
                .sendKeys(costSheet.getStandardTaxRate());

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
            
            // Wait for the confirmation modal to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(confirmButtonLocator));
            
        } catch (Exception e) {
            System.out.println("❌ Error performing delete operation on charge '" + chargeName + "': " + e.getMessage());
            throw new RuntimeException("Failed to perform delete operation on charge: " + chargeName, e);
        }
    }

    /**
     * Edits a charge in the table based on its current name and updates its values.
     */
    public void editChargeByName(String chargeName, ProjectDataModal.Charge updatedChargeData) throws TimeoutException {
        
        By actionMenuLocator = ACTION_MENU_BUTTON_BY_NAME.apply(chargeName);
        
        try {
            WebElement actionButton = wait.until(ExpectedConditions.presenceOfElementLocated(actionMenuLocator));
            
            // ⭐ FIX 1: Scroll the element into view and use JS Executor to click the action button
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", actionButton);
            wait.until(ExpectedConditions.elementToBeClickable(actionButton)); 
            
            try {
                actionButton.click();
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                System.out.println("⚠️ Click intercepted! Using JavaScript Executor to force click the action button.");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionButton);
            }

            // 🚨 CRITICAL PAUSE: Give time for the menu to fully render in the DOM (as done in delete method)
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // ⭐ FIX 2: Use JS Executor to click the 'Edit' option in the dropdown menu
            // Reusing the general locator structure for menu items for robustness
            String editOptionXPath = "//*[normalize-space(text())='Edit' and not(self::td) and not(self::th)]"; 
            By editOptionLocator = By.xpath(editOptionXPath);
            
            WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(editOptionLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editOption); // Force click
            
            System.out.println("📝 Clicked 'Edit' option for charge: " + chargeName);

            // Wait for the 'Edit Charge' modal to appear (using the update button visibility)
            wait.until(ExpectedConditions.visibilityOfElementLocated(UPDATE_CHARGE_BUTTON));

            // Helper function to handle Select dropdowns (Kept as per your original code)
            Function<By, Select> getSelectElement = (by) -> {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                return new Select(element);
            };

            // Update the fields in the modal
            if (updatedChargeData.getChargesFor() != null) {
                // Note: If this is a custom dropdown, use selectCustomDropdown, not Select.selectByVisibleText
                getSelectElement.apply(CHARGES_FOR_DROPDOWN).selectByVisibleText(updatedChargeData.getChargesFor());
            }

            if (updatedChargeData.getCategory() != null) {
                getSelectElement.apply(CATEGORY_DROPDOWN).selectByVisibleText(updatedChargeData.getCategory());
            }

            if (updatedChargeData.getCostType() != null) {
                getSelectElement.apply(COST_TYPE_DROPDOWN).selectByVisibleText(updatedChargeData.getCostType());
            }

            if (updatedChargeData.getAmount() != null) {
                WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(AMOUNT_FIELD));
                amountField.clear();
                amountField.sendKeys(updatedChargeData.getAmount());
            }

            if (updatedChargeData.getTax() != null) {
                // Ensure the tax value includes the '%' if required by the selectByVisibleText method
                String taxValue = updatedChargeData.getTax().trim().endsWith("%") ? updatedChargeData.getTax().trim() : updatedChargeData.getTax().trim() + "%";
                getSelectElement.apply(TAX_DROPDOWN).selectByVisibleText(taxValue);
            }
            
            // Click the 'Update Charge' button
            driver.findElement(UPDATE_CHARGE_BUTTON).click();
            
            // Wait for the modal to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(UPDATE_CHARGE_BUTTON));
            
            System.out.println("✅ Charge '" + chargeName + "' successfully edited.");

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
    
    

    private void enterApprovalNumber(String number, By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, number);
    }

    // ======================= DATE HANDLING =======================
    public String formatDateForUI(String fullDate) {
        try {
            String[] parts = fullDate.split(",\\s*");
            String year = parts[2].trim();
            String monthPart = parts[1].trim();
            String monthName = monthPart.split(" ")[0];
            String dayWithSuffix = monthPart.split(" ")[1].replaceAll("[a-zA-Z]", "");

            Map<String, String> monthMap = new HashMap<>();
            monthMap.put("January", "01"); monthMap.put("February", "02"); monthMap.put("March", "03");
            monthMap.put("April", "04"); monthMap.put("May", "05"); monthMap.put("June", "06");
            monthMap.put("July", "07"); monthMap.put("August", "08"); monthMap.put("September", "09");
            monthMap.put("October", "10"); monthMap.put("November", "11"); monthMap.put("December", "12");

            String month = monthMap.getOrDefault(monthName, "00");
            String day = dayWithSuffix.length() == 1 ? "0" + dayWithSuffix : dayWithSuffix;

            return String.format("%s/%s/%s", month, day, year);
        } catch (Exception e) {
            System.err.println("Error converting date: " + e.getMessage());
            return "00/00/0000";
        }
    }

    private void selectDateInCalendar(By dateFieldLocator, String fullDateAriaLabel) {
        clickElement(dateFieldLocator);

        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("January","0"); monthMap.put("February","1"); monthMap.put("March","2");
        monthMap.put("April","3"); monthMap.put("May","4"); monthMap.put("June","5");
        monthMap.put("July","6"); monthMap.put("August","7"); monthMap.put("September","8");
        monthMap.put("October","9"); monthMap.put("November","10"); monthMap.put("December","11");

        String[] parts = fullDateAriaLabel.split(",\\s*");
        String year = parts[2].trim();
        String monthName = parts[1].split(" ")[0];

        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(calendarYearDropdown))).selectByValue(year);
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(calendarMonthDropdown)))
                .selectByValue(monthMap.get(monthName));

        By dateElementLocator = By.xpath("//div[@aria-label='" + fullDateAriaLabel + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dateElementLocator)).click();
    }

    // ======================= GETTERS FOR ASSERTION =======================
    public String getProjectNameValue() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(driver -> {
                    WebElement el = driver.findElement(projectNameField); // <-- Fails here
                    if (el.isDisplayed()) {
                        return el.getAttribute("value").trim();
                    } else return null;
                });
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Project Name: " + e.getMessage(), e);
        }
    }

    public String getProjectExtentValue() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(projectExtentField));
        return element.getAttribute("value").trim();
    }

    public String getSaleableAreaValue() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(saleableAreaField));
        return element.getAttribute("value").trim();
    }

    public String getApprovalDropdownValue(String label) {
        By locator = label.equalsIgnoreCase("Planning Authority Approval") ? planningApprovalButton : reraApprovalButton;
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String text = dropdown.getText().trim();
        if (text.isEmpty()) {
            text = (String) js.executeScript("return arguments[0].innerText.trim();", dropdown);
        }
        return text;
    }

    public String getPlanningAuthorityNameValue() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(planningAuthorityNameDropdown));
        String text = dropdown.getText().trim();
        if (text.isEmpty()) {
            text = (String) js.executeScript("return arguments[0].innerText.trim();", dropdown);
        }
        return text;
    }

    public String getApprovalFieldText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String val = element.getAttribute("value");
        if (val == null || val.isEmpty()) {
            val = element.getText().trim();
        }
        return val.trim();
    }

    // ======================= BASIC FILLERS =======================
    public void selectProjectType(String type) { clickElement(projectTypeButton(type)); }
    public void enterProjectName(String name) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(projectNameField));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        element.sendKeys(name);
    }
    public void enterProjectWebsiteURL(String url) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(projectWebsiteURL));
        element.sendKeys(url);
    }
    public void enterMarketedBy(String marketedByText) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(marketedBy));
        element.sendKeys(marketedByText);
    }

}
