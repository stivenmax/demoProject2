package com.belatrix.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.belatrix.exceptions.PayUTimeoutException;

/**
 * Utility class to use different features of the WebDriver API. Based on the
 * {@link Script} class
 * 
 * @author <a href="mailto:jeanpaul.manjarres@payulatam.com">Jean Paul Manjarres
 *         Correal</a><br/>
 *         19/02/2014
 * 
 */
public final class WebDriverUtils {

	/** Class logger */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WebDriverUtils.class);

	/**
	 * Returns a query object based on a locator. The locator could use the
	 * following syntax: <br/>
	 * //XPATH <br/>
	 * ID=[value] <br/>
	 * NAME=[value] <br/>
	 * CSS=[value] <br/>
	 * LINK=[value] <br/>
	 * CLASS=[value] <br/>
	 * <br/>
	 * If none of the previous sintax is used, it will try to find using the
	 * locator as name o id in XPath
	 * 
	 * @param locator
	 * @return {@link By}
	 */
	public static By getBy(String locator) {

		By find = null;

		if (locator.startsWith("//") || locator.startsWith("(//")) {
			find = By.xpath(locator);
		} else if (locator.contains("=")) {

			String[] items = locator.split("=");
			String by = items[0].toUpperCase().trim();

			if (items.length > 1) {

				if (by.equals("ID")) {
					find = By.id(items[1]);
				} else if (by.equals("NAME")) {
					find = By.name(items[1]);
				} else if (by.equals("CSS")) {
					find = By.cssSelector(items[1]);
				} else if (by.equals("LINK")) {
					find = By.linkText(items[1]);
				} else if (by.equals("CLASS")) {
					find = By.className(items[1]);
				} else {
					LOGGER.error("Search option not found [{}] ", by);
				}
			}
		} else {

			find = By.xpath("//*[@name='" + locator + "' or @id='" + locator
					+ "']");
		}
		return find;
	}

	/**
	 * Executes a Javascript and return an object if it exists
	 * 
	 * @param driver
	 *            {@link WebDriver}
	 * @param javascript
	 *            {@link String}
	 * @param element
	 *            {@link WebElement} Could be null
	 * @return {@link Object}
	 * @throws Exception
	 */
	public static Object evalJS(WebDriver driver, String javascript,
			WebElement element) {

		if (driver == null) {
			LOGGER.warn("evalJS()--> Driver instance is NULL ");
			return null;
		}

		if (Strings.isNullOrEmpty(javascript)) {
			LOGGER.warn("evalJS()--> Javascript is NULL or empty");
			return null;
		}

		Object retObject = null;
		JavascriptExecutor executor = (JavascriptExecutor) driver;

		try {
			retObject = element == null ? executor.executeScript(javascript)
					: executor.executeScript(javascript, element);
		} catch (Exception e) {
			LOGGER.error("evalJS()--> Error executing javascript:", e);
		}
		return retObject;
	}

	/**
	 * Get the browser current URL
	 * 
	 * @return {@link String} the current URL
	 * 
	 */
	public static String getCurrentURL(WebDriver driver) {

		if (driver == null) {
			LOGGER.warn("getCurrentURL()--> Driver instance is NULL ");
			return StringUtils.EMPTY;
		}

		String url = null;
		try {
			url = driver.getCurrentUrl();
		} catch (Exception e) {
			LOGGER.error(
					"getCurrentURL()--> Error Trying to get the current url: ",
					e);
		}
		return url;
	}

	/**
	 * Returns the whole content of a web page. <br/>
	 * Searches for the body tag in the page
	 * 
	 * @param driver
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentPageText(WebDriver driver) {

		if (driver == null) {
			LOGGER.warn("getCurrentPageText()--> Driver instance is NULL ");
			return StringUtils.EMPTY;
		}

		// defaultBrowser();
		// String html = driver.getPageSource();

		String html = null;

		try {
			WebElement body = driver.findElement(By.xpath("/html/body"));
			html = body.getText();
		} catch (Exception e) {
			LOGGER.error("getCurrentPageText()--> Exception: ", e);
			html = StringUtils.EMPTY;
		}
		return html;
	}

	/**
	 * Return the text of a element
	 * 
	 * @param locator
	 *            - Id of element
	 * @return String - text of element
	 * @throws Exception
	 * 
	 */
	public static String getTextElement(WebDriver driver, String locator) {

		if (driver == null) {
			LOGGER.warn("getCurrentPageSource()--> Driver instance is NULL ");
			return StringUtils.EMPTY;
		}

		WebElement element = getWebElement(driver, locator, 60);

		LOGGER.debug("Capturing text from element [{}]", locator);

		String result = "";
		if (element.getTagName().equals("input")) {
			if (element.getAttribute("type").equals("select")) {
				result = new Select(element).getAllSelectedOptions().get(0)
						.getText();
			} else if (element.getAttribute("type").equals("checkbox")) {
				result = element.getAttribute("checked");
			} else {
				result = element.getAttribute("value");
			}
		} else {
			result = element.getText();
		}
		return result;
	}

	/**
	 * Returns the whole content of a web page. <br/>
	 * Searches for the body tag in the page
	 * 
	 * @param driver
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentPageSource(WebDriver driver) {

		if (driver == null) {
			LOGGER.warn("getCurrentPageSource()--> Driver instance is NULL ");
			return StringUtils.EMPTY;
		}

		String html = null;

		try {
			html = driver.getPageSource();
		} catch (Exception e) {
			LOGGER.error("getCurrentPageSource()--> Exception: ", e);
			html = StringUtils.EMPTY;
		}
		return html;
	}

	/**
	 * Count the elements that match a specific locator
	 * 
	 * @param locator
	 *            See the {@link WebDriverUtils#getBy(String)} method
	 * @return int the count of elements
	 * @throws Exception
	 * 
	 */
	public static int countElements(WebDriver driver, String locator) {

		if (driver == null) {
			LOGGER.warn("countElements()--> Driver instance is NULL ");
			return 0;
		}

		if (Strings.isNullOrEmpty(locator)) {
			LOGGER.warn("countElements()--> locator is NULL or empty");
			return 0;
		}

		// defaultBrowser();
		int total = 0;
		try {
			By find = getBy(locator);
			if (find != null) {
				total = driver.findElements(find).size();
			}
		} catch (Exception e) {
			LOGGER.error(
					"countElements()--> error while counting elements with locator [{}]. Error [{}]",
					locator, e.getMessage());
		}
		return total;
	}

	/**
	 * Gets a web element, use a timeout
	 * 
	 * @param driver
	 *            {@link WebDriver} the web driver to use
	 * @param locator
	 *            {@link String}. Check for {@link #getBy(String)}
	 * @param timeoutSeconds
	 * @return
	 */
	public static WebElement getWebElement(WebDriver driver, String locator,
			final long timeoutSeconds) {

		final By find = getBy(locator);
		WebElement currentElement = null;

		LOGGER.trace("Looking for element [{}] ", locator);

		if (find != null) {

			try {
				if (timeoutSeconds > 01) {
					// defaultBrowser();
					Wait<WebDriver> wait = new WebDriverWait(driver,
							timeoutSeconds);
					currentElement = wait.until(WebDriverUtils
							.visibilityOfElementLocated(find));
				} else {
					currentElement = driver.findElement(find);
				}
			} catch (Exception e) {
				LOGGER.error(
						"getWebElement()--> Error while finding an element. Locator[{}] - Error[{}]: ",
						locator, e);
			}

		}
		return currentElement;
	}

	/**
	 * Checks if an object is Visible and enabled.
	 * 
	 * @param locator
	 * 
	 * @return ExpectedCondition<WebElement>
	 * 
	 */
	public static ExpectedCondition<WebElement> visibilityOfElementLocated(
			final By locator) {

		return new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {

				WebElement toReturn = driver.findElement(locator);
				if (toReturn.isDisplayed() && toReturn.isEnabled()) {
					return toReturn;
				}
				return null;
			}
		};
	}

	public static ExpectedCondition<WebElement> notVisibilityOfElementLocated(
			final By locator) {

		return new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {

				WebElement toReturn = driver.findElement(locator);
				if (!toReturn.isDisplayed()) {
					return toReturn;
				}
				return null;
			}
		};
	}
	
	public static void waitForElementIsNotVisible(WebDriver driver, String locator)
			throws Exception {

		int timeoutSeconds = 40;
		final By find = getBy(locator);

		LOGGER.debug("Waiting for this element is not visible [{}] ", locator);

		if (find != null) {

			try {
				if (timeoutSeconds > 0l) {
					Wait<WebDriver> wait = new WebDriverWait(driver,
							timeoutSeconds);
					wait.until(WebDriverUtils.notVisibilityOfElementLocated(find));
				} else {
					driver.findElement(find);
				}
			} catch (Exception e) {
				LOGGER.error(
						"waitForElementPresent()--> Error while waiting for element not visible. Locator[{}] - Error[{}]: ",
						locator, e);
			}
		}
	}
	
	
	
	/**
	 * Wait for an alert modal window
	 * 
	 * @return {@link Alert}
	 */
	public static Alert waitForAlert(WebDriver driver, long alertTimeout) {

		Wait<WebDriver> wait = new WebDriverWait(driver, alertTimeout);
		Alert alert = null;
		alert = wait.until(new ExpectedCondition<Alert>() {

			@Override
			public Alert apply(WebDriver driver) {

				Alert result = driver.switchTo().alert();
				// result.getText();
				return result;
			}
		});

		return alert;
	}

	/**
	 * Wait for an alert modal window and Accepts it
	 * 
	 * @param driver
	 * @param alertTimeout
	 * @throws Exception
	 */
	public static void verifyAndAcceptAlert(WebDriver driver, long alertTimeout) {

		final Alert alert = WebDriverUtils.waitForAlert(driver, alertTimeout);
		if (alert != null) {
			alert.accept();
		}
	}

	/**
	 * Aguara da pagina carregar. Status: Complete
	 * 
	 * @return void
	 * @throws Exception
	 */
	public static void waitForCurrentPageToLoad(final WebDriver driver,
			int loadPageTimeout) throws PayUTimeoutException {

		final Thread task = new Thread(new Runnable() {

			public void run() {

				for (;;) {

					Object object = null;
					try {

						object = WebDriverUtils
								.evalJS(driver,
										"return document.readyState ? 'loading' != document.readyState : true",
										null);

						if (object != null && (Boolean) object) {
							break;
						}
						Thread.sleep(500);

						// if (TestApplication.isExitTimeOut()) {
						// break;
						// }

					} catch (Exception e) {
						LOGGER.error(
								"waitForCurrentPageToLoad()--> Exception ", e);
					}
				}
			}
		});

		task.setName(task.getName() + "-WaitPageLoad");

		waitExecuteCommand(task, loadPageTimeout);
	}

	/**
	 * Wait for a task in other thread to finish or the timeout, the one that
	 * happens first.
	 * 
	 * @param thread
	 *            {@link Thread} to execute
	 * @param timeout
	 *            the timeout time in seconds. Cannot be negative
	 * @throws PayUTimeoutException
	 *             if the timeout is reached
	 */
	public static void waitExecuteCommand(Thread thread, int timeout)
			throws PayUTimeoutException {

		if (thread == null) {
			LOGGER.warn("waitExecuteCommand()--> Thread sent is null");
			return;
		}
		if (timeout < 0) {
			LOGGER.warn("waitExecuteCommand()--> invalid timeout [{}]", timeout);
			return;
		}

		try {

			thread.start();
			thread.join(timeout);

			Calendar endTime = Calendar.getInstance();
			endTime.add(Calendar.SECOND, timeout);

			while (thread.isAlive()) {

				// If the timeout is reached
				if (endTime.before(Calendar.getInstance())) {
					thread.interrupt();
					throw new PayUTimeoutException("Timeout reached: "
							+ timeout + " in Thread: " + thread.getName(), null);
				}
				Thread.sleep(500);
			}

		} catch (Exception e) {
			LOGGER.error("waitExecuteCommand()--> unexpected exception: [{}]",
					e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Obtains the parameter from a GET url
	 * http://stackoverflow.com/questions/5902090
	 * /how-to-extract-parameters-from-a-given-url TODO: Check
	 * 
	 * @param url
	 * @return
	 */
	public static Map<String, List<String>> getQueryParams(String url) {

		try {
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			String[] urlParts = url.split("\\?");
			if (urlParts.length > 1) {
				String query = urlParts[1];
				for (String param : query.split("&")) {
					String[] pair = param.split("=");
					String key = URLDecoder.decode(pair[0], "UTF-8");
					String value = "";
					if (pair.length > 1) {
						value = URLDecoder.decode(pair[1], "UTF-8");
					}

					List<String> values = params.get(key);
					if (values == null) {
						values = new ArrayList<String>();
						params.put(key, values);
					}
					values.add(value);
				}
			}

			return params;
		} catch (UnsupportedEncodingException ex) {
			LOGGER.warn("getQueryParams()--> Unsupported encoding. Ex: [{}]",
					ex.getMessage());
			return null;
		}
	}

	/**
	 * Wait for specific element
	 * 
	 * @param driver
	 * @param locator
	 * @throws Exception
	 */
	public static void waitForElementPresent(WebDriver driver, String locator)
			throws Exception {

		int timeoutSeconds = 40;
		final By find = getBy(locator);

		LOGGER.debug("Waiting for this element [{}] ", locator);

		if (find != null) {

			try {
				if (timeoutSeconds > 0l) {
					Wait<WebDriver> wait = new WebDriverWait(driver,
							timeoutSeconds);
					wait.until(WebDriverUtils.visibilityOfElementLocated(find));
				} else {
					driver.findElement(find);
				}
			} catch (Exception e) {
				LOGGER.error(
						"waitForElementPresent()--> Error while finding an element. Locator[{}] - Error[{}]: ",
						locator, e);
			}
		}
	}

	/**
	 * Wait for specific text
	 * 
	 * @param driver
	 * @param expectedText
	 * @throws Exception
	 */
	public static void waitForTextPresent(WebDriver driver,
			final String expectedText) throws Exception {

		int timeoutSeconds = 60;

		LOGGER.trace("Looking for text [{}] ", expectedText);

		Wait<WebDriver> wait = new WebDriverWait(driver, timeoutSeconds);

		Boolean isPresent = wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {

				final Boolean result = driver.getPageSource().contains(
						expectedText);
				return Boolean.TRUE.equals(result) ? Boolean.TRUE : null;
			}
		});

		if (!isPresent) {
			LOGGER.debug(
					"waitForTextPresent()--> Text is not present. Text[{}] ",
					expectedText);
		} else {
			LOGGER.debug("waitForTextPresent()--> Text is present. Text[{}] ",
					expectedText);
		}

	}

	/**
	 * Method to verify if a element is enable
	 * 
	 * @param actualText
	 * @param expectedText
	 * @return
	 * @throws Exception
	 */
	public static boolean waitForEnableElement(WebDriver driver,
			final String locator) throws Exception {

		final long timeoutSeconds = 20;

		LOGGER.debug("Waiting for this element is enabled buajaja [{}] ",
				locator);

		Wait<WebDriver> wait = new WebDriverWait(driver, timeoutSeconds);

		Boolean isEnable = wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				boolean result = false;

				try {

					By find = getBy(locator);
					if (find != null) {

						WebElement element = getWebElement(driver, locator,
								timeoutSeconds);

						result = element.isEnabled() == true ? true : false;

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				return Boolean.TRUE.equals(result) ? Boolean.TRUE : null;

			}
		});

		if (!isEnable) {
			LOGGER.debug(
					"waitForEnabledElement()--> Element is not enabled. Element[{}] ",
					locator);
		} else {
			LOGGER.debug(
					"waitForEnabledElement()--> Element is enabled. Element[{}] ",
					locator);
		}
		return isEnable;

	}

	public static boolean existsElement(WebDriver driver, String locator)
			throws Exception {

		boolean found = false;
		boolean generateException = true;
		LOGGER.debug(
				"Verifying that element [{}] exists. Generate exception [{}]",
				locator, generateException);
		// if (getWebElement(locator, false) == null) {

		if (getWebElement(driver, locator, 60) == null) {

			if (generateException) {
				throw new Exception("Objeto " + locator + " nao encontrado ");
			}
		} else {
			found = true;
		}
		return found;
	}

	public static void waitForPageTitle(WebDriver driver, String titlePage) {

		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.titleContains(titlePage));
	}

	public static int countNumberOfElements(WebDriver driver, String locator) {

		List<WebElement> allElements = driver.findElements(getBy(locator));

		int i = 0;
		for (WebElement Element : allElements) {
			i = i + 1;

			LOGGER.debug("Verifying that element [{}]", Element.getTagName());
			LOGGER.debug("Verifying that element [{}]", Element.getText());
		}

		LOGGER.debug("Total objects found [{}]", i);
		return i;
	}

	/**
	 * wait until the item is not this visible.
	 * 
	 * @throws Exception
	 * 
	 */
	public static boolean waitUntilElementIsNoPresent(WebDriver driver,
			final String locator) throws Exception {

		int timeoutSeconds = 60;

		LOGGER.debug("Waiting for this element is not present [{}] ", locator);

		Wait<WebDriver> wait = new WebDriverWait(driver, timeoutSeconds);

		Boolean isPresent = wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				boolean result = false;

				try {

					By find = getBy(locator);
					if (find != null) {

						result = driver.findElements(find).isEmpty() == true ? true
								: false;

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				return Boolean.TRUE.equals(result) ? Boolean.TRUE : null;

			}
		});

		if (!isPresent) {
			LOGGER.debug(
					"waitUntilElementIsNoPresent()--> Element is not present. Element[{}] ",
					locator);
		} else {
			LOGGER.debug(
					"waitUntilElementIsNoPresent()--> Element is present. Element[{}] ",
					locator);
		}
		return isPresent;

	}

	public static void selectWindowsByUrl(WebDriver driver, String url)
			throws Exception {
		int timeOut = 60;
		LOGGER.debug("Select page " + url);
		boolean found = false;
		long endTime = (new Date()).getTime() + (timeOut * 1000);
		try {
			while (!found) {
				if ((new Date()).getTime() >= endTime) {
					throw new Exception("Page not found!.");
				}
				for (String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle);
					int i = 0;
					while (driver.getCurrentUrl().equals("about:blank")) {
						if (i > 30) {
							throw new Exception("Not load Page.");
						}
						Thread.sleep(1000);
					}
					waitForCurrentPageToLoad(driver, timeOut);
					if (driver.getCurrentUrl().contains(url)) {
						found = true;
						break;
					}
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		if (!found) {
			throw new Exception("Error to find the page with url " + url);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param x
	 * @param y
	 */
	public static void Scroll(WebDriver driver, int x, int y) {

		StringBuilder function = new StringBuilder().append("scroll(")
				.append(x).append(",").append(y).append(");");

		evalJS(driver, function.toString(), null);
	}
}