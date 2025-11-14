package pom.pageobjectclasses.villas.projectmodule;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dataModels.ProjectDataModal; 
import dataModels.ProjectDataModal.DocumentDetails; 
import dataModels.ProjectDataModal.AccessDetails; 
import dataModels.ProjectDataModal.ProjectSettingsDetails; 

public class AccessAndOthersPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // --- Locators ---
    private final String DOC_CARD_XPATH_TEMPLATE = "//div[normalize-space(text())='%s']"; 
    
    // LOCATOR: Targets the hidden file input within the visible upload area.
    private final By UPLOAD_INPUT_LOCATOR = By.xpath("//div[contains(@class, 'border-dashed') and .//span[contains(text(),'Click to upload')]]//input[@type='file']");
    
    // XPATH to locate the row for a successfully uploaded file based on its displayed name
    private final String FILE_ROW_XPATH_TEMPLATE = "//p[contains(text(), '%s')]/ancestor::div[contains(@class,'justify-between')]";
    
    // STABLE WAIT ELEMENT: This element is used to confirm the modal has successfully opened.
    private final By MAIN_MODAL_CLOSE_BUTTON = By.xpath("//button[@data-slot='dialog-close']"); 

    private final By DELETE_CONFIRMATION_BUTTON = By.xpath("//button[normalize-space()='Delete file']");

    private final By VIEW_BUTTON_LOCATOR = By.xpath(".//button[@title='View file']");
    private final By DOWNLOAD_BUTTON_LOCATOR = By.xpath(".//button[@title='Download file']");
    private final By DELETE_BUTTON_LOCATOR = By.xpath(".//button[@title='Delete file']");

    // --- New Access Locators ---
    private final By SEARCH_INPUT_LOCATOR = By.xpath("//input[contains(@data-testid, 'input-search-by-name-role-or-email')]");
    private final String CHECKBOX_BY_NAME_TEMPLATE = "//span[normalize-space(text())='%s']/ancestor::div[contains(@class,'flex items-center space-x-3')]//button[@role='checkbox']";
    private final By USER_LIST_CONTAINER = By.xpath("//div[contains(@class, 'max-h-[350px] overflow-y-auto')]");
    private final By NO_EXECUTIVES_LOCATOR = By.xpath("//div[contains(@class, 'max-h-[350px]')]//div[normalize-space()='No data found.']"); 
    private final By ANY_USER_ROW_LOCATOR = By.xpath("//div[contains(@class, 'max-h-[350px] overflow-y-auto')]//div[contains(@class, 'justify-between')]");
    
    // --- Project Settings Locators ---
    private final By PAST_DATE_TOGGLE = By.id("allowPastDateBooking");
    private final By MAX_BLOCK_DAYS_INPUT = By.id("maxBlockDays");
    private final By SAVE_BUTTON = By.xpath("//button[normalize-space()='Save']");
    private final By AUDIT_BUTTON = By.xpath("//button[normalize-space()='Audit']");
    private final By DELETE_PROJECT_BUTTON = By.xpath("//button[normalize-space()='Delete Project']");
    private final By CONFIRM_DELETE_BUTTON = By.xpath("//button[normalize-space(text())='Delete project']");
    private final By CANCEL_BUTTON = By.xpath("//button[contains(normalize-space(.), 'Cancel')]");


    // ----- Constructor ------
    public AccessAndOthersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Helper method to retrieve the correct List<String> of file paths 
     * based on the card name being processed.
     */
    private List<String> getFilePathsForCard(String cardName, ProjectDataModal projectDataModal) {
        DocumentDetails details = null;
        
        switch (cardName) {
            case "Project Approvals":
                details = projectDataModal.getProjectApprovalsDetails();
                break;
            case "Marketing Docs":
                details = projectDataModal.getMarketingDocsDetails();
                break;
            case "Legal Docs":
                details = projectDataModal.getLegalDocsDetails();
                break;
            case "Other Docs":
                // Assuming "Other Docs" is the correct key if it exists in your data model
                details = projectDataModal.getOtherDocsDetails(); 
                break;
            default:
                System.err.println("⚠️ Unknown document card name encountered: " + cardName);
                return null;
        }
        
        return details != null ? details.getDocsToUpload() : null;
    }
   
    
    /**
     * Helper method to retrieve the list of users to grant access for a specific card.
     */
    private List<String> getUsersToGrantAccess(String cardName, ProjectDataModal projectDataModal) {
        AccessDetails details = null;
        
        switch (cardName) {
            case "Sales Access":
                details = projectDataModal.getSalesAccessDetails();
                break;
            case "Credit Note Issuers":
                details = projectDataModal.getCreditNoteIssuersDetails();
                break;
            case "Accounts Access":
                details = projectDataModal.getAccountsAccessDetails();
                break;
            case "Marketing Access":
                details = projectDataModal.getMarketingAccessDetails();
                break;
            case "CRM Access":
                details = projectDataModal.getCrmAccessDetails();
                break;
            case "Legal Access":
                details = projectDataModal.getLegalAccessDetails();
                break;
            default:
                System.err.println("⚠️ Unknown access card name encountered: " + cardName);
                return null;
        }
        
        return details != null ? details.getUsersToGrantAccess() : null;
    }

    /**
     * Master method to manage all four document card types in sequence.
     * @param projectDataModal The complete data model containing the file paths.
     */
    public void manageAllDocumentCards(ProjectDataModal projectDataModal) {
        
        String[] docCards = {"Project Approvals", "Marketing Docs", "Legal Docs", "Other Docs"};
        
        try {
            for (String cardName : docCards) {
                System.out.println("\n===== Starting Document Management for: " + cardName + " =====");
                manageProjectDocuments(cardName, projectDataModal);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error in managing Project Documents flow: " + e.getMessage());
            throw new RuntimeException("Failed to complete Access & Others page flow.", e); 
        }
    }
    
    /**
     * Master method to manage all six user access cards in sequence.
     * @param projectDataModal The complete data model containing the user names.
     */
    public void manageAllAccessCards(ProjectDataModal projectDataModal) {
        
        String[] accessCards = {
            "Sales Access", "Credit Note Issuers", "Accounts Access", 
            "Marketing Access", "CRM Access", "Legal Access"
        };
        
        try {
            for (String cardName : accessCards) {
                System.out.println("\n===== Starting User Access Management for: " + cardName + " =====");
                grantUserAccess(cardName, projectDataModal);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error in managing User Access flow: " + e.getMessage());
            throw new RuntimeException("Failed to complete Access & Others page flow.", e); 
        }
    }


    /**
     * Reusable core method to upload, manage actions, and close the modal for a single card type.
     */
    private void manageProjectDocuments(String cardName, ProjectDataModal projectDataModal) throws Exception {
        
        List<String> filePaths = getFilePathsForCard(cardName, projectDataModal); 
        
        try {
            // 1. Click the specific Document Card
            By cardLocator = By.xpath(String.format(DOC_CARD_XPATH_TEMPLATE, cardName));
            clickElement(cardLocator, cardName + " Card");
            
            // Wait for the stable close button to appear, confirming the modal has opened.
            wait.until(ExpectedConditions.presenceOfElementLocated(MAIN_MODAL_CLOSE_BUTTON));
            System.out.println("✅ Document modal opened successfully.");
            
            // 2. Upload files and get the name of the last successfully uploaded file
            String lastSuccessfulFileName = uploadMultipleDocuments(filePaths); 

            // 3. Perform actions on the last successful uploaded file 
            if (lastSuccessfulFileName != null) {
                Thread.sleep(2000); 
                performDocumentActions(lastSuccessfulFileName);
            } else {
                System.out.println("⚠️ No files were successfully uploaded for " + cardName + ". Skipping actions.");
            }
                
            // 4. Close the modal
            clickElement(MAIN_MODAL_CLOSE_BUTTON, cardName + " Modal Close Button");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to manage documents for " + cardName + ". Error: " + e.getMessage());
            throw e; 
        }
    }

    /**
     * Uploads multiple documents and returns the filename of the last successfully uploaded file.
     */
    private String uploadMultipleDocuments(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            System.out.println("✅ No documents specified for upload. Skipping upload operation.");
            return null;
        }

        System.out.println("▶️ Starting document upload...");
        
        By uploadLocator = UPLOAD_INPUT_LOCATOR; 
        String lastSuccessfulFileName = null;
        
        for (String filePath : filePaths) {
            String fileName = new File(filePath).getName();
            try {
                WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(uploadLocator));
                
                js.executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible'; arguments[0].style.height='1px'; arguments[0].style.width='1px';", fileInput);
                
                fileInput.sendKeys(filePath);
                
                js.executeScript("arguments[0].style.display='none'; arguments[0].style.visibility='hidden';", fileInput);

                Thread.sleep(500); 

                By fileRowLocator = By.xpath(String.format(FILE_ROW_XPATH_TEMPLATE, fileName));
                wait.until(ExpectedConditions.presenceOfElementLocated(fileRowLocator));
                System.out.println("✅ Successfully uploaded file: " + fileName);
                
                lastSuccessfulFileName = fileName;
                
            } catch (Exception e) {
                System.err.println("❌ Failed to upload document: " + filePath + ". File Name: " + fileName + ". Error: " + e.getMessage());
                if (e.getMessage().contains("File not found")) {
                    System.err.println("⚠️ The file path appears inaccessible. Check your ProjectDataModal for correct paths.");
                } else if (lastSuccessfulFileName == null) {
                    throw new RuntimeException("Critical: First file upload failed for an application error.", e);
                }
            }
        }
        return lastSuccessfulFileName;
    }
    
    
    private void performDocumentActions(String fileName) throws Exception {
        System.out.println("\n▶️ Performing View, Download, and Delete actions on last successful file: " + fileName);

        By rowLocator = By.xpath(String.format(FILE_ROW_XPATH_TEMPLATE, fileName));
        
        // --- 1. View File (Eye Icon) ---
        WebElement fileRow = wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", fileRow);
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(fileRow, VIEW_BUTTON_LOCATOR));
        
        WebElement viewButton = fileRow.findElement(VIEW_BUTTON_LOCATOR); 
        js.executeScript("arguments[0].click();", viewButton);
        System.out.println("📝 Clicked View button. Waiting for new window/tab to open...");
        
        String currentWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Set<String> allWindowHandles = driver.getWindowHandles();
        String newWindow = allWindowHandles.stream().filter(h -> !h.equals(currentWindow)).findFirst().orElse(null);
        
        if (newWindow != null) {
            driver.switchTo().window(newWindow);
            System.out.println("⏸️ Staying on View page for 5 seconds..."); // Wait set to 5 seconds
            Thread.sleep(5000); // Wait set to 5 seconds
            driver.close();
            driver.switchTo().window(currentWindow);
            System.out.println("✅ View tab closed, returned to main page.");
        } else {
             System.out.println("⚠️ New window for View action not found, continuing...");
        }

        // --- 2. Download File (Download Icon) ---
        System.out.println("⏸️ Waiting 5 seconds before initiating Download...");
        Thread.sleep(5000); 

        fileRow = wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator)); 
        WebElement downloadButton = fileRow.findElement(DOWNLOAD_BUTTON_LOCATOR); 
        js.executeScript("arguments[0].click();", downloadButton);
        System.out.println("✅ Clicked Download button. File download initiated.");

        // --- 3. Delete File (X Icon) ---
        System.out.println("⏸️ Waiting 5 seconds before initiating Delete...");
        Thread.sleep(5000); 

        fileRow = wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator)); 
        WebElement deleteIcon = fileRow.findElement(DELETE_BUTTON_LOCATOR); 
        js.executeScript("arguments[0].click();", deleteIcon);
        System.out.println("📝 Clicked Delete icon. Waiting for confirmation modal...");

        clickElement(DELETE_CONFIRMATION_BUTTON, "Delete Confirmation Button"); 
        
        wait.until(ExpectedConditions.invisibilityOfElementLocated(rowLocator));
        System.out.println("✅ File '" + fileName + "' successfully deleted.");
    }
    
    /**
     * Reusable core method to open the modal, search for users, grant access, and close Module access management
     */
    private void grantUserAccess(String cardName, ProjectDataModal projectDataModal) throws Exception {
        
        List<String> userNames = getUsersToGrantAccess(cardName, projectDataModal); 
        
        try {
            // 1. Click the specific Access Card
            By cardLocator = By.xpath(String.format(DOC_CARD_XPATH_TEMPLATE, cardName));
            clickElement(cardLocator, cardName + " Card");
            
            // Wait for the stable close button to appear, confirming the modal has opened.
            wait.until(ExpectedConditions.presenceOfElementLocated(MAIN_MODAL_CLOSE_BUTTON));
            System.out.println("✅ Access modal opened successfully.");
            
            // 2. Grant access to users
            if (userNames != null && !userNames.isEmpty()) {
                for (String userName : userNames) {
                    searchAndSelectUser(userName, cardName);
                }
            } else {
                System.out.println("✅ No users specified to grant access for " + cardName + ".");
            }
                
            // 3. Close the modal
            clickElement(MAIN_MODAL_CLOSE_BUTTON, cardName + " Modal Close Button");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to manage access for " + cardName + ". Error: " + e.getMessage());
            throw e; 
        }
    }
    
    /**
     * Searches for a user, waits for the name to be displayed, pauses for 5 seconds for visual confirmation,
     * and then clicks the checkbox.
     */
    private void searchAndSelectUser(String userName, String cardName) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5)); 
        
        try {
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_INPUT_LOCATOR));
            searchInput.clear();
            searchInput.sendKeys(userName);
            System.out.println("📝 Searching for executive: " + userName);
            
            System.out.println("⏸️ Waiting up to 5 seconds for executive name to be displayed...");
            By checkboxLocator = By.xpath(String.format(CHECKBOX_BY_NAME_TEMPLATE, userName));
            WebElement checkbox = shortWait.until(ExpectedConditions.presenceOfElementLocated(checkboxLocator));

            System.out.println("⏸️ Name displayed. Pausing 5 seconds for visual confirmation before clicking checkbox.");
            Thread.sleep(5000); 

            js.executeScript("arguments[0].click();", checkbox);
            System.out.println("✅ Granted access to: " + userName + " in " + cardName + ".");
            
            Thread.sleep(1000); 
            
            searchInput.clear();

        } catch (Exception e) {
            System.err.println("❌ Executive '" + userName + "' not found within 5 seconds for " + cardName + ". Skipping this executive.");
            try {
                WebElement searchInput = driver.findElement(SEARCH_INPUT_LOCATOR);
                searchInput.clear();
            } catch (Exception clearException) {
                // Ignore failure to clear
            }
        }
    }

    // --- Project Settings Action Methods ---
    
    /**
     * Master method to apply project settings and handle final actions,
     * following the sequence: Toggle -> Input -> Save -> Audit -> Delete/Cancel.
     */
    public void configureProjectSettings(ProjectDataModal projectDataModal) {
        
        ProjectSettingsDetails details = projectDataModal.getProjectSettingsDetails();
        
        try {
            System.out.println("\n===== Starting Project Settings Configuration (Sequence: Toggle -> Input -> Save -> Audit -> Delete/Cancel) =====");

            // 1. Configure the toggle switch (Past Date Booking) + 5s pause
            configurePastDateBooking(details.isAllowPastDateBooking());
            
            // 2. Configure Max Block Days + 5s pause
            configureMaxBlockDays(details.getMaxBlockDays());

            // 3. Click Save + 5s pause (as requested)
            clickSaveButton();
            System.out.println("⏸️ Pausing 5 seconds after Save action.");
            Thread.sleep(5000); 

            // 4. Click Audit
            clickAuditButton();
            System.out.println("⏸️ Pausing 5 seconds after Audit action."); // ADDED WAIT
            Thread.sleep(5000); // ADDED WAIT
            
            // 5. Determine and execute the final action (Delete or Cancel)
            String action = details.getDeleteAction();
            
            // Check for delete action first (case-insensitive check for "delete" or "Delete project")
            if (action != null && ("Delete project".equalsIgnoreCase(action) || "delete".equalsIgnoreCase(action))) {
                // DELETE PATH
                performProjectDeletion();
            } else if (action != null && "cancel delete".equalsIgnoreCase(action)) {
                 // CANCEL PATH
                 performProjectDeletionAndCancel();
            } else {
                System.out.println("✅ Project Settings saved and audited. No final 'Delete Project' action was specified.");
            }
            
            System.out.println("✅ Project Settings configuration flow complete.");

        } catch (Exception e) {
            System.err.println("❌ Error in Project Settings configuration flow: " + e.getMessage());
            throw new RuntimeException("Failed to complete Project Settings page flow.", e); 
        }
    }
    
    /**
     * Toggles the "Allow Past Date Booking" switch and adds a 5-second pause.
     */
    private void configurePastDateBooking(boolean desiredState) throws InterruptedException {
        try {
            WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(PAST_DATE_TOGGLE));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", toggle);
            
            boolean currentState = Boolean.parseBoolean(toggle.getAttribute("aria-checked"));

            if (currentState != desiredState) {
                toggle.click();
                System.out.println("📝 Toggled 'Allow Past Date Booking' to: " + (desiredState ? "ON" : "OFF") + ".");
            } else {
                System.out.println("✅ 'Allow Past Date Booking' already at desired state: " + (desiredState ? "ON" : "OFF") + ".");
            }
            
            System.out.println("⏸️ Pausing 5 seconds to observe 'Allow Past Date Booking' state.");
            Thread.sleep(5000); 

        } catch (Exception e) {
            System.err.println("❌ Failed to configure 'Allow Past Date Booking' toggle. Error: " + e.getMessage());
            throw new RuntimeException("Toggle configuration failed.", e);
        }
    }

    /**
     * Enters the value into the Max Block Days input field and adds a 5-second pause.
     */
    private void configureMaxBlockDays(String blockDays) throws InterruptedException {
        if (blockDays == null || blockDays.trim().isEmpty()) {
            System.out.println("⚠️ Max Block Days is not specified in data model. Skipping input.");
            return;
        }
        
        try {
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(MAX_BLOCK_DAYS_INPUT));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", input);
            input.clear();
            input.sendKeys(blockDays);
            System.out.println("📝 Set Max Block Days to: " + blockDays + ".");
            
            System.out.println("⏸️ Pausing 5 seconds to observe Max Block Days input.");
            Thread.sleep(5000); 

        } catch (Exception e) {
            System.err.println("❌ Failed to set Max Block Days to " + blockDays + ". Error: " + e.getMessage());
            throw new RuntimeException("Max Block Days input failed.", e);
        }
    }

    /**
     * Clicks the Audit button.
     */
    public void clickAuditButton() {
        System.out.println("\n--- Performing Audit Action ---");
        clickElement(AUDIT_BUTTON, "Audit Button");
        System.out.println("✅ Audit button clicked successfully. Audit Log modal/page should now be visible.");
    }
    
    /**
     * Clicks the Save button.
     */
    public void clickSaveButton() {
        System.out.println("\n--- Performing Save Action ---");
        clickElement(SAVE_BUTTON, "Save Button");
        System.out.println("✅ Save button clicked successfully. Settings updated.");
    }
    

    /**
     * Initiates the project deletion process by clicking the initial Delete Project button and confirming.
     */
    private void performProjectDeletion() {
        System.out.println("\n--- Initiating Project Deletion (Danger Zone) ---");
        try {
            // 1. Click the Delete Project button to open the confirmation modal
            clickElement(DELETE_PROJECT_BUTTON, "Delete Project Button");
            
            System.out.println("📝 Delete Confirmation modal opened.");
            
            // 2. Automatically confirm the deletion 
            confirmProjectDeletion();
            System.out.println("🎉 Project deletion flow completed successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to click the initial 'Delete Project' button. Error: " + e.getMessage());
            throw new RuntimeException("Project Deletion initiation failed.", e);
        }
    }
    
    /**
     * Initiates the project deletion process and then cancels.
     */
    private void performProjectDeletionAndCancel() {
        System.out.println("\n--- Initiating Project Deletion, then Cancelling ---");
        try {
            // 1. Click the Delete Project button to open the confirmation modal
            clickElement(DELETE_PROJECT_BUTTON, "Delete Project Button");
            
            System.out.println("📝 Delete Confirmation modal opened.");
            
            // 2. Cancel the deletion 
            cancelProjectDeletion();
            System.out.println("✅ Project deletion successfully cancelled.");
        } catch (Exception e) {
            System.err.println("❌ Failed to initiate/cancel 'Delete Project' flow. Error: " + e.getMessage());
            throw new RuntimeException("Project Deletion (Cancel) flow failed.", e);
        }
    }

    /**
     * Confirms the project deletion inside the modal.
     */
    public void confirmProjectDeletion() {
        System.out.println("--- Confirming Project Deletion ---");
        // Wait for the confirmation modal to be visible before clicking the button inside
        wait.until(ExpectedConditions.visibilityOfElementLocated(CONFIRM_DELETE_BUTTON)); 
        clickElement(CONFIRM_DELETE_BUTTON, "Yes, Delete Confirmation Button");
        System.out.println("✅ Project confirmed for deletion.");
        
        // ADDED WAIT: Wait 5 seconds after clicking delete confirmation
        System.out.println("⏸️ Pausing 5 seconds after confirming deletion.");
        try {
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Cancels the project deletion inside the modal.
     */
    public void cancelProjectDeletion() {
        System.out.println("--- Cancelling Project Deletion ---");
        // Wait for the confirmation modal to be visible before clicking the button inside
        wait.until(ExpectedConditions.visibilityOfElementLocated(CANCEL_BUTTON)); 
        clickElement(CANCEL_BUTTON, "Cancel Deletion Button");
        System.out.println("✅ Project deletion cancelled.");
    }
    
    // --- Helper Methods ---

    private void clickElement(By locator, String elementName) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            element.click();
            System.out.println("📝 Clicked " + elementName + ".");
        } catch (Exception e) {
            System.err.println("❌ Failed to click " + elementName + " at locator: " + locator + ". Error: " + e.getMessage());
            throw new RuntimeException("Click failed on " + elementName, e);
        }
    }
}
