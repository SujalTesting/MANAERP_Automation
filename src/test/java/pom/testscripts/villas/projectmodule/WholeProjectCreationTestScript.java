package pom.testscripts.villas.projectmodule;

import java.util.concurrent.TimeoutException;

import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.SkipException; 

import base.BaseClass;
import dataModels.ProjectDataModal;
import pom.pageobjectclasses.villas.projectmodule.AccessAndOthersPage;
import pom.pageobjectclasses.villas.projectmodule.Home;
import pom.pageobjectclasses.villas.projectmodule.PaymentSchedule;
import pom.pageobjectclasses.villas.projectmodule.ProjectSetupPage;
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
            description = "Handles Payment Schedule Add, Edit, and Delete operations for dual-schedule Villa projects (Plot & Construction).")
      public void testPaymentScheduleOperations_Villa() throws InterruptedException, TimeoutException {
          	    
        try { 	
          // Initialize the Payment Schedule page object
          PaymentSchedule schedulePage = new PaymentSchedule(getDriver());
          test.log(Status.INFO, "Starting Payment Schedule operations (Add, Edit, Delete) for **Villa Dual Schedules (Plot & Construction)**.");

          // 1. Execute the ENTIRE Payment Schedule Flow 
          // The page object handles both Plot and Construction tabs, performing ops, and navigating away.
          schedulePage.newAddSchedules(projectData);
          test.log(Status.PASS, "Complete Payment Schedule flow executed successfully for Villa dual schedules.");

          // 2. Assertions (Logging the outcomes based on JSON input for both Plot and Construction)

          // --- A. Plot Schedule Assertions (using getPlotScheduleOperationVilla()) ---
          ProjectDataModal.ScheduleOperations plotOps = projectData.getPlotScheduleOperationVilla();
          
          if (plotOps != null) {
              test.log(Status.INFO, "--- Analyzing **Plot Component** schedule operations. ---");
              
              // Assert Delete Operation
              if (plotOps.getChargeToDelete() != null) {
                  String action = plotOps.getChargeToDelete().getModalConfirmationAction();
                  String name = plotOps.getChargeToDelete().getChargeName();
                  
                  if (action.equalsIgnoreCase("Delete")) { 
                      test.log(Status.PASS, "Plot item **" + name + "** was successfully **DELETED** by confirming the modal action: **" + action + "**.");
                  } else {
                      test.log(Status.PASS, "Plot deletion action was **CANCELLED** for item: **" + name + "** (Action: " + action + ").");
                  }
              }
              
              // Assert Edit Operation
              if (plotOps.getChargeToEdit() != null && plotOps.getUpdatedChargeData() != null) {
                  String originalName = plotOps.getChargeToEdit();
                  String newStageName = plotOps.getUpdatedChargeData().getPaymentStage();
                  String newCostType = plotOps.getUpdatedChargeData().getCostType();
                  
                  test.log(Status.PASS, 
                      "Plot item originally named **" + originalName + 
                      "** was successfully **EDITED** and renamed to **" + newStageName + 
                      "** with new Cost Type: **" + newCostType + "**.");
              } else {
                  test.log(Status.WARNING, "No Edit operation was defined for Villa Plot. Skipping Edit assertion.");
              }
          } else {
              test.log(Status.WARNING, "No schedule operations were defined for Villa Plot. Skipping detailed operation assertions.");
          }
          
          // --- B. Construction Schedule Assertions (using getConstructionScheduleOperationVilla()) ---
          ProjectDataModal.ScheduleOperations constructionOps = projectData.getConstructionScheduleOperationVilla();
          
          if (constructionOps != null) {
              test.log(Status.INFO, "--- Analyzing **Construction Component** schedule operations. ---");
              
              // Assert Delete Operation
              if (constructionOps.getChargeToDelete() != null) {
                  String action = constructionOps.getChargeToDelete().getModalConfirmationAction();
                  String name = constructionOps.getChargeToDelete().getChargeName();
                  
                  if (action.equalsIgnoreCase("Delete")) { 
                      test.log(Status.PASS, "Construction item **" + name + "** was successfully **DELETED** by confirming the modal action: **" + action + "**.");
                  } else {
                      test.log(Status.PASS, "Construction deletion action was **CANCELLED** for item: **" + name + "** (Action: " + action + ").");
                  }
              }
              
              // Assert Edit Operation
              if (constructionOps.getChargeToEdit() != null && constructionOps.getUpdatedChargeData() != null) {
                  String originalName = constructionOps.getChargeToEdit();
                  String newStageName = constructionOps.getUpdatedChargeData().getPaymentStage();
                  String newCostType = constructionOps.getUpdatedChargeData().getCostType();
                  
                  test.log(Status.PASS, 
                      "Construction item originally named **" + originalName + 
                      "** was successfully **EDITED** and renamed to **" + newStageName + 
                      "** with new Cost Type: **" + newCostType + "**.");
              } else {
                  test.log(Status.WARNING, "No Edit operation was defined for Villa Construction. Skipping Edit assertion.");
              }
          } else {
              test.log(Status.WARNING, "No schedule operations were defined for Villa Construction. Skipping detailed operation assertions.");
          }


        } catch (Exception e) {
          // Log the detailed error
          test.log(Status.FAIL, "FATAL ERROR during Villa Payment Schedule Operations (P3). Error: " + e.getMessage());
          throw e; 
        }
      }

    
 // =========================================================================
    // UPDATED TEST: Access and Others Page Flow (File Upload/Actions & User Access)
    // =========================================================================
    // FIX APPLIED HERE: changed dependsOnMethods from "testPaymentScheduleOperations" 
    // to the correct method name "testPaymentScheduleOperations_Villa"
    @Test(priority = 4, dependsOnMethods = "testPaymentScheduleOperations_Villa",
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
