package com.belatrix.pageobjects;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.belatrix.exceptions.PayUTimeoutException;
import com.belatrix.utils.WebDriverUtils;

/**
 * Abstract implementation of a Web Page object.<br/>
 * Extends the {@link AbstractPageObjectBase} adding some methods related to
 * full web pages.
 *
 * @author <a href="mailto:jeanpaul.manjarres@payulatam.com">Jean Paul Manjarres
 *         Correal</a><br/>
 *         19/02/2014
 *
 */
public abstract class AbstractWebPagePageObjectBase extends
		AbstractPageObjectBase {

	/** Class logger */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractWebPagePageObjectBase.class);

	/** URL for this web page */
	private String url;

	/** Page load timeout. Defaults to 5 seconds */
	private int loadPageSecondsTimeout = 30;

	/**
	 * Constructor
	 *
	 * @param driver
	 * @param url
	 */
	public AbstractWebPagePageObjectBase(WebDriver driver, String url) {

		super(driver);
		this.url = url;
		System.out.println(driver);
	}

	/**
	 * Returns the page title.
	 *
	 * @return
	 */
	public String getTitle() {

		return this.driver.getTitle();
	}

	/**
	 * @return the url
	 */
	public String getUrl() {

		return url;
	}

	/**
	 * Open the web page associated to this object
	 *
	 * @return
	 * @throws PayUTimeoutException
	 */
	public AbstractWebPagePageObjectBase open() {

		LOGGER.debug("open()--> Open web page: [{}]", getUrl());
		
		pause(2000);


		//TODO: Review this code.
		// try {
		// this.driver.get(getUrl());
		// WebDriverUtils.waitForCurrentPageToLoad(this.driver,
		// this.loadPageSecondsTimeout);
		// }
		// catch (PayUTimeoutException e) {
		// LOGGER.error("open()--> Error trying to open the page. ");
		// throw new RuntimeException(e);
		// }

		try {
			Thread task = new Thread(new Runnable() {

				public void run() {

					AbstractWebPagePageObjectBase.this.driver.get(getUrl());
				}
			});
			WebDriverUtils
					.waitExecuteCommand(task, this.loadPageSecondsTimeout);
		} catch (PayUTimeoutException e) {
			LOGGER.error("open()--> Error trying to open the page. ");
			throw new RuntimeException(e);
		}

		return this;
	}

	/**
	 * Wait for this page to load
	 */
	public void waitForThisPageToLoad() {

		WebDriverUtils.waitForCurrentPageToLoad(this.driver,
				this.loadPageSecondsTimeout);
	}

	/*
	public void replaceHTML(By by, String html) {
	      WebElement e = driver.findElement(by);
	     ((JavascriptExecutor) driver).executeScript("arguments[0].innerHTML='" + StringEscapeUtils.escapeEcmaScript(html) + "'", e);
	}
	*/
	
	/**
	 * Funcionalidad para tomar evidencia de la ejecucion de un caso de prueba
	 *
	 * @param fileName
	 * @throws Exception
	 */
	public void printScreenEvidence(String filePath, String fileName) {

		File directory = new File(filePath);

		try {
			if (directory.isDirectory()) {
				/*For remote capture...*/
				//driver = new Augmenter().augment(driver);
				/*For remote capture...*/
				File aux = ((TakesScreenshot) driver)
						.getScreenshotAs(OutputType.FILE);

				FileUtils.copyFile(aux, new File(directory.getAbsolutePath()
						+ File.separator + fileName + ".png"));
			} else {
				LOGGER.error("FilePath specified is not directory. [{}]",
						filePath);
				throw new IOException(
						"ERROR : FilePath specified is not directory : "+filePath);
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new RuntimeException("Error while trying to get printscreen", ex);
		}

		LOGGER.debug("Image generated succesfully. [{}/{}.png] ", filePath,
				fileName);
	}

}