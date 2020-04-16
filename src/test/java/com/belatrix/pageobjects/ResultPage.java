package com.belatrix.pageobjects;

import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.belatrix.utils.Util;


public class ResultPage extends AbstractWebPagePageObjectBase {
	
	//constructor
	public ResultPage(WebDriver driver, String url) {
		super(driver, url);
		// TODO Auto-generated constructor stub
	}
	
	//locators
	String lnkresutlsLocator = "//h1[@class='srp-controls__count-heading']";
	String lstSortLocator = "//div[@id='w9']//*[@class='svg-icon x-flyout-arrow-down']";
	String listItemdAscendantLocator = "//span[contains(text(),'Price + Shipping: lowest first')"
			+ "or contains(text(),'Precio + Envío: más bajo primero')]";
	String listItemdDescendantLocator = "//span[contains(text(),'Price + Shipping: highest first')"
			+ "or contains(text(),'Precio + Envío: más bajo primero')]";
	
	
	//This method print the results
	public void printResults(String language) {
		if(language.equals("SPANISH"))
		System.out.println("Hay " + getTextElement(lnkresutlsLocator));
		else
			System.out.println("There are  " + getTextElement(lnkresutlsLocator));
	}
	
	//This method resturn the locators to the products information
	public ArrayList<String> getProductLocators(int Quantity, String locator) {
		ArrayList<String> productLocators = new ArrayList<String>();
		
		if(locator.equals("NAME")) {
		   for(int i = 1; i <= Quantity; i++) {
			 productLocators.add("//ul[@class='srp-results srp-grid clearfix']/"
					+ "li["+i+"]/div[1]/div[2]/a/h3");
		   }
		}else if(locator.equals("PRICE")) {
		   for(int i = 1; i <= Quantity; i++) {
			 productLocators.add("//ul[@class='srp-results srp-grid clearfix']"
			 		+ "/li["+i+"]/div[1]/div[2]/div[3]/div[1]/span[1]");
		   }
		}else if(locator.equals("SHIPPING")) {
		   for(int i = 1; i <= Quantity; i++) {  
			 productLocators.add("//ul[@class='srp-results srp-grid clearfix']/"
			 		+ "li["+i+"]//span[contains(text(), '+CO') or contains(text(), 'Free')]");
				   }
				}
		return productLocators;
	}
	
	public ArrayList<String> getProductName() {
		ArrayList<String> productLocators = getProductLocators(5, "NAME");
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0; i < productLocators.size(); i++) {
			names.add(getTextElement(productLocators.get(i)));
		}
		return names;
	}
	
	public ArrayList<Float> getPrices() {
		ArrayList<String> productLocators = getProductLocators(5, "PRICE");
		ArrayList<Float> prices = new ArrayList<Float>();
		for(int i = 0; i < productLocators.size(); i++) {
			prices.add(Util.parseTextToNumber(getTextElement(productLocators.get(i))));
		}
		return prices;
	}
	
	public ArrayList<Float> getShippingPrices() {
		ArrayList<String> productLocators = getProductLocators(5, "SHIPPING");
		ArrayList<Float> shippingPrices = new ArrayList<Float>();
		for(int i = 0; i < productLocators.size(); i++) {
			if(getTextElement(productLocators.get(i)).equals("Free International Shipping")) {
			  shippingPrices.add(Float.valueOf(0.0f));
			}else {
				shippingPrices.add(Util.parseTextToNumber(getTextElement(productLocators.get(i))));
			}
		}
		return shippingPrices;
	}
	
	public ArrayList<Float> getTotal() {
		ArrayList<Float> pricesValues = getPrices();
		ArrayList<Float> shippingValues = getShippingPrices();
		ArrayList<Float> total = new ArrayList<Float>();
		Float result;
		for(int i = 0; i < pricesValues.size(); i++) {
			result = pricesValues.get(i).floatValue() + shippingValues.get(i).floatValue();
			total.add(result);
		}
		return total;
	}
	
	public boolean validateOrder(String order) {
		ArrayList<Float> prices = getTotal();
		int i = 0; int j = 1;
		boolean orderResult = false;
		while(j < prices.size()) {
			if(order.equals("ASCENDANT")) {
				if(prices.get(i).floatValue() <= prices.get(j).floatValue()) {
				   orderResult = true;
				}else {
				   orderResult  = false;
				   break;
			    }
	        }else if(order.equals("DESCENDANT")) {
			    if(prices.get(i).floatValue() >= prices.get(j).floatValue()) {
				   orderResult = true;
				}else {    
				   orderResult  = false;
				   break;
		        }
		    }
		i++; j++;
	   }
		return orderResult;
	}
	
	public void sort(String order) {
		try {
			if(order.equals("ASCENDANT")) {
			waitForElementPresent(lstSortLocator);
			clicOnMenuElement(lstSortLocator, listItemdAscendantLocator);
			}else if(order.equals("DESCENDANT")) {
				waitForElementPresent(lstSortLocator);
				clicOnMenuElement(lstSortLocator, listItemdDescendantLocator);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void printProducts() {
		ArrayList<String> names = getProductName();
		ArrayList<Float> prices = getPrices();
		for(int i = 0; i < names.size(); i++) {
		System.out.println("\nProduct name: "+names.get(i)+ " Price: " + prices.get(i));
		}
	}
}
