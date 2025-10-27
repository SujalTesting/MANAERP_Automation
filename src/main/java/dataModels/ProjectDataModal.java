package dataModels;

import java.util.List;

/**
 * Main Data model for a single Project Creation test run, containing Basic Details and Area Inputs.
 */
public class ProjectDataModal {
    
    private String testId;
    
    private BasicDetails basicDetails;
    private AreaConverter projectExtent;
    private AreaConverter saleableArea;
    private ApprovalDetails approvals;
    private BankDetails bankDetails; // <-- NEW FIELD
    private LocationDetails locationDetails; // now we use LocationDetails
    private CostSheetDetails costSheet;
    private AddChargesDetails addCharges;
    private ChargeOperations chargeOperations;
    
    public ProjectDataModal() {} 

    // Public getters
    public BasicDetails getBasicDetails() { return basicDetails; }
    public AreaConverter getProjectExtent() { return projectExtent; }
    public AreaConverter getSaleableArea() { return saleableArea; }
    public ApprovalDetails getApprovals() { return approvals; } 
    public BankDetails getBankDetails() { return bankDetails; } // <-- NEW
    public LocationDetails getLocationDetails() { return locationDetails; }
    public CostSheetDetails getCostSheet() { return costSheet; }
    public AddChargesDetails getAddCharges() { return addCharges; }
    public ChargeOperations getChargeOperations() { return chargeOperations; }
    public String getTestId() { return testId; }
    
    
    // --- INNER CLASSES ---
    
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
    
    // ⭐ NEW INNER CLASS FOR APPROVALS & COMPLIANCE
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
    
    // ========== NEW: BankDetails ==========
    public static class BankDetails {
        private List<String> banks;  // JSON should have a "banks": ["Bank1", "Bank2"]

        // Getter
        public List<String> getBanks() {
            return banks;
        }
        
        // Setter (optional, useful if you want to create objects in code)
        public void setBanks(List<String> banks) {
            this.banks = banks;
        }
    }
    
    // ========== LocationDetails ==========
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
    
    // Cost Sheet
    public static class CostSheetDetails {
        private String basePricePerSqft;
        private String standardTaxRate;

        // Getters
        public String getBasePricePerSqft() { return basePricePerSqft; }
        public String getStandardTaxRate() { return standardTaxRate; }

        // Setters (optional)
        public void setBasePricePerSqft(String basePricePerSqft) { this.basePricePerSqft = basePricePerSqft; }
        public void setStandardTaxRate(String standardTaxRate) { this.standardTaxRate = standardTaxRate; }
    }

 // ========== NEW: AddChargesDetails (Parent container for a list of charges) ==========
    public static class AddChargesDetails {
        private List<Charge> charges;

        // Getter
        public List<Charge> getCharges() { return charges; }
        
        // Setter (optional)
        public void setCharges(List<Charge> charges) { this.charges = charges; }
    }

    // ========== NEW: Charge (Single charge structure) ==========
    public static class Charge {
        private String chargesFor;
        private String category;
        private String costType;
        private String amount;
        private String tax;

        // Getters
        public String getChargesFor() { return chargesFor; }
        public String getCategory() { return category; }
        public String getCostType() { return costType; }
        public String getAmount() { return amount; }
        public String getTax() { return tax; }
        
        // Setters (optional)
        public void setChargesFor(String chargesFor) { this.chargesFor = chargesFor; }
        public void setCategory(String category) { this.category = category; }
        public void setCostType(String costType) { this.costType = costType; }
        public void setAmount(String amount) { this.amount = amount; }
        public void setTax(String tax) { this.tax = tax; }
    }
    
    public static class DeleteOperationDetails {
        private String chargeName;
        private String modalConfirmationAction;

        // Getters and Setters must be present for JSON deserialization
        public String getChargeName() { return chargeName; }
        public String getModalConfirmationAction() { return modalConfirmationAction; }
        // Add setters here if needed by your framework
    }
    
 // ⭐ NEW INNER CLASS: To handle Edit/Delete operations
    public static class ChargeOperations {
    	private DeleteOperationDetails chargeToDelete;
        private String chargeToEdit;
        // Reuses the existing 'Charge' class for the update data structure
        private Charge updatedChargeData;

        // Getters and Setters
        public DeleteOperationDetails getChargeToDelete() { return chargeToDelete; }
        public String getChargeToEdit() {  return chargeToEdit;  }
        public void setChargeToEdit(String chargeToEdit) { this.chargeToEdit = chargeToEdit; }
        public Charge getUpdatedChargeData() { return updatedChargeData;  }
        public void setUpdatedChargeData(Charge updatedChargeData) { this.updatedChargeData = updatedChargeData; }
    }
}

