package base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utils.ExtentTestManager;

public class BaseClass {

    // ✅ Thread-safe WebDriver for parallel execution
    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    // ✅ Shared ExtentReports instance
    public static ExtentReports extent;

    // ✅ Current test instance (public so listener can access)
    public ExtentTest test;

    // -------------------------------------------------------------------------------------
    // 🔹 Getters
    public WebDriver getDriver() {
        return driverThread.get();
    }

    public static ExtentReports getExtent() {
        return extent;
    }
    // -------------------------------------------------------------------------------------

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        // --- Initialize ExtentReports ---
        extent = ExtentReportManager.createInstance();
        ExtentTestManager.setExtent(extent);

        // --- Initialize WebDriver ---
        WebDriver driver = new ChromeDriver();
        driverThread.set(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://manaerp-qa.netlify.app/");

        // --- Login Step ---
        try {
            Login loginPage = new Login(driver);
            loginPage.enterUsername("admin-qa@braincellinfotech.ai");
            loginPage.enterPassword("redefine@123");
            loginPage.clickRememberMe();
            loginPage.clickSubmitButton();
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }
    }

    
    @AfterSuite(alwaysRun = true)
    public void teardownSuite() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }

        if (extent != null) {
            extent.flush();
        }
    }
}
