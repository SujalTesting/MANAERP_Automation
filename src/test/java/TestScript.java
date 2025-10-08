import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseClass; // Assuming this is your base class with driver setup
import base.ProjectDataModal;
import page.Home;
import page.ProjectSetupPage;
import utils.JsonDataReader;

public class TestScript extends BaseClass {

    @Test(priority = 1, description = "Login and verify the user is on the dashboard.")
    public void testLoginAndVerifyHomePage() {
        test = extent.createTest("Login and Home URL Verification");
        Home home = new Home(driver);
        home.checkDashBoardURL(); // Assuming this method verifies the homepage
        test.pass("Successfully logged in and verified dashboard URL.");
    }
    
    @Test(priority = 2, dependsOnMethods = "testLoginAndVerifyHomePage", description = "Fills out the Project Setup form with data from JSON.")
    public void testFillProjectSetupStep1() {
        test = extent.createTest("Fill Project Setup - Step 1");

        // 1. Read test data from the JSON file
        ProjectDataModal projectData = JsonDataReader.getProjectData();
        Assert.assertNotNull(projectData, "Failed to read project data from JSON.");
        test.info("Successfully loaded test data for: " + projectData.getTestId());

        // 2. Navigate to the project creation page (this might be needed)
        Home home = new Home(driver);
        home.navigateToProjectCreation(); 

        // 3. Initialize the Page Object for the Project Setup screen
        ProjectSetupPage setupPage = new ProjectSetupPage(driver);
        test.info("Project Setup page loaded.");

        // 4. Fill the entire form using the data
        setupPage.fillEntireForm(projectData);
        test.info("Filled all fields on the Project Setup page.");
        
        // 5. Click the 'Next' button to proceed to the next step
        setupPage.clickNextStep();
        test.info("Clicked 'Next' to proceed to Project Units step.");

        // 6. (Optional but Recommended) Add an assertion to verify the navigation
        // For example, you can check if the title of the next step is visible.
        // Assert.assertTrue(driver.findElement(By.xpath("//*[contains(text(), 'Project Units')]")).isDisplayed());
        
        test.pass("Successfully completed and submitted Step 1 of Project Setup.");
    }
}
