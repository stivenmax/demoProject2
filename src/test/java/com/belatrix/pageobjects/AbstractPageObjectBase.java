package com.belatrix.pageobjects;

import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.belatrix.enums.BrowserType;
import com.belatrix.utils.WebDriverUtils;

/**
 * PageObject base implementation.
 * 
 * @author <a href="mailto:jeanpaul.manjarres@payulatam.com">Jean Paul Manjarres
 *         Correal</a><br/>
 *         19/02/2014
 * 
 */
public abstract class AbstractPageObjectBase {

	/** Class logger */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractPageObjectBase.class);

	/** Browser used to render this page. Defaults firefox */
	private BrowserType browser = BrowserType.FIREFOX;

	/** Timeout for the alert modal windows */
	private long alertTimeout=1;

	/** This page's WebDriver */
	protected WebDriver driver;

	/**
	 * Constructor. Creates the class with a Selenium Driver
	 * 
	 * @param driver
	 */
	public AbstractPageObjectBase(WebDriver driver) {

		this.driver = driver;
	}

	/**
	 * Returns the body content of this web page
	 * 
	 * @return {@link String}
	 */
	public String getBodyContent() {

		return WebDriverUtils.getCurrentPageText(this.driver);
	}

	/**
	 * Returns the page source
	 * 
	 * @return
	 */
	public String getPageSource() {

		return WebDriverUtils.getCurrentPageSource(this.driver);
	}

	/**
	 * Accepts and alert modal window.
	 * 
	 * @return
	 */
	public AbstractPageObjectBase acceptAlert() {

		WebDriverUtils.verifyAndAcceptAlert(driver, this.alertTimeout);
		return this;

	}

	/**
	 * Clears the specified web element. Wait for the element as long as the
	 * timeout reaches.
	 * 
	 * @param locator
	 */
	protected AbstractPageObjectBase clearElement(String locator,
			long timeoutSeconds) {

		WebElement currentElement = WebDriverUtils.getWebElement(driver,
				locator, timeoutSeconds);
		currentElement.clear();

		return this;

	}

	/**
	 * Clears the specified web element, uses no timeout.
	 * 
	 * @param locator
	 */
	protected AbstractPageObjectBase clearElement(String locator) {

		clearElement(locator, 0);
		return this;
	}

	/**
	 * Click a button, selected by a locator
	 * 
	 * @param locator
	 * @return
	 */
	protected AbstractPageObjectBase clickOnElement(String locator) {

		WebElement element = WebDriverUtils.getWebElement(driver, locator, 1);
		WebDriverUtils.evalJS(driver, "arguments[0].focus()", element);

		if (BrowserType.IE.equals(this.browser)) {
			WebDriverUtils.evalJS(driver, "arguments[0].click()", element);
		} else {
			element.click();
		}
		return this;
	}
	
	protected AbstractPageObjectBase moveToElement(String locator) {

		Actions actions = new Actions(driver);
		WebElement mouseOver = driver.findElement(By.xpath(locator));
		actions.moveToElement(mouseOver).moveToElement(mouseOver).doubleClick()
				.build();

		// clickOnElement(locator);
		// for (int i=0; i<10; i++){
		// mouseOver.sendKeys(Keys.TAB);
		// }
		return this;
	}
	
	protected AbstractPageObjectBase clicOnMenuElement(String locatorlist, String locatorItem) {
		//Actions builder = new Actions(driver); WebElement element = driver.findElement(By.linkText("Put your text here")); builder.moveToElement(element).build().perform();
		Actions actions = new Actions(driver);
		WebElement mouseOver = driver.findElement(By.xpath(locatorlist));
		actions.moveToElement(mouseOver).moveToElement(mouseOver).build().perform();
		clickOnElement(locatorItem);

		// clickOnElement(locator);
		// for (int i=0; i<10; i++){
		// mouseOver.sendKeys(Keys.TAB);
		// }
		return this;
	}

	/**
	 * Click a button, selected by a locator
	 * 
	 * @param locator
	 * @return
	 */
	protected AbstractPageObjectBase waitForElementAndClick(String locator,
			long timeout) {

		WebElement element = WebDriverUtils.getWebElement(driver, locator,
				timeout);
		WebDriverUtils.evalJS(driver, "arguments[0].focus()", element);

		if (BrowserType.IE.equals(this.browser)) {
			WebDriverUtils.evalJS(driver, "arguments[0].click()", element);
		} else {
			element.click();
		}
		return this;
	}

	/**
	 * Type text in a web element, selected by a locator
	 * 
	 * @param locator
	 */
	protected AbstractPageObjectBase typeText(String locator, String value) {

		typeText(locator, value, 0);
		return this;
	}

	/**
	 * Type text in a web element, selected by a locator
	 * 
	 * @param locator
	 */
	protected AbstractPageObjectBase typeText(String locator, String value,
			long timeoutSeconds) {

		final WebElement element = WebDriverUtils.getWebElement(driver,
				locator, timeoutSeconds);

		if (BrowserType.IE.equals(this.browser)) {
			element.clear();
		}
		element.sendKeys(value);
		element.sendKeys(Keys.TAB);

		return this;
	}

	protected AbstractPageObjectBase typeTextWithOutTab(String locator, String value,
			long timeoutSeconds) {

		final WebElement element = WebDriverUtils.getWebElement(driver,
				locator, timeoutSeconds);
		element.sendKeys(value);
		return this;
	}
	
	
	/**
	 * Type text in a web element, selected by a locator
	 * 
	 * @param locator
	 */
	protected AbstractPageObjectBase sendKey(String locator, Keys key,
			long timeoutSeconds) {

		final WebElement element = WebDriverUtils.getWebElement(driver,
				locator, timeoutSeconds);

		if (BrowserType.IE.equals(this.browser)) {
			element.clear();
		}

		element.sendKeys(key);

		return this;
	}

	protected AbstractPageObjectBase sendKeys(String locator, CharSequence keys,
			long timeoutSeconds) {

		final WebElement element = driver.findElement(By.cssSelector(locator));
		WebDriverUtils.evalJS(driver, "arguments[0].focus()", element);
		
		if (BrowserType.IE.equals(this.browser)) {
			element.clear();
		}
		
		element.sendKeys(keys);

		return this;
	}
	
	
	
	/**
	 * Returns the current URL.
	 * 
	 * @return
	 */
	public String getCurrentUrl() {

		return WebDriverUtils.getCurrentURL(driver);
	}

	/**
	 * Returns the text of a element.
	 * 
	 * @return
	 */
	public String getTextElement(String locator) {
		this.pause(1000);
		return WebDriverUtils.getTextElement(driver, locator);
	}

	/**
	 * Wait for element
	 * 
	 * @return
	 * @throws Exception
	 */
	public void waitForElementPresent(String locator) throws Exception {
		this.pause(500);
		WebDriverUtils.waitForElementPresent(driver, locator);
	}
	
	/**
	 * Wait for element is not visible
	 * 
	 * @return
	 * @throws Exception
	 */
	public void waitForElementIsNotVisible(String locator) throws Exception {
		this.pause(2000);
		WebDriverUtils.waitForElementIsNotVisible(driver, locator);
	}

	/**
	 * Wait for a specific text
	 * 
	 * @return
	 * @throws Exception
	 */
	public void waitForTextPresent(String expectedText) throws Exception {

		WebDriverUtils.waitForTextPresent(driver, expectedText);
	}

	/**
	 * Wait for a element is enable
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean waitForEnableElement(String locator) throws Exception {

		return WebDriverUtils.waitForEnableElement(driver, locator);

	}

	/**
	 * Wait for a specific text
	 * 
	 * @return
	 * @throws Exception
	 */
	public void existsElement(String locator) throws Exception {

		WebDriverUtils.existsElement(driver, locator);
	}

	/**
	 * Check if the text present in page.
	 * 
	 * @param text
	 *            {@link String} to search
	 * @return boolean
	 */
	public boolean isTextPresent(String text) {

		return driver.getPageSource().contains(text);
	}

	/**
	 * Is the Element in page.
	 * 
	 */
	public boolean isElementPresent(String locator) {

		WebElement element = null;

		try {
			element = driver.findElement(WebDriverUtils.getBy(locator));

			if (element != null) {

				if (element.isDisplayed() && element.isEnabled()) {

					return true;

				}
				return false;

			}

		} catch (NoSuchElementException e) {
			return false;
		}
		return false;
	}

	/**
	 * Is the Element checked in page.
	 * 
	 */
	public boolean isElementChecked(String locator) {

		if (driver.findElement(WebDriverUtils.getBy(locator)).getAttribute(
				"checked") != null) {
			return true;
		} else
			return false;
	}
	
	/**
	 * Is the radioButton checked in page.
	 * 
	 */
	public boolean isRadioButtonChecked(String locator) {

		if (driver.findElement(WebDriverUtils.getBy(locator)).isSelected()){
			return true;
		} else
			return false;
	}
	

	/**
	 * Is the Element displayed.
	 * 
	 */
	public boolean isElementDisplayed(String locator) {

		return driver.findElement(WebDriverUtils.getBy(locator)).isDisplayed();

	}

	/**
	 * Select a specific page.
	 * 
	 * @throws Exception
	 * 
	 */
	public void selectWindowsByUrl(String url) throws Exception {

		WebDriverUtils.selectWindowsByUrl(driver, url);

	}

	public void waitUntilElementIsNoPresent(String locator) throws Exception {

		WebDriverUtils.waitUntilElementIsNoPresent(driver, locator);

	}

	/** return the number of element that have the same locator */
	public int countNumberOfElements(String locator) {

		return WebDriverUtils.countNumberOfElements(driver, locator);

	}

	/**
	 * Wait the specified time (milliseconds)
	 * 
	 * @param waitTime
	 *            in milliseconds
	 * 
	 */
	public void pause(long waitTime) {

		LOGGER.trace("pause()--> Pausing thread for [{}] ms", waitTime);

		try {
			Thread.sleep(waitTime);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * Wait the specified time (milliseconds)
	 * 
	 * @param waitTime
	 *            in milliseconds
	 * 
	 */
	public void waitForPageTitle(String pageTitle) {

		LOGGER.trace("waitForPageTitle()--> Waiting for title for [{}] ms",
				pageTitle);

		WebDriverUtils.waitForPageTitle(driver, pageTitle);
	}

	/**
	 * Selects an element in a html select component by clicking the select and
	 * then the option by text<br>
	 * <p>
	 * <b>this doesnt work with ZK combobox component as is not a select</b>
	 * </p>
	 * 
	 * @param selectLocator
	 *            Locator of the select input component
	 * @param text
	 *            The text to select.
	 * @return {@link AbstractPageObjectBase}
	 * @throws InterruptedException
	 */
	protected AbstractPageObjectBase selectOptionByText(String selectLocator,
			String text) throws InterruptedException {

		// TODO: Fix, should not use pauses, instead wait for the element
		clickOnElement(selectLocator);
		pause(500);
		WebElement element = WebDriverUtils.getWebElement(driver,
				selectLocator, 0);
		Select select = new Select(element);
		pause(500);
		select.selectByVisibleText(text);
		pause(200);
		return this;
	}

	/**
	 * Selects an element in a html select component by clicking the select and
	 * then the option by value<br>
	 * <p>
	 * <b>this doesnt work with ZK combobox component as is not a select</b>
	 * </p>
	 * 
	 * @param selectLocator
	 *            Locator of the select input component
	 * @param value
	 * @return
	 * @throws InterruptedException
	 */
	protected AbstractPageObjectBase selectOptionByValue(String selectLocator,
			String value) throws InterruptedException {

		clickOnElement(selectLocator);
		pause(500);

		WebElement element = WebDriverUtils.getWebElement(driver,
				selectLocator, 0);
		Select select = new Select(element);
		select.selectByValue(value);
		pause(200);
		return this;
	}

	/**
	 * Selects an element in a zk select component by clicking the select and
	 * then the option by value<br>
	 * <p>
	 * <b></b>
	 * </p>
	 * 
	 * @param selectLocator
	 *            Locator of the select input component
	 * @param value
	 * @return
	 * @throws InterruptedException
	 */
	protected AbstractPageObjectBase selectOptionZk(String selectLocator,
			String value) throws InterruptedException {

		String asciiSpaceCode = "&#160;";
		String formatedValue = StringEscapeUtils.unescapeHtml4(value.replace(
				" ", asciiSpaceCode));

		/** Locator for a zk country combobox option */
		String countryZKComboBoxValueLocator = "//div[@class='z-combobox-pp z-combobox-shadow']/table/tbody/tr/td[@class='z-comboitem-text' and text()='%s']";
		this.clickOnElement(selectLocator);
		this.pause(500);
		this.clickOnElement(String.format(countryZKComboBoxValueLocator,
				formatedValue));
		return this;
	}

}