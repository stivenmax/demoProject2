package com.belatrix.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MenuPage extends AbstractWebPagePageObjectBase{
	
	//constructor
	public MenuPage(WebDriver driver, String url) {
		super(driver, url);
       // TODO Auto-generated constructor stub
	}
	
	//locators
	String menuLanguageLocator = "//a[@id= 'gh-eb-Geo-a-default']/span[2]";
	String englishLanguageLocator = "//li[@lang = 'en-US']";
	String spanishLanguageLocator = "//li[@lang = 'en-US']";
	
	//This method change the language.
	public void selectLanguage(String language) {
		open();
		if(language.equals("SPANISH") && getTextElement(menuLanguageLocator).equals("English")) {
			//open();
			clickOnElement(menuLanguageLocator);
			clickOnElement(spanishLanguageLocator);
		
		}else if(language.equals("ENGLISH") && getTextElement(menuLanguageLocator).equals("Español")) {
			//open();
			clickOnElement(menuLanguageLocator);
			clickOnElement(englishLanguageLocator);
		}	
	}

}
