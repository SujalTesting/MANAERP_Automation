import org.testng.annotations.Test;

import base.BaseClass;
import page.Home;

public class TestScript extends BaseClass{

	@Test(priority=1)
    public void testLoginAndVerifyHomePage() throws InterruptedException {
        test = extent.createTest("Login and Home URL Verification Test");
        
        // Verify URL after login
        Home home = new Home(driver);
        home.checkDashBoardURL();
    }
	
}
