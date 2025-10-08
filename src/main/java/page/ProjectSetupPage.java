package page;

import base.ProjectDataModal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProjectSetupPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS ---

    // --- THIS IS THE FIX ---
    // The original locator was too broad. This one is more specific.
    // It looks for an element whose class is EXACTLY 'step-active' (ignoring whitespace)
    // AND contains the correct text. This ensures it finds only one element.
    private final By stepTitleLocator = By.xpath("//*[normalize-space(@class)='step-active' and contains(text(), 'Project Setup')]");
    
    // ... (rest of your locators remain the same)
    private final By projectTypeButton(String type) { return By.xpath("//div[contains(@class, 'cursor-pointer') and normalize-space()='" + type + "']"); }
    private final By projectNameField = By.xpath("//label[text()='Project Name *']/following-sibling::input");
    private final By projectExtentField = By.xpath("//input[@id='extent']");
    private final By saleableAreaField = By.xpath("//input[contains(@placeholder, 'Saleable Area')]");
    private final By projectUrlField = By.xpath("//input[@placeholder='https://example.com']");
    private final By marketedByField = By.xpath("//input[@placeholder='Enter marketer name']");
    private final By planningAuthorityDropdown = By.name("planningAuthorityDropdown");
    private final By planningApprovalYesNo(String value) { return By.xpath("//label[normalize-space()='Planning Authority Approval']/following-sibling::div//button[normalize-space()='" + value + "']"); }
    private final By approvalNumberField = By.name("approvalNumber");
    private final By approvalDateField = By.name("approvalDate"); 
    private final By reraRequiredYesNo(String value) { return By.xpath("//label[normalize-space()='RERA Compliance']/following-sibling::div//button[normalize-space()='" + value + "']"); }
    private final By reraNumberField = By.name("reraNumber");
    private final By selectBankAccountDropdown = By.xpath("//label[text()='Select bank accounts']/following-sibling::div//div[@class='dropdown-indicator']");
    private final By bankOption(String bankName) { return By.xpath("//div[normalize-space()='" + bankName + "']"); }
    private final By pincodeField = By.name("pincode"); 
    private final By cityField = By.name("City");
    private final By addressField = By.name("Address");
    private final By nextButton = By.xpath("//button[normalize-space()='Next: Project Units']");

    public ProjectSetupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // This line will now work correctly with the fixed locator
       // wait.until(ExpectedConditions.visibilityOfElementLocated(stepTitleLocator));
    }

    // ... (rest of your methods remain the same)
    public void fillEntireForm(ProjectDataModal data) {
        selectProjectType(data.getProjectType());
        fillProjectDetails(data.getProjectName(), data.getProjectExtent(), data.getSaleableArea(), data.getProjectUrl(), data.getMarketedBy());
        handlePlanningApproval(data.getPlanningApprovalRequired(), data.getPlanningAuthority(), data.getApprovalNumber());
        handleRERACompliance(data.getReraRequired(), data.getReraNumber());
        selectBankAccount(data.getBankAccount());
        fillPincodeAndVerifySync(data.getPincode(), data.getExpectedCity());
        fillAddressDetails(data.getAddressDetail());
    }
    
    public void selectProjectType(String type) {
        driver.findElement(projectTypeButton(type)).click();
    }

    public void fillProjectDetails(String name, String extent, String saleable, String url, String marketedBy) {
        driver.findElement(projectNameField).sendKeys(name);
        driver.findElement(projectExtentField).sendKeys(extent);
        driver.findElement(saleableAreaField).sendKeys(saleable);
        driver.findElement(projectUrlField).sendKeys(url);
        driver.findElement(marketedByField).sendKeys(marketedBy);
    }
    
    public void handleRERACompliance(String required, String reraNum) {
        driver.findElement(reraRequiredYesNo(required)).click();
        if (required.equalsIgnoreCase("Yes") && reraNum != null && !reraNum.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(reraNumberField));
            driver.findElement(reraNumberField).sendKeys(reraNum);
        }
    }
    
    public void handlePlanningApproval(String required, String authority, String number) {
        driver.findElement(planningApprovalYesNo(required)).click();
        if (required.equalsIgnoreCase("Yes")) {
            driver.findElement(approvalNumberField).sendKeys(number);
            driver.findElement(approvalDateField).sendKeys("01/01/2026");
        }
    }

    public void selectBankAccount(String bankName) {
        driver.findElement(selectBankAccountDropdown).click();
        wait.until(ExpectedConditions.elementToBeClickable(bankOption(bankName))).click();
    }

    public void fillAddressDetails(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    public void fillPincodeAndVerifySync(String pincode, String expectedCity) {
        driver.findElement(pincodeField).sendKeys(pincode);
        wait.until(ExpectedConditions.attributeToBe(cityField, "value", expectedCity));
    }
    
    public void clickNextStep() {
        driver.findElement(nextButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Project Units')]")));
    }
}

