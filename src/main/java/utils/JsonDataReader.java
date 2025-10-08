package utils;

import base.ProjectDataModal;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonDataReader {

    /**
     * Reads projectdata.json and returns a ProjectDataModal object.
     * @return ProjectDataModal object with data from the JSON file.
     */
    public static ProjectDataModal getProjectData() {
        String filePath = "src/main/java/utils/projectData.json";
        Gson gson = new Gson();
        ProjectDataModal projectData = null;

        try (Reader reader = new FileReader(filePath)) {
            // Convert JSON file to a Java object
            projectData = gson.fromJson(reader, ProjectDataModal.class);
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        return projectData;
    }
}
