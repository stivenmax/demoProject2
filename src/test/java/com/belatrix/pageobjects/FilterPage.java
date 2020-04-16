package com.belatrix.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FilterPage extends AbstractWebPagePageObjectBase {

	public FilterPage(WebDriver driver, String url) {
		super(driver, url);
		// TODO Auto-generated constructor stub
	}

	// locators
	String cbxBrandLocator = "//input[@type = 'checkbox' and @aria-label = 'PUMA']";
	String cbxSizeLocator = "//div[@id='x-refine__group_1__0']/ul/li[5]/div[1] /a";
	String txtBrandLocator = "//input[@id='w4-w12-0[0]']";
	String cbxStateLocator = "//input[@type = 'checkbox' and @aria-label = 'New without tags']";

	public void selectBrand() throws Exception {
		if (isElementPresent(txtBrandLocator)) {
			typeText(txtBrandLocator, "PUMA");
			Thread.sleep(3000);
			if (isElementChecked(cbxBrandLocator))
				System.out.println("The Webelement is selected");
			else
				clickOnElement(cbxBrandLocator);
		} else
			System.out.println("WebElement was not found");

	}

	public void selectSize() {
		try {
			waitForElementPresent(cbxSizeLocator);
			if (isElementChecked(cbxSizeLocator))
				System.out.println("The Webelement is selected");
			else
				clickOnElement(cbxSizeLocator);
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void selectStatus() {
		try {
			
			clickOnElement(cbxStateLocator);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
