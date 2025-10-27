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

    // ======================= CONSTRUCTOR =======================
    public ProjectSetupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
    }

    // ======================= MASTER ACTION =======================
    public void fillEntireForm(ProjectDataModal data) {
        selectProjectType(data.getBasicDetails().getProjectType());
        enterProjectName(data.getBasicDetails().getProjectName());
        handleProjectExtent(data.getProjectExtent());
        handleSaleableArea(data.getSaleableArea());
        enterProjectWebsiteURL(data.getBasicDetails().getProjectUrl());
        enterMarketedBy(data.getBasicDetails().getMarketedBy());
        handleApprovals(data.getApprovals());
        handleBankSelection(data.getBankDetails());

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

            List<String> bankList = bankData.getBanks();

            for (int i = 0; i < bankList.size(); i++) {
                String bankName = bankList.get(i).trim();
                boolean found = false;

                // 🔹 Re-find dropdown each time to avoid stale element
                WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(bankDropdown));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", dropdown);
                wait.until(ExpectedConditions.elementToBeClickable(dropdown));
                js.executeScript("arguments[0].click();", dropdown);
                System.out.println("⬇️ Opened dropdown to select bank: " + bankName);

                // 🔹 Wait for dropdown options to appear
                By optionsContainer = By.xpath("//div[@role='listbox']");
                wait.until(ExpectedConditions.visibilityOfElementLocated(optionsContainer));

                // 🔹 Loop and scroll through options until bank is found
                for (int j = 0; j < 15; j++) {
                    List<WebElement> allOptions = driver.findElements(By.xpath("//div[@role='option']//span"));
                    for (WebElement option : allOptions) {
                        if (option.getText().trim().equalsIgnoreCase(bankName)) {
                            js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
                            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
                            System.out.println("✅ Selected bank: " + bankName);
                            found = true;
                            break;
                        }
                    }

                    if (found) break;

                    // Scroll dropdown down if bank not found yet
                    js.executeScript("arguments[0].scrollBy(0, 200);", driver.findElement(optionsContainer));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(optionsContainer));
                }

                if (!found) {
                    System.out.println("❌ Bank not found in dropdown: " + bankName);
                }

                // 🔹 Close dropdown safely
                js.executeScript("document.body.click();");

                // 🔹 Wait until dropdown is fully closed
                wait.until(ExpectedConditions.attributeToBe(dropdown, "aria-expanded", "false"));
            }

            System.out.println("✅ All banks selected successfully: " + bankList);

        } catch (Exception e) {
            System.out.println("❌ Error selecting banks: " + e.getMessage());
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
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(projectNameField));
        return element.getAttribute("value").trim();
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
