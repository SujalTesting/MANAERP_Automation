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

    
    public ProjectDataModal() {} 

    // Public getters
    public BasicDetails getBasicDetails() { return basicDetails; }
    public AreaConverter getProjectExtent() { return projectExtent; }
    public AreaConverter getSaleableArea() { return saleableArea; }
    public ApprovalDetails getApprovals() { return approvals; } 
    public BankDetails getBankDetails() { return bankDetails; } // <-- NEW
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

}