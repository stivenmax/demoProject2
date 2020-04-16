package com.belatrix.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage extends AbstractWebPagePageObjectBase {

	// constructor
	public SearchPage(WebDriver driver, String url) {
		super(driver, url);
		// TODO Auto-generated constructor stub
	}

	// locators
	String txtSearchLocator = "//input[@name='_nkw']";
	String btnSearchLocator = "//input[@value='Search']";

	// This method looks for a keyword
	public void searchProduct(String keyword) {
		try {
			waitForElementPresent(txtSearchLocator);
			clearElement(txtSearchLocator);
			typeText(txtSearchLocator, keyword);
			clickOnElement(btnSearchLocator);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
