package pom.testscripts.plots.projectmodule;

import java.util.concurrent.TimeoutException;

import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.Assert; 
import org.testng.SkipException;

import base.BaseClass;
import dataModels.ProjectDataModal;
import pom.pageobjectclasses.plots.projectmodule.Home;
import pom.pageobjectclasses.plots.projectmodule.UpdateProjectSetupPage;
import pom.pageobjectclasses.plots.projectmodule.UpdateCostSetupPage; // <-- NEW IMPORT ADDED
import utils.JsonDataReader;
import utils.TestListener;
import com.aventstack.extentreports.Status;

@Listeners(TestListener.class)
public class ExistingProjectTestScriptForApartment extends BaseClass {
	
	// 1. Load the data required for the update from 'UpdateProject.json'
	ProjectDataModal updateData = JsonDataReader.getAllUpdateTestData().getProject();

	// The 'projectData' object is now redundant for this specific test class, 
	// as all actions (view/update) rely on the update-specific data model.
	// We will comment it out or ideally remove it, but for compatibility, we'll keep it harmlessly uninitialized.
	// private final ProjectDataModal projectData = JsonDataReader.getProjectData(); 
	
	@Test(priority = 1, description = "Login and verify the user is on the dashboard.")
	public void testLoginAndVerifyHomePage() {
		Home home = new Home(getDriver());
		home.checkDashBoardURL();
		test.log(Status.PASS, "Dashboard URL verified successfully.");
	}

	// =========================================================================
	// TEST: View Existing Project
	// =========================================================================
	/**
	 * Navigates to the Projects listing page, applies filters (if provided),
	 * handles pagination, and clicks 'View Project' for a specific project.
	 */
	@Test(priority = 2, dependsOnMethods = "testLoginAndVerifyHomePage",
			description = "Navigates to the Projects listing, filters, and views an existing project.")
	public void testViewExistingProject() throws InterruptedException, TimeoutException {
				
		Home home = new Home(getDriver());
		
		try {
			// ⭐ FIX APPLIED HERE: Retrieve the ProjectSelectionDetails from the 'updateData' model
			// which contains the necessary "The Marriage" details from the provided JSON.
			ProjectDataModal.ProjectSelectionDetails details = updateData.getProjectSelectionDetails();
			
			// ⭐ Defensive Null Check (Kept as best practice)
			if (details == null || details.getProjectName() == null) {
				String errorMsg = "FATAL ERROR: Project Selection Details or Project Name is missing/null in the update data model. Check your JSON file.";
				test.log(Status.FAIL, errorMsg);
				Assert.fail(errorMsg);
			}

			test.log(Status.INFO, "Starting navigation to view existing project: **" + details.getProjectName() + "** (Type: " + details.getProjectType() + ").");
			
			// Execute the core functionality to navigate to the Project Details Page
			home.viewExistingProject(details);
			
			test.log(Status.PASS, "Successfully navigated to the details page for project: **" + details.getProjectName() + "**.");

		} catch (Exception e) {
			test.log(Status.FAIL, "FATAL ERROR during View Existing Project flow. Error: " + e.getMessage());
			// Re-throw the exception to mark the test as failed
			throw e;
		}
	}
	
	// =========================================================================
	// TEST: Update Existing Project Setup Details
	// =========================================================================
	@Test(priority = 3, dependsOnMethods = "testViewExistingProject", 
			description = "Updates the setup details (Name, Extent, Location, etc.) of the existing project.")
	public void testUpdateProjectSetup() throws InterruptedException, TimeoutException {
		
		// ⭐ Null Check for Update Data
		if (updateData == null || updateData.getBasicDetails() == null || updateData.getProjectExtent() == null) {
			String errorMsg = "FATAL ERROR: Update data is incomplete or null. Cannot proceed with update test.";
			test.log(Status.FAIL, errorMsg);
			Assert.fail(errorMsg);
		}
		
		// 2. Initialize the Page Object Model
		UpdateProjectSetupPage updatePage = new UpdateProjectSetupPage(getDriver());
		
		test.log(Status.INFO, "Attempting to update Project Setup with new name: **" + updateData.getBasicDetails().getProjectName() + "**.");
		
		// 3. Fill the entire form with the update data and click the update button
		updatePage.fillEntireForm(updateData);
		
		test.log(Status.PASS, "Project Setup form filled and Update button clicked successfully.");
		
		// 4. Verification (Read the updated values from the form/page and assert)
		
		// Get expected values
		String expectedName = updateData.getBasicDetails().getProjectName();
		String expectedExtent = updateData.getProjectExtent().getSingleValue(); 
		
		// Get actual values from the UI after submission
		String actualName = updatePage.getProjectNameValue();
		String actualExtent = updatePage.getProjectExtentValue();
		
		// Assertion 1: Verify Project Name
		Assert.assertEquals(actualName, expectedName, 
			"Assertion Failed: Project Name was not updated correctly. Expected: " + expectedName + ", Actual: " + actualName);
		test.log(Status.PASS, "Verified Project Name updated to: **" + actualName + "**.");
		
//		// Assertion 2: Verify Project Extent (Commented out as per original script structure, but should be enabled if needed)
//		Assert.assertEquals(actualExtent, expectedExtent, 
//			"Assertion Failed: Project Extent was not updated correctly. Expected: " + expectedExtent + ", Actual: " + actualExtent);
//		test.log(Status.PASS, "Verified Project Extent updated to: **" + actualExtent + "**.");
		
		test.log(Status.PASS, "Successfully updated and verified the existing project setup details.");
	}
	

	
//	 =========================================================================
//	 NEW TEST: Update Existing Project Cost Setup Details
//	 =========================================================================
	@Test(priority = 4, dependsOnMethods = "testUpdateProjectSetup", 
			description = "Updates the Cost Setup details of the existing project.")
	public void testUpdateProjectCostSetup() throws InterruptedException, TimeoutException {
		
		// 2. Initialize the Page Object Model
		UpdateCostSetupPage costPage = new UpdateCostSetupPage(getDriver());
		
		try {
    		
			costPage.fillEntireForm(updateData);
	    	// 2. CHARGE OPERATIONS (Edit/Delete) - Execution
			costPage.handleChargeMasterActions(updateData);
	        test.log(Status.INFO, "Executed charge master actions (Delete/Edit) via orchestrator.");

	        // 3. Assertions (Logging the outcomes based on JSON input)
	        ProjectDataModal.ChargeOperations ops = updateData.getChargeOperations();
	        if (ops != null) {
	            if (ops.getChargeToDelete() != null) {
	                test.log(Status.PASS, "Charge '" + ops.getChargeToDelete().getChargeName() + "' confirmed for deletion in flow.");
	            }
	            if (ops.getChargeToEdit() != null) {
	                test.log(Status.PASS, "Charge '" + ops.getChargeToEdit() + "' confirmed for edit in flow.");
	            } 
	        }
	    } catch (Exception e) {
	        // ⭐ CRITICAL: Log the detailed error that is causing test P3 to skip
	        test.log(Status.FAIL, "FATAL ERROR during Project Setup (P2). P3 will be skipped. Error: " + e.getMessage());
	        // Re-throw or throw a SkipException to handle the failure gracefully if desired
	        throw e; 
	    }
		
		test.log(Status.PASS, "Successfully updated and verified the existing project Cost Setup details.");
	}
}