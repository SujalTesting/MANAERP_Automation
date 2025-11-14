package dataModels;

import java.util.List;

public class ProjectDataModal {
    
    private String testId;
    
    // =========================================================================
	// 1. Existing Project Selection Details (Highest Priority for existing project tests)
    // =========================================================================
    private ProjectSelectionDetails projectSelectionDetails;
    
    // =========================================================================
    // 2. TAB 1 & 2: Core Details
    // =========================================================================
    private BasicDetails basicDetails;
    private AreaConverter projectExtent; // Initial area 
    private AreaConverter saleableArea;  // Initial saleable area 
    private ApprovalDetails approvals;
    private BankDetails bankDetails; 
    private LocationDetails locationDetails; 
    
    // =========================================================================
    // 3. TAB 3: Project Units Setup 
    // =========================================================================
    
    private Unit unitDetails;
    
    private List<Block> blocks;
    
    // =========================================================================
    // 3. TAB 3: Exisiting Cost Setup 
    // =========================================================================
    
//    private CostDetails costDetails;
    
    // =========================================================================
    // 3. TAB 3: Cost Setup Project Creation
    // =========================================================================
    private CostSheetDetails costSheet;
    private AddChargesDetails addCharges;
    private ChargeOperations chargeOperations; // Used for EDIT/DELETE operations
    
    // =========================================================================
    // 4. TAB 4: Payment Schedule
    // =========================================================================
    // For Apartment/Plot Projects
    private AddScheduleDetails addScheduleApartmentOrPlot;
    private ScheduleOperations scheduleOperationApartmentOrPlot; // Used for EDIT/DELETE
    
    // For Villa Projects (Plot Component)
    private AddScheduleDetails plotAddScheduleVilla;
    private ScheduleOperations plotScheduleOperationVilla; // Used for EDIT/DELETE

    // For Villa Projects (Construction Component)
    private AddScheduleDetails constructionAddScheduleVilla;
    private ScheduleOperations constructionScheduleOperationVilla; // Used for EDIT/DELETE
    
    // =========================================================================
    // 5. TAB 5: Documents
    // =========================================================================
    private DocumentDetails projectApprovalsDetails; 
    private DocumentDetails marketingDocsDetails;    
    private DocumentDetails legalDocsDetails;        
    private DocumentDetails otherDocsDetails;        
    
    // =========================================================================
    // 6. TAB 6: Access & Permissions
    // =========================================================================
    private AccessDetails salesAccessDetails;
    private AccessDetails creditNoteIssuersDetails;
    private AccessDetails accountsAccessDetails;
    private AccessDetails marketingAccessDetails;
    private AccessDetails crmAccessDetails;
    private AccessDetails legalAccessDetails;
    
    // =========================================================================
    // 7. TAB 7: Project Settings
    // =========================================================================
	private ProjectSettingsDetails projectSettingsDetails;
    
    public ProjectDataModal() {
    	// Initialize new details
    	this.projectSettingsDetails = new ProjectSettingsDetails();
    } 

    // =========================================================================
    // MAIN CLASS GETTERS (Public) 
    // =========================================================================
    
    public String getTestId() { return testId; }
    
 // ⭐ PROJECT UNIT GETTER: Returns the list of blocks (REQUIRED FOR STEP 2)
    public List<Block> getBlocks() { return blocks; }
    
	// ACCESSOR FOR Project Selection Details (Moved up)
    public ProjectSelectionDetails getProjectSelectionDetails() { return projectSelectionDetails; }
    
    public BasicDetails getBasicDetails() { return basicDetails; }
    public AreaConverter getProjectExtent() { return projectExtent; }
    public AreaConverter getSaleableArea() { return saleableArea; }
    public ApprovalDetails getApprovals() { return approvals; } 
    public BankDetails getBankDetails() { return bankDetails; } 
    public LocationDetails getLocationDetails() { return locationDetails; }
//    public CostDetails getCostDetails() { return costDetails; }
    public Unit getUnitDetails( ) { return unitDetails; }
    public CostSheetDetails getCostSheet() { return costSheet; }
    public AddChargesDetails getAddCharges() { return addCharges; }
    public ChargeOperations getChargeOperations() { return chargeOperations; } 
    
    // Schedule Getters 
    public AddScheduleDetails getAddScheduleApartmentOrPlot() { return addScheduleApartmentOrPlot; }
    public ScheduleOperations getScheduleOperationApartmentOrPlot() { return scheduleOperationApartmentOrPlot; } 
    public AddScheduleDetails getPlotAddScheduleVilla() { return plotAddScheduleVilla; }
    public ScheduleOperations getPlotScheduleOperationVilla() { return plotScheduleOperationVilla; } 
    public AddScheduleDetails getConstructionAddScheduleVilla() { return constructionAddScheduleVilla; }
    public ScheduleOperations getConstructionScheduleOperationVilla() { return constructionScheduleOperationVilla; } 
    
    public DocumentDetails getProjectApprovalsDetails() { return projectApprovalsDetails; }
    public DocumentDetails getMarketingDocsDetails() { return marketingDocsDetails; }
    public DocumentDetails getLegalDocsDetails() { return legalDocsDetails; }
    public DocumentDetails getOtherDocsDetails() { return otherDocsDetails; } 
    
    // ACCESS GETTERS
    public AccessDetails getSalesAccessDetails() { return salesAccessDetails; }
    public AccessDetails getCreditNoteIssuersDetails() { return creditNoteIssuersDetails; }
    public AccessDetails getAccountsAccessDetails() { return accountsAccessDetails; }
    public AccessDetails getMarketingAccessDetails() { return marketingAccessDetails; }
    public AccessDetails getCrmAccessDetails() { return crmAccessDetails; }
    public AccessDetails getLegalAccessDetails() { return legalAccessDetails; }
    
    // PROJECT SETTINGS GETTER
	public ProjectSettingsDetails getProjectSettingsDetails() { return projectSettingsDetails; } 

    
    // =========================================================================
    // MAIN CLASS SETTERS 
    // =========================================================================

    public void setTestId(String testId) { this.testId = testId; }
    
	// ACCESSOR FOR Project Selection Details (Moved up)
    public void setProjectSelectionDetails(ProjectSelectionDetails projectSelectionDetails) { this.projectSelectionDetails = projectSelectionDetails; }

    public void setBasicDetails(BasicDetails basicDetails) { this.basicDetails = basicDetails; }
    public void setProjectExtent(AreaConverter projectExtent) { this.projectExtent = projectExtent; }
    public void setSaleableArea(AreaConverter saleableArea) { this.saleableArea = saleableArea; }
    public void setApprovals(ApprovalDetails approvals) { this.approvals = approvals; }
    public void setBankDetails(BankDetails bankDetails) { this.bankDetails = bankDetails; }
    public void setLocationDetails(LocationDetails locationDetails) { this.locationDetails = locationDetails; }
    public void setCostSheet(CostSheetDetails costSheet) { this.costSheet = costSheet; }
    public void setAddCharges(AddChargesDetails addCharges) { this.addCharges = addCharges; }
    public void setChargeOperations(ChargeOperations chargeOperations) { this.chargeOperations = chargeOperations; }
    
    // Schedule Setters 
    public void setAddSchedule_Apartment_or_Plot(AddScheduleDetails addScheduleApartmentOrPlot) { this.addScheduleApartmentOrPlot = addScheduleApartmentOrPlot; }
    public void setScheduleOperation_Apartment_or_Plot(ScheduleOperations scheduleOperationApartmentOrPlot) { this.scheduleOperationApartmentOrPlot = scheduleOperationApartmentOrPlot; }
    public void setPlotAddSchedule_Villa(AddScheduleDetails plotAddScheduleVilla) { this.plotAddScheduleVilla = plotAddScheduleVilla; }
    public void setPlotScheduleOperation_Villa(ScheduleOperations plotScheduleOperationVilla) { this.plotScheduleOperationVilla = plotScheduleOperationVilla; }
    public void setConstructionAddSchedule_Villa(AddScheduleDetails constructionAddScheduleVilla) { this.constructionAddScheduleVilla = constructionAddScheduleVilla; }
    public void setConstructionScheduleOperation_Villa(ScheduleOperations constructionScheduleOperationVilla) { this.constructionScheduleOperationVilla = constructionScheduleOperationVilla; }
    
    public void setProjectApprovalsDetails(DocumentDetails projectApprovalsDetails) { this.projectApprovalsDetails = projectApprovalsDetails; }
    public void setMarketingDocsDetails(DocumentDetails marketingDocsDetails) { this.marketingDocsDetails = marketingDocsDetails; }
    public void setLegalDocsDetails(DocumentDetails legalDocsDetails) { this.legalDocsDetails = legalDocsDetails; }
    public void setOtherDocsDetails(DocumentDetails otherDocsDetails) { this.otherDocsDetails = otherDocsDetails; }
    
    // ACCESS SETTERS
    public void setSalesAccessDetails(AccessDetails salesAccessDetails) { this.salesAccessDetails = salesAccessDetails; }
    public void setCreditNoteIssuersDetails(AccessDetails creditNoteIssuersDetails) { this.creditNoteIssuersDetails = creditNoteIssuersDetails; }
    public void setAccountsAccessDetails(AccessDetails accountsAccessDetails) { this.accountsAccessDetails = accountsAccessDetails; }
    public void setMarketingAccessDetails(AccessDetails marketingAccessDetails) { this.marketingAccessDetails = marketingAccessDetails; }
    public void setCrmAccessDetails(AccessDetails crmAccessDetails) { this.crmAccessDetails = crmAccessDetails; }
    public void setLegalAccessDetails(AccessDetails legalAccessDetails) { this.legalAccessDetails = legalAccessDetails; }

	// PROJECT SETTINGS SETTER
    public void setProjectSettingsDetails(ProjectSettingsDetails projectSettingsDetails) { this.projectSettingsDetails = projectSettingsDetails; }
    
    
    // =========================================================================
    // INNER CLASSES (DATA STRUCTURES)
    // =========================================================================
    
	// --- A. Existing Project Selection Details (Used for locating the project) ---
    public static class ProjectSelectionDetails {
        private String projectName; // Name of the project to view, e.g., "The Lock"
        private String projectType; // Project Type dropdown selection, e.g., "Plots"
        private int pageNumber;     // Pagination button number to click, e.g., 1 or 10

        // Getters
        public String getProjectName() { return projectName; }
        public String getProjectType() { return projectType; }
        public int getPageNumber() { return pageNumber; }
        
        // Setters
        public void setProjectName(String projectName) { this.projectName = projectName; }
        public void setProjectType(String projectType) { this.projectType = projectType; }
        public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    }
    
    // --- B. Project Basic Details (Mainly for Initial Creation) ---
    public static class BasicDetails {
        private String projectType; 
        private String projectName;
        private String projectUrl;
        private String marketedBy;
        private String logoFilePath;
        
        // Getters
        public String getProjectType() { return projectType; }
        public String getProjectName() { return projectName; }
        public String getProjectUrl() { return projectUrl; }
        public String getMarketedBy() { return marketedBy; }
        public String getLogoFilePath() { return logoFilePath; }
    }

    // --- C. Area Converters (Project Extent / Saleable Area) ---
    public static class AreaConverter {
        private String isConverterUsed; // "Yes" or "No"
        private String singleValue;
        private String value1;
        private String unit1;
        private String value2;
        private String unit2;
        
        // Getters
        public String getIsConverterUsed() { return isConverterUsed; }
        public String getSingleValue() { return singleValue; }
        public String getValue1() { return value1; }
        public String getUnit1() { return unit1; } 
        public String getValue2() { return value2; }
        public String getUnit2() { return unit2; } 
    }
    
    // --- D. Approvals & Compliance ---
    public static class ApprovalDetails {
        private String planningApprovalStatus; 
        private String planningAuthorityName;  
        private String approvalNumber;         
        private String approvalDate;           
        private String reraApprovalStatus;     
        private String reraNumber;             
        private String reraStartDate;          
        private String reraEndDate;    
        
        // Getters
        public String getPlanningApprovalStatus() { return planningApprovalStatus; }
        public String getPlanningAuthorityName() { return planningAuthorityName; }
        public String getApprovalNumber() { return approvalNumber; }
        public String getApprovalDate() { return approvalDate; }
        public String getReraApprovalStatus() { return reraApprovalStatus; }
        public String getReraNumber() { return reraNumber; }
        public String getReraStartDate() { return reraStartDate; }
        public String getReraEndDate() { return reraEndDate; }
    }
    
    // --- E. Bank Details ---
    public static class BankDetails {
        private List<String> banks;  

        // Getter
        public List<String> getBanks() { return banks; }
        
        // Setter 
        public void setBanks(List<String> banks) { this.banks = banks; }
    }
    
    // --- F. Location Details ---
    public static class LocationDetails {
        private String pincode;
        private String address;

        // Getters
        public String getPincode() { return pincode; }
        public String getAddress() { return address; }

        // Setters
        public void setPincode(String pincode) { this.pincode = pincode; }
        public void setAddress(String address) { this.address = address; }
    }
    
    
    // INNER STATIC DATA MODEL CLASSES (PROJECT UNITS - STEP 2)
    // =========================================================================
    
    // === 1. UNIT DATA MODEL ===
    /**
     * Data model for a single Unit/Plot inside a Block.
     */
    public static class Unit {
        // Unit Details
        private String unitNo;
        private String unitType;
        private String dimension;
        private String facing;
        private String areaSqft;
        private String priceSqft;
        private String plcPerSqft;
        private String minRatePerSqft;

        // Dimension Details
        private String eastSide;
        private String westSide;
        private String northSide;
        private String southSide;
        
        // Status Details
        private String unitStatus;
        private String releaseStatus;
        private String mortgageType;
        private String sharingType;
        
     // ⭐ NEW: Additional Details
        private String surveyNo;
        private String kathaNo;
        private String pidNo;
        private String inventoryType; // Maps to Inventory Type Dropdown
        
     // ⭐ NEW Getters
        public String getSurveyNo() { return surveyNo; }
        public String getKathaNo() { return kathaNo; }
        public String getPidNo() { return pidNo; }
        public String getInventoryType() { return inventoryType; }
        
        // Getters
		public String getUnitNo() { return unitNo; }
		public String getUnitType() { return unitType; }
		public String getDimension() { return dimension; }
		public String getFacing() { return facing; }
		public String getAreaSqft() { return areaSqft; }
		public String getPriceSqft() { return priceSqft; }
		public String getPlcPerSqft() { return plcPerSqft; }
		public String getMinRatePerSqft() { return minRatePerSqft; }
		public String getEastSide() { return eastSide; }
		public String getWestSide() { return westSide; }
		public String getNorthSide() { return northSide; }
		public String getSouthSide() { return southSide; }
		public String getUnitStatus() { return unitStatus; }
		public String getReleaseStatus() { return releaseStatus; }
		public String getMortgageType() { return mortgageType; }
		public String getSharingType() { return sharingType; }
	}
    
    // === 2. BLOCK DATA MODEL ===
    /**
     * Data model for a single Block/Unit container within the Project Unit setup.
     */
    public static class Block {
        private String blockName;
        private int expectedFloorCount; 
        private List<Unit> units; // Contains a list of units

        // Getters
		public String getBlockName() { return blockName; }
		public int getExpectedFloorCount() { return expectedFloorCount; }
		public List<Unit> getUnits() { return units; }
	}
    
// // --- Existing Cost Setup Details (Base pricing) ---
//    public static class CostDetails {
//        private String totalCost; // Matches the method call getTotalCost()
//
//        // Constructor for deserialization
//        public CostDetails() {}
//
//        // Getter used in the test script:
//        public String getTotalCost() {
//            return totalCost;
//        }
//    }
    
    // --- G. Cost Sheet Details (Base pricing) ---
    public static class CostSheetDetails {
        private String basePricePerSqft;
        private String standardTaxRate;
        private String baseConstructionPricePerSqft;
        private String constructionTaxRate;

        // Getters
        public String getBasePricePerSqft() { return basePricePerSqft; }
        public String getStandardTaxRate() { return standardTaxRate; }
        public String getBaseConstructionPricePerSqft() { return baseConstructionPricePerSqft; }
        public String getConstructionTaxRate() { return constructionTaxRate; }

        // Setters
        public void setBasePricePerSqft(String basePricePerSqft) { this.basePricePerSqft = basePricePerSqft; }
        public void setStandardTaxRate(String standardTaxRate) { this.standardTaxRate = standardTaxRate; }
        public void setBaseConstructionPricePerSqft(String baseConstructionPricePerSqft) { this.baseConstructionPricePerSqft = baseConstructionPricePerSqft; }
        public void setConstructionTaxRate(String constructionTaxRate) { this.constructionTaxRate = constructionTaxRate; }
    }

    // --- H. Additional Charges Structures ---
    
    // Container for a list of charges to add (creation/adding to existing)
    public static class AddChargesDetails {
        private List<Charge> charges;

        // Getter
        public List<Charge> getCharges() { return charges; }
        
        // Setter
        public void setCharges(List<Charge> charges) { this.charges = charges; }
    }

    // Structure for a single charge (for addition or update)
    public static class Charge {
        private String chargesFor; // e.g., "Apartment/Plot"
        private String category;   // e.g., "EDC", "PLC"
        private String costType;   // e.g., "Percentage", "Amount"
        private String amount;
        private String tax;

        // Getters
        public String getChargesFor() { return chargesFor; }
        public String getCategory() { return category; }
        public String getCostType() { return costType; }
        public String getAmount() { return amount; }
        public String getTax() { return tax; }
        
        // Setters
        public void setChargesFor(String chargesFor) { this.chargesFor = chargesFor; }
        public void setCategory(String category) { this.category = category; }
        public void setCostType(String costType) { this.costType = costType; }
        public void setAmount(String amount) { this.amount = amount; }
        public void setTax(String tax) { this.tax = tax; }
    }
    
    // Structure for charge deletion data
    public static class DeleteOperationDetails {
        private String chargeName;
        private String modalConfirmationAction; // e.g., "Delete" or "Cancel"

        // Getters
        public String getChargeName() { return chargeName; }
        public String getModalConfirmationAction() { return modalConfirmationAction; }
    }
    
    // Container for Edit/Delete operations on charges
    public static class ChargeOperations {
    	private DeleteOperationDetails chargeToDelete; // Data for deleting an existing charge
        private String chargeToEdit;                   // Name of existing charge to edit
        private Charge updatedChargeData;              // New data for the charge being edited

        // Getters and Setters
        public DeleteOperationDetails getChargeToDelete() { return chargeToDelete; }
        public String getChargeToEdit() {  return chargeToEdit;  }
        public void setChargeToEdit(String chargeToEdit) { this.chargeToEdit = chargeToEdit; }
        public Charge getUpdatedChargeData() { return updatedChargeData;  }
        public void setUpdatedChargeData(Charge updatedChargeData) { this.updatedChargeData = updatedChargeData; }
    }
    
    // --- I. Payment Schedule Structures ---
    
    // Container for a list of schedules to add
    public static class AddScheduleDetails {
        private List<PaymentScheduleOptions> charges;

        // Getter
        public List<PaymentScheduleOptions> getCharges() { return charges; }
        
        // Setter
        public void setCharges(List<PaymentScheduleOptions> charges) { this.charges = charges; }
    }
    
    // Structure for a single payment schedule item
    public static class PaymentScheduleOptions {
        private String paymentStage;     
        private String costType;         // e.g., "Percentage", "Amount"
        private String amountPercentage; // Value of the cost
        private String timelineDays;     
        private String description;      

        // Getters
        public String getPaymentStage() { return paymentStage; }
        public String getCostType() { return costType; }
        public String getAmountPercentage() { return amountPercentage; }
        public String getTimelineDays() { return timelineDays; }
        public String getDescription() { return description; }

        // Setters
        public void setPaymentStage(String paymentStage) { this.paymentStage = paymentStage; }
        public void setCostType(String costType) { this.costType = costType; }
        public void setAmountPercentage(String amountPercentage) { this.amountPercentage = amountPercentage; }
        public void setTimelineDays(String timelineDays) { this.timelineDays = timelineDays; }
        public void setDescription(String description) { this.description = description; }
    }
    
    // Structure for schedule deletion data
    public static class DeleteScheduleDetails {
        private String chargeName;
        private String modalConfirmationAction; // e.g., "Delete" or "Cancel"

        // Getters
        public String getChargeName() { return chargeName; }
        public String getModalConfirmationAction() { return modalConfirmationAction; }
    }
    
    // Container for Edit/Delete operations on schedules
    public static class ScheduleOperations {
    	private DeleteScheduleDetails chargeToDelete;       // Data for deleting an existing schedule
        private String chargeToEdit;                       // Name of existing schedule item to edit
        private PaymentScheduleOptions updatedChargeData;  // New data for the schedule item being edited

        // Getters and Setters
        public DeleteScheduleDetails getChargeToDelete() { return chargeToDelete; }
        public String getChargeToEdit() {  return chargeToEdit;  }
        public void setChargeToEdit(String chargeToEdit) { this.chargeToEdit = chargeToEdit; }
        public PaymentScheduleOptions getUpdatedChargeData() { return updatedChargeData;  }
        public void setUpdatedChargeData(PaymentScheduleOptions updatedChargeData) { this.updatedChargeData = updatedChargeData; }
    }
    
    // --- J. Document Details (File paths for uploads) ---
    public static class DocumentDetails {
        private List<String> docsToUpload;
        
        // Getter
        public List<String> getDocsToUpload() { return docsToUpload; }
        
        // Setter
        public void setDocsToUpload(List<String> docsToUpload) { this.docsToUpload = docsToUpload; }
    }
    
    // --- K. User Access Management ---
    public static class AccessDetails {
        private List<String> usersToGrantAccess;

        public List<String> getUsersToGrantAccess() {
            return usersToGrantAccess;
        }

        public void setUsersToGrantAccess(List<String> usersToGrantAccess) {
            this.usersToGrantAccess = usersToGrantAccess;
        }
    }
    
	// --- L. Project Settings Details (Used for Configuration/Deletion) ---
	public static class ProjectSettingsDetails {
		private boolean allowPastDateBooking; // Setting toggle (true/false)
		private String maxBlockDays;          // Setting input
		private String deleteAction;          // Used for deleting the EXISTING project ("Delete project" or "Cancel")
		
		public ProjectSettingsDetails() {}

		public boolean isAllowPastDateBooking() {
			return allowPastDateBooking;
		}

		public void setAllowPastDateBooking(boolean allowPastDateBooking) {
			this.allowPastDateBooking = allowPastDateBooking;
		}

		public String getMaxBlockDays() {
			return maxBlockDays;
		}

		public void setMaxBlockDays(String maxBlockDays) {
			this.maxBlockDays = maxBlockDays;
		}

		public String getDeleteAction() {
			return deleteAction;
		}

		public void setDeleteAction(String deleteAction) {
			this.deleteAction = deleteAction;
		}
	}
    
}
