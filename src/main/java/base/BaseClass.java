package base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import page.Login;

public class BaseClass {
    public WebDriver driver;
    public static ExtentReports extent;
    public static ExtentTest test;

    @BeforeSuite
    public void setupReport() {
    	extent = ExtentReportManager.getInstance();
    	
    	driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://manaerp-qa.netlify.app//");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        

        Login loginPage = new Login(driver);
        
        loginPage.enterUsername("shub@braincellinfotech.ai");
        loginPage.enterPassword("redefine@999");
        loginPage.clickRememberMe();
        loginPage.clickSubmitButton();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail("Test Failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed");
        } else {
            test.skip("Test Skipped");
        }
    }

//    @AfterClass
//    public void closeBrowser() {
//        driver.quit();
//    }

    @AfterSuite
    public void flushReport() {
        extent.flush();
    }
}

