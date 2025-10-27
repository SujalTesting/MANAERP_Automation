package base;

import dataModels.ProjectDataModal;

/**
 * MASTER DATA MODAL: Acts as the root container for all feature-specific data models.
 * Used to deserialize the entire masterData.json file in one operation.
 * As you add features (Sales, HR), you just add new private variables and getters here.
 */
public class TestDataRoot {
    
    // Feature-specific data objects
    private ProjectDataModal project; 
    // private SalesData sales; // Example for future use
    
    // Constructor required by Gson
    public TestDataRoot() {}

    // Public Getters for Test Scripts
    public ProjectDataModal getProject() { return project; }
    // public SalesData getSales() { return sales; } 
}