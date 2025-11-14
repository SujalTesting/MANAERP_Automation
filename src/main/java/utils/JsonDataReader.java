package utils;

import java.io.FileReader;
import java.io.File;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import base.TestDataRoot;
import dataModels.ProjectDataModal; // NOTE: This import is needed because TestDataRoot uses ProjectDataModal

/**
 * Utility class to read and parse JSON test data files.
 * This class correctly handles the top-level 'project' wrapper using TestDataRoot
 * and uses ClassLoader for platform-independent file path resolution,
 * which fixes the FileNotFoundException experienced on your system.
 */
public class JsonDataReader {

    // Define file names relative to the 'testdata' folder within 'resources'
    private static final String RESOURCE_SUBFOLDER = "";
    private static final String TEST_DATA_FILE = "testdata.json"; // Changed from ProjectData.json to match user's input
    private static final String UPDATE_PROJECT_DATA_FILE = "UpdateProject.json";

    /**
     * Retrieves the initial project creation data root object.
     * @return The TestDataRoot instance.
     */
    public static TestDataRoot getAllTestData() {
        return readJsonFile(TEST_DATA_FILE, TestDataRoot.class);
    }

    /**
     * Retrieves the project update data root object.
     * @return The TestDataRoot instance containing the update details.
     */
    public static TestDataRoot getAllUpdateTestData() {
        return readJsonFile(UPDATE_PROJECT_DATA_FILE, TestDataRoot.class);
    }

    /**
     * Generic private method to read and deserialize a JSON file.
     * This method uses ClassLoader to find the file from the classpath, ensuring 
     * the code works regardless of the execution environment (e.g., Windows/Linux).
     * @param <T> The class type to deserialize the JSON into (e.g., TestDataRoot).
     * @param fileName The name of the JSON file.
     * @param classType The Class object of type T.
     * @return The deserialized object of type T, or null on failure.
     */
    private static <T> T readJsonFile(String fileName, Class<T> classType) {
        
        // Construct the full path relative to the 'resources' root
        // If your files are directly under src/test/resources/ (no 'testdata' subfolder),
        // change RESOURCE_SUBFOLDER to "" (empty string).
        String resourcePath = RESOURCE_SUBFOLDER + fileName; 
        
        // Use ClassLoader to reliably find the resource file
        URL resource = JsonDataReader.class.getClassLoader().getResource(resourcePath);

        if (resource == null) {
            System.err.println("FATAL PATH ERROR: Resource not found. Please verify the file exists at 'src/test/resources/" + resourcePath + "'");
            return null;
        }

        try {
            // Convert the URL to a File object for FileReader (Platform-independent)
            File file = new File(resource.toURI());
            
            try (FileReader reader = new FileReader(file)) {
                
                Gson gson = new Gson();
                T dataObject = gson.fromJson(reader, classType);
                
                if (dataObject == null) {
                    System.err.println("ERROR: Deserialization failed for file: " + fileName);
                    return null;
                }
                
                return dataObject;
                
            } catch (JsonSyntaxException e) {
                System.err.println("FATAL GSON ERROR: Invalid JSON syntax or structure mismatch for file: " + fileName);
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("FATAL IO/URI ERROR while processing file: " + fileName);
            e.printStackTrace();
        }

        return null;
    }
}
