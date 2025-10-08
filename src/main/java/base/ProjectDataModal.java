package base;

/**
 * Data model for a single Project Creation test run, deserialized from JSON.
 * Keys in this class must match the keys in the JSON file.
 */
public class ProjectDataModal {
    // Basic Details
    private String testId;
    private String projectName;
    private String projectType; 
    private String projectExtent;
    private String saleableArea;
    private String projectUrl;
    private String marketedBy;
    
    // Approvals & Compliance
    private String planningApprovalRequired; // Yes/No
    private String planningAuthority;
    private String approvalNumber;
    private String reraRequired;
    private String reraNumber;
    
    // Banks & Location
    private String bankAccount;
    private String addressDetail;
    private String pincode; 
    private String expectedCity; // Used for Pincode Sync Validation

    private boolean isNegativeTest; 

    // Constructor required by most JSON libraries (like Jackson or Gson)
    public ProjectDataModal() {} 

    // Public getters for the Test Class
    public String getTestId() { return testId; }
    public String getProjectName() { return projectName; }
    public String getProjectType() { return projectType; }
    public String getProjectExtent() { return projectExtent; }
    public String getSaleableArea() { return saleableArea; }
    public String getProjectUrl() { return projectUrl; }
    public String getMarketedBy() { return marketedBy; }
    public String getPlanningApprovalRequired() { return planningApprovalRequired; }
    public String getPlanningAuthority() { return planningAuthority; }
    public String getApprovalNumber() { return approvalNumber; }
    public String getReraRequired() { return reraRequired; }
    public String getReraNumber() { return reraNumber; }
    public String getBankAccount() { return bankAccount; }
    public String getAddressDetail() { return addressDetail; }
    public String getPincode() { return pincode; }
    public String getExpectedCity() { return expectedCity; }
    public boolean isNegativeTest() { return isNegativeTest; }
}
