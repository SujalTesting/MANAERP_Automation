package pom.pageobjectclasses.plots.projectmodule;

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

// Renamed class for the Update Project workflow
public class UpdateProjectSetupPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // ======================= LOCATORS =======================

    private final By projectNameField = By.xpath("//input[@name='projectName']");
    private final By projectWebsiteURL = By.xpath("//input[@name='projectWebsiteurl']");
    private final By marketedBy = By.cssSelector("input[name='marketedby']");
    private final By projectExtentField = By.xpath("//input[@id='extent']");
    private final By projectExtentPlusIcon = By.xpath("//button[.//*[local-name()='svg' and @data-testid='AddIcon']]");
    
    private final By projectExtentDropdown1 = By.xpath("(//div[contains(@class, 'control')])[1]");
    private final By projectExtentInput1 = By.xpath("(//input[@id='extent'])[2]");
    private final By projectExtentDropdown2 = By.xpath("(//div[contains(@class, 'control')])[2]");
    private final By projectExtentInput2 = By.id("extentSecondary");
    private final By convertButtonProjectExtent = By.xpath("(//button[contains(text(),'Convert to Sqmt')])[1]");

    private final By saleableAreaField = By.xpath("//input[@id='area']");
    private final By saleableAreaPlusIcon = By.xpath("(//button[.//*[local-name()='svg' and @data-testid='AddIcon']])[2]");
    
    private final By saleableAreaDropdown1 = By.xpath("(//div[contains(@class, 'control')])[3]");
    private final By saleableAreaInput1 = By.xpath("(//input[@id='area'])[2]");
    private final By saleableAreaDropdown2 = By.xpath("(//div[contains(@class, 'control')])[4]");
    private final By saleableAreaInput2 = By.xpath("//input[@id='areaSecondary']");
    private final By convertButtonSaleableArea = By.xpath("//button[normalize-space()='Convert to Sqmt']");

    // APPROVAL BUTTONS / DROPDOWNS
    public final By planningApprovalButton = By.xpath("(//button[@role='combobox'])[1]");
    public final By reraApprovalButton = By.xpath("(//button[@role='combobox'])[4]");
    private final By planningAuthorityNameDropdown = By.xpath("//label[contains(.,'Authority')]/following::button[@role='combobox'][2]");

    // BANK DETAILS
    private final By bankDropdown = By.xpath("(//button[@role='combobox' and @data-slot='select-trigger'])[7]"); 

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
    // Locator targets both "Update" and "Create" buttons
    public final By CREATE_BUTTON = By.xpath("//button[text()='Update']");
    
    // ======================= CONSTRUCTOR =======================

    public UpdateProjectSetupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
    }
    
    // PUBLIC GETTER 
    public WebDriver getDriver() {
        return this.driver;
    }

    // ======================= CORE HELPER METHOD (FIXED FOR ROBUST CLEARING) =======================

    /**
     * Finds an element, uses Keys.CONTROL+A and DELETE 
     * for robust clearing (fixing the data accumulation issue), and then enters new text.
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

    // ======================= MASTER ACTION =======================

    public void fillEntireForm(ProjectDataModal data) throws InterruptedException, RuntimeException, TimeoutException {
    	enterProjectName(data.getBasicDetails().getProjectName());
        handleProjectExtent(data.getProjectExtent());
        handleSaleableArea(data.getSaleableArea());
        enterProjectWebsiteURL(data.getBasicDetails().getProjectUrl());
        enterMarketedBy(data.getBasicDetails().getMarketedBy());
        handleApprovals(data.getApprovals());
        handleBankSelection(data.getBankDetails());
        fillLocationThenUpdate(data.getLocationDetails());
    }
    
    // ======================= BASIC FILLERS =======================

    public void enterProjectName(String name) {
        clearAndEnterText(projectNameField, name);
    }

    public void enterProjectWebsiteURL(String url) {
        clearAndEnterText(projectWebsiteURL, url);
    }

    public void enterMarketedBy(String marketedByText) {
        clearAndEnterText(marketedBy, marketedByText);
    }

    // ======================= COMPLEX FIELD HANDLERS =======================

    public void handleProjectExtent(AreaConverter data) {
        if (data.getIsConverterUsed().equalsIgnoreCase("No")) {
            clearAndEnterText(projectExtentField, data.getSingleValue());
        } else {
            clickElement(projectExtentPlusIcon);
            wait.until(ExpectedConditions.visibilityOfElementLocated(projectExtentInput1));
            
            selectCustomDropdown(projectExtentDropdown1, data.getUnit1());
            clearAndEnterText(projectExtentInput1, data.getValue1()); 

            selectCustomDropdown(projectExtentDropdown2, data.getUnit2());
            clearAndEnterText(projectExtentInput2, data.getValue2());
            
            clickElement(convertButtonProjectExtent);
        }
    }

    public void handleSaleableArea(AreaConverter data) {
        if (data.getIsConverterUsed().equalsIgnoreCase("No")) {
            clearAndEnterText(saleableAreaField, data.getSingleValue());
        } else {
            clickElement(saleableAreaPlusIcon);
            wait.until(ExpectedConditions.visibilityOfElementLocated(saleableAreaInput1));
            
            selectCustomDropdown(saleableAreaDropdown1, data.getUnit1());
            clearAndEnterText(saleableAreaInput1, data.getValue1()); 

            selectCustomDropdown(saleableAreaDropdown2, data.getUnit2());
            clearAndEnterText(saleableAreaInput2, data.getValue2()); 
            
            clickElement(convertButtonSaleableArea);
        }
    }

    // ======================= APPROVALS =======================

    public void handleApprovals(ApprovalDetails data) {
        try {
            // ======== PLANNING APPROVAL ========
            handleYesNoDropdown(planningApprovalButton, data.getPlanningApprovalStatus());
            Thread.sleep(500); 

            selectPlanningAuthority(data.getPlanningAuthorityName());
            Thread.sleep(500); 

            enterApprovalNumber(data.getApprovalNumber(), planningApprovalNoField);

            js.executeScript("document.body.click();"); // click outside to close any open dropdowns
            Thread.sleep(800);

            // ======== RERA APPROVAL ========
            handleYesNoDropdown(reraApprovalButton, data.getReraApprovalStatus());
            Thread.sleep(500);

            enterApprovalNumber(data.getReraNumber(), reraNoField); 
            // Date handling logic omitted here as it's commented out in original

            System.out.println("✅ Completed both Planning and RERA approvals successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error in handleApprovals: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to handle approvals.", e);
        }
    }

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

            String bankName = bankData.getBanks().get(0).trim();
            System.out.println("⬇️ Selecting bank: " + bankName);

            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(bankDropdown));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdown);
            js.executeScript("arguments[0].click();", dropdown); 
            Thread.sleep(800); 

            By optionLocator = By.xpath("//div[@role='option']//span[normalize-space()='" + bankName + "']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
            js.executeScript("arguments[0].click();", option); 

            System.out.println("✅ Selected bank successfully: " + bankName);

        } catch (Exception e) {
            System.err.println("❌ Error selecting bank: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ======================= LOCATION DETAILS (Update Logic) =======================

    /**
     * Clears old data and re-enters, clicking the general update/create button.
     * FIX APPLIED: Changed updateBtn.click() to js.executeScript("arguments[0].click();", updateBtn);
     * to bypass the click interception issue.
     */
    public void fillLocationThenUpdate(ProjectDataModal.LocationDetails data) {
        try {
            // Fill Pincode, using robust clear and enter
            clearAndEnterText(PINCODE_FIELD, data.getPincode());

            Thread.sleep(5000); // wait for UI to process inputs (e.g., locality lookup)

            // Fill Address, using robust clear and enter
            clearAndEnterText(ADDRESS_FIELD, data.getAddress());

            // Click Update/Create button
            WebElement updateBtn = wait.until(ExpectedConditions.elementToBeClickable(CREATE_BUTTON));
            
            // Scroll the button into view before clicking (may help, but JS click is the main fix)
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", updateBtn);
            
            // **FIX HERE: Using JavaScript click to bypass the sticky header interception**
            js.executeScript("arguments[0].click();", updateBtn);

            System.out.println("✅ Pincode & Address filled, and Update button clicked successfully.");

            // Wait for success message 
            By successLocator = By.xpath("//div[contains(text(),'Project added successfully') or contains(text(),'Project updated successfully') or contains(text(),'Project saved successfully')]");
            // Wait for success message after the update/postback event
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(successLocator)); 
            System.out.println("🎉 " + successMsg.getText());

        } catch (Exception e) {
            System.err.println("❌ Error in filling LocationDetails: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to complete location details and update.", e);
        }
    }
    
    // ======================= OTHER LOCAL HELPERS (INTERNAL) =======================

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

    private void selectCustomDropdown(By dropdown, String value) {
        try {
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdownButton);
            dropdownButton.click(); 

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

    /**
     * Helper to enter approval number. Now uses the robust clearAndEnterText internally.
     */
    private void enterApprovalNumber(String number, By locator) {
        clearAndEnterText(locator, number);
    }

    // ======================= DATE HANDLING (Kept for completeness) =======================

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


    // ======================= GETTERS FOR ASSERTION (FIXED FOR STALENESS) =======================

    public List<String> getSelectedBanks() {
        List<String> selectedBanks = new ArrayList<>();
        // This locator assumes a specific structure; if it fails, adjust.
        List<WebElement> bankElements = driver.findElements(By.xpath("//div[@role='listbox']//div[@aria-selected='true']"));
        for (WebElement bank : bankElements) {
            selectedBanks.add(bank.getText().trim());
        }
        return selectedBanks;
    }
    
    /**
     * **FIX for StaleElementReferenceException:**
     * Robustly waits for the project name field to be visible after the update/re-render,
     * ensuring we get a fresh element reference before retrieving its value.
     */
    public String getProjectNameValue() {
        try {
            // Wait for the element to be visible/present again in the new DOM.
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(projectNameField));
            return element.getAttribute("value").trim();
        } catch (Exception e) {
            // Throw a clearer error, indicating failure even after attempting re-location
            throw new RuntimeException("Failed to re-locate and get Project Name value after update. Stale element likely still present.", e);
        }
    }

    public String getProjectExtentValue() {
        // Re-using the robust waiting pattern for consistency
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(projectExtentField));
        return element.getAttribute("value").trim();
    }

    public String getSaleableAreaValue() {
        // Re-using the robust waiting pattern for consistency
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

}