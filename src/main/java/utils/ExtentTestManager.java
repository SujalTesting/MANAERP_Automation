package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

    private static Map<Long, ExtentTest> extentTestMap = new HashMap<>();
    private static ExtentReports extent;

    // Set ExtentReports (called once from BaseClass)
    public static synchronized void setExtent(ExtentReports extentInstance) {
        extent = extentInstance;
    }

    public static synchronized ExtentReports getExtent() {
        return extent;
    }

    // Start new test (creates ExtentTest for the current thread)
    public static synchronized ExtentTest startTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        extentTestMap.put(Thread.currentThread().getId(), test);
        return test;
    }

    // Retrieve the test linked to the current thread
    public static synchronized ExtentTest getTest() {
        return extentTestMap.get(Thread.currentThread().getId());
    }
}
