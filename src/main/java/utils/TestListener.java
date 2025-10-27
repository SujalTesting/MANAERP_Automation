package utils;

import base.BaseClass;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

// ⭐ Listener: Handles Extent logging and screenshots automatically
public class TestListener extends BaseClass implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testDesc = result.getMethod().getDescription() != null
                ? result.getMethod().getDescription()
                : testName;

        ExtentTest testInstance = ExtentTestManager.startTest(testName, testDesc);

        Object currentClass = result.getInstance();
        if (currentClass instanceof BaseClass) {
            ((BaseClass) currentClass).test = testInstance; // ✅ Works since test is public
        }

        testInstance.log(Status.INFO, "Starting Test: " + testName + " - " + testDesc);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "✅ Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        test.log(Status.FAIL, "❌ Test Failed: " + result.getThrowable().getMessage());
        test.log(Status.FAIL, result.getThrowable());

        try {
            String screenshotPath = captureScreenshot(result.getMethod().getMethodName());
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        } catch (IOException e) {
            test.log(Status.WARNING, "⚠️ Could not attach screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(
                Status.SKIP,
                "⚠️ Test Skipped: " +
                        (result.getThrowable() != null ? result.getThrowable().getMessage() : "No reason provided")
        );
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentTestManager.getExtent().flush();
    }

    @Override public void onStart(ITestContext context) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onTestFailedWithTimeout(ITestResult result) { onTestFailure(result); }

    // 📸 Screenshot helper
    private String captureScreenshot(String methodName) throws IOException {
        if (getDriver() == null) return null;

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = System.getProperty("user.dir") + "/test-output/screenshots/";
        Files.createDirectories(Paths.get(screenshotDir));

        String screenshotPath = screenshotDir + methodName + "_" + timestamp + ".png";
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        Files.copy(srcFile.toPath(), Paths.get(screenshotPath));

        return screenshotPath;
    }
    
    
}
