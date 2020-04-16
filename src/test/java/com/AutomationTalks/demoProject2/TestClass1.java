package com.AutomationTalks.demoProject2;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TestClass1 extends AbstractSearchIntegrationTest {
	
	@Test
	public void test1() {
		getDriver().navigate().to("https://www.automationtalks.com/");
		System.out.println("Test 1 title is "+getDriver().getTitle());
	}
	
	@Test
    public void test2() {
		getDriver().navigate().to("https://www.automationtalks.com/");
		System.out.println("Test 2 title is "+getDriver().getTitle());
	}

	@Test
    public void test3() {
		getDriver().navigate().to("https://www.automationtalks.com/");
		System.out.println("Test 3 title is "+getDriver().getTitle());
    }
	
	@AfterMethod
	public void quit() {
		getDriver().quit();
	}

}
