package base;

import dataModels.ProjectDataModal;

/**
 * MASTER DATA MODAL: Acts as the root container for all feature-specific data models.
 * Used to deserialize JSON files that wrap the core data inside a 'project' node:
 * { "project": { ... } }
 * As you add features (Sales, HR), you just add new private variables and getters/setters here.
 */
public class TestDataRoot {
    
    // Feature-specific data objects. The field name 'project' must match the key in your JSON file.
    private ProjectDataModal project; 
    // private SalesData sales; // Example for future use

    // Public Getters for Test Scripts
    public ProjectDataModal getProject() { 
        return project; 
    }
    
    // Setter required by Gson to inject the deserialized ProjectDataModal object
    public void setProject(ProjectDataModal project) {
        this.project = project;
    }
    
    // public SalesData getSales() { return sales; } 
    // public void setSales(SalesData sales) { this.sales = sales; } // Example setter
}
