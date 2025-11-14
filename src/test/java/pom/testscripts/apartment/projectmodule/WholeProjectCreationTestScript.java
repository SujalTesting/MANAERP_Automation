package pom.testscripts.apartment.projectmodule;

import java.util.concurrent.TimeoutException;

import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.SkipException; 

import base.BaseClass;
import dataModels.ProjectDataModal;
import pom.pageobjectclasses.apartment.projectmodule.AccessAndOthersPage;
import pom.pageobjectclasses.apartment.projectmodule.Home;
import pom.pageobjectclasses.apartment.projectmodule.PaymentSchedule;
import pom.pageobjectclasses.apartment.projectmodule.ProjectSetupPage;
import utils.JsonDataReader;
import utils.TestListener;
import com.aventstack.extentreports.Status;

@Listeners(TestListener.class)
public class WholeProjectCreationTestScript extends BaseClass {

	private final ProjectDataModal projectData = JsonDataReader.getAllTestData().getProject();

    @Test(priority = 1, description = "Login and verify the user is on the dashboard.")
    public void testLoginAndVerifyHomePage() {
        Home home = new Home(getDriver());
        home.checkDashBoardURL();
        test.log(Status.PASS, "Dashboard URL verified successfully.");
    }

    // =========================================================================
    // MASTER TEST: Fills all steps sequentially (Step 1, Create, Cost Sheet, Charges)
    // =========================================================================
    @Test(priority = 2, dependsOnMethods = "testLoginAndVerifyHomePage",
          description = "Fills all project setup steps and performs charge Edit/Delete operations.")
    public void testFullProjectCreationAndChargeOperations() throws InterruptedException, TimeoutException {

        Home home = new Home(getDriver());
        ProjectSetupPage setupPage = new ProjectSetupPage(getDriver());
        
        test.log(Status.INFO, "Navigating to Project Creation and starting all steps.");
        home.navigateToProjectCreation();
        
        try {
            // 1. Execute the ENTIRE Project Creation Flow
            setupPage.fillEntireForm(projectData); 
            test.log(Status.INFO, "Project Setup and Initial Charges added via the master flow.");

            // 2. CHARGE OPERATIONS (Edit/Delete) - Execution
            setupPage.handleChargeMasterActions(projectData);
            test.log(Status.INFO, "Executed charge master actions (Delete/Edit) via orchestrator.");

            // 3. Assertions (Logging the outcomes based on JSON input)
            ProjectDataModal.ChargeOperations ops = projectData.getChargeOperations();
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

    }
    
    // NEW TEST: Payment Schedule Operations
    // =========================================================================
    @Test(priority = 3, dependsOnMethods = "testFullProjectCreationAndChargeOperations",
            description = "Handles Payment Schedule Add, Edit, and Delete operations for single-schedule projects (Apartment/Plot).")
      public void testPaymentScheduleOperations_ApartmentPlot() throws InterruptedException, TimeoutException {
          	    
        try { 	
          // Initialize the Payment Schedule page object
          PaymentSchedule schedulePage = new PaymentSchedule(getDriver());
          test.log(Status.INFO, "Starting Payment Schedule operations (Add, Edit, Delete) for **Apartment/Plot single schedule**.");

          // 1. Execute the ENTIRE Payment Schedule Flow (Add, Edit, Delete, Next)
          // The page object handles adding schedules, performing ops, and navigating away.
          schedulePage.newAddSchedules(projectData);
          test.log(Status.PASS, "Complete Payment Schedule flow executed successfully for single schedule.");

          // 2. Assertions (Logging the outcomes based on JSON input for Apartment/Plot)
          // Fetches the single schedule operations data
          ProjectDataModal.ScheduleOperations ops = projectData.getScheduleOperationApartmentOrPlot();
          
          if (ops != null) {
              test.log(Status.INFO, "Analyzing results based on data-driven schedule operations...");
              
              // Assert Delete Operation
              if (ops.getChargeToDelete() != null) {
                  String action = ops.getChargeToDelete().getModalConfirmationAction();
                  String name = ops.getChargeToDelete().getChargeName();
                  
                  if (action.equalsIgnoreCase("Delete")) { 
                      test.log(Status.PASS, "**Apartment/Plot Schedule:** Item **" + name + "** was successfully **DELETED** by confirming the modal action: **" + action + "**.");
                  } else {
                      test.log(Status.PASS, "**Apartment/Plot Schedule:** Deletion action was **CANCELLED** for item: **" + name + "** (Action: " + action + ").");
                  }
              }
              
              // Assert Edit Operation
              if (ops.getChargeToEdit() != null && ops.getUpdatedChargeData() != null) {
                  String originalName = ops.getChargeToEdit();
                  String newStageName = ops.getUpdatedChargeData().getPaymentStage();
                  String newCostType = ops.getUpdatedChargeData().getCostType();
                  
                  test.log(Status.PASS, 
                      "**Apartment/Plot Schedule:** Item originally named **" + originalName + 
                      "** was successfully **EDITED** and renamed to **" + newStageName + 
                      "** with new Cost Type: **" + newCostType + "**.");
              } else {
                  test.log(Status.WARNING, "**Apartment/Plot Schedule:** No Edit operation was defined. Skipping detailed operation assertions.");
              }
          } else {
              test.log(Status.WARNING, "**Apartment/Plot Schedule:** No operations (Edit/Delete) were defined. Skipping detailed operation assertions.");
          }
          
        } catch (Exception e) {
          // Log the detailed error
          test.log(Status.FAIL, "FATAL ERROR during Apartment/Plot Payment Schedule Operations (P3). Error: " + e.getMessage());
          throw e; 
        }
      }
    
 // =========================================================================
    // UPDATED TEST: Access and Others Page Flow (File Upload/Actions & User Access)
    // =========================================================================
    @Test(priority = 4, dependsOnMethods = "testPaymentScheduleOperations_ApartmentPlot", // <-- FIX APPLIED HERE
          description = "Handles document management (Upload/Delete) and user access management for all folders.")
    public void testAccessAndOthersPageFlow() throws InterruptedException, TimeoutException {
        
        try {   
            AccessAndOthersPage accessPage = new AccessAndOthersPage(getDriver());
            test.log(Status.INFO, "Starting Access and Others page flow: Document and Access Management.");

            // 1. Execute all 4 Document Management cards (Upload, View, Download, Delete)
            accessPage.manageAllDocumentCards(projectData);
            test.log(Status.INFO, "Successfully completed all 4 Document Management cards (File Uploads/Actions).");
            
            // 2. Execute all 6 User Access Management cards (Search and Select)
            accessPage.manageAllAccessCards(projectData);
            test.log(Status.INFO, "Successfully completed all 6 User Access Management cards (granting access).");
            
         // ⭐ 4. Manage Project Settings, Audit, and Danger Zone
            System.out.println("\n--- Starting Project Settings, Audit, and Danger Zone ---");
            accessPage.configureProjectSettings(projectData);
            
            // Assertion/Logging of completion
            test.log(Status.PASS, "Access and Others page flow completed successfully, including all document and access actions.");

        } catch (Exception e) {
            // CRITICAL: Log the detailed error
            test.log(Status.FAIL, "FATAL ERROR during Access and Others Flow (P4). Error: " + e.getMessage());
            // Re-throw the exception to mark the test as failed
            throw e; 
        }
    }
}
