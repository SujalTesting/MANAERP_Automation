package utils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import base.TestDataRoot;
import dataModels.ProjectDataModal;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonDataReader {

    // ✅ JSON path in resources folder
    private static final String PROJECT_JSON_PATH = "src/test/resources/testdata.json";

    /**
     * Reads the entire JSON file and returns the root object (TestDataRoot)
     */
    public static TestDataRoot getAllTestData() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(PROJECT_JSON_PATH)) {
            return gson.fromJson(reader, TestDataRoot.class);
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read JSON file: " + e.getMessage());
        }
    }

    /**
     * Reads only the 'project' node from JSON and returns ProjectDataModal
     */
    public static ProjectDataModal getProjectData() {
        TestDataRoot root = getAllTestData();
        if (root == null || root.getProject() == null) {
            throw new RuntimeException("Project data is missing in JSON file.");
        }
        return root.getProject();
    }
}
