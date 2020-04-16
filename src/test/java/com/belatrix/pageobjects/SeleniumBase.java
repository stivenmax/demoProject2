package com.belatrix.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class SeleniumBase {
	
	private WebDriver driver;
	String chromepath = "./src/test/resources/chromedriver.exe";
	
	public SeleniumBase(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebDriver ChromeDriverConnection() {
		System.setProperty("webdriver.chrome.driver", chromepath);
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		return driver;
	}
	
	public WebElement findElement(By locator) {
		return driver.findElement(locator);
	}
	
	public String getText(By locator) {
		return driver.findElement(locator).getText();
	}
	
	public void SelectElement (By locator, String text) {
		WebElement dropDowm = driver.findElement(locator);
		Select select = new Select(dropDowm);
		select.selectByVisibleText(text);
	}
	
	public void clickOnElement(By locator) {
		driver.findElement(locator).click();
	}
	
	public void type(By locator, String text) {
		driver.findElement(locator).sendKeys(text);
	}
	
	public boolean isDisplayed(By locator) {
		try {
	      return driver.findElement(locator).isDisplayed();
		}catch(org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}
	
	public boolean isSelected(By locator) {
		return driver.findElement(locator).isSelected();
	}
	
	public void visit(String url) {
		driver.get(url);
	}
	
	public void clear(By locator) {
		driver.findElement(locator).clear();
	}
	
	public WebDriver getDriver() {
		return driver;
	}

}
