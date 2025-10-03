import org.testng.annotations.Test;

import base.BaseClass;

public class TestScript extends BaseClass{

	@Test(priority=1)
	public void testLogin() {
		
		test = extent.createTest("Login Test");
		
	}
	
}
