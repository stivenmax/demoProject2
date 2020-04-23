package com.AutomationTalks.demoProject2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import ch.qos.logback.classic.Level;
import com.google.common.base.Strings;
import com.belatrix.enums.BrowserType;
import com.belatrix.enums.Environment;
//import com.payulatam.testsuites.admin.factories.AdminWebPageObjectFactory;
import com.belatrix.constants.Constants;
import com.belatrix.constants.WebDriverTestEnvironmentConstants;
import com.belatrix.utils.WebDriverConfigUtils;


/**
 * Abstract implementation for all the test. <br/>
 * This class initializes the environment for automated tests.
 * 
 * @author <a href="mailto:ing_stiven@hotmail.com">Stiven Jose Mosquera Puello</a><br/>
 *         13/12/2019
 */
public class AbstractSearchIntegrationTest {

	/** Class logger */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractSearchIntegrationTest.class);

	/** The web driver to use */
	private WebDriver driver;

	/** Factory for the Web Admin Page Objects */
	//private AdminWebPageObjectFactory factory;
	/** The Constant STATE. */
	//private static final String STATE = "ST";

	/**
	 * The context of the web application. By default it uses the Latest
	 * environment
	 */
	private String webContextURL = "https://www.ebay.com/";

	/** The remote hosts to use. Default none. */
	private String remoteHost = "";

	/** The remote hosts to use. Default local. */
	//private String executionType = "local";
	private String executionType = "remote";

	/** The browser to use. Defaults to firefox */
	private String browser = "CHROME";

	/** The headless mode to use. */
	private String headlessMode = "OFF";
	
	/** Evidence path for the execution. Defaults to /var/log/payu/tests/searchProducts*/
	private String evidencePath = "/var/log/payu/tests/searchProducts";

	/** Page load time out. Defaults to 5 */
	private int pageLoadTimeOut = 5;

	/** The resource bundle for the properties */
	private Properties properties;
	
	
	/**
	 * Suite initialization
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeClass() {

		System.setProperty("webdriver.accept.untrusted.certs", "true");
		System.setProperty("webdriver.reap_profile", "true");

		this.initializeEnvironmentVariables();

		final WebDriverConfigUtils config = new WebDriverConfigUtils();
		//SET HEADLESS
		config.setHeadless(headlessMode);
		
		if(SystemUtils.IS_OS_LINUX){
			config.setChromeDriverPath("src/test/resources/chromedriver");	
		}else if(SystemUtils.IS_OS_WINDOWS){
			config.setChromeDriverPath("src/test/resources/chromedriver.exe");	
		}
		//config.setChromeDriverPath("src/test/resources/drivers/chromedriver.exe");
		config.setIeDriverPath("src/test/resources/IEDriverServer.exe");

		// Creates a driver suited for remote or local execution
		if (Constants.EXECUTION_TYPE_LOCAL.equalsIgnoreCase(this.executionType)) {
			driver = config.createDriverForLocalExecution(BrowserType
					.valueOf(this.browser));
		} else if (Constants.EXECUTION_TYPE_REMOTE
				.equalsIgnoreCase(this.executionType)) {
			driver = config.createDriverForRemoteExecution(this.remoteHost,
					BrowserType.valueOf(this.browser));
		}

		//factory = new AdminWebPageObjectFactory();
		//factory.setWebDriver(driver);
		
		System.out.println("");
		System.out.println("*****************   URL ENVIRONMENT  *****************");
		System.out.println(webContextURL);
		System.out.println("******************************************************");
		System.out.println("");
		

	}
	
     
	/**
	 * Initialize the possible environment variables
	 */
	protected void initializeEnvironmentVariables() {

		// Change the logLevel at runtime
		final String loggerLevel = System.getenv(Constants.ENV_LOGGER_LEVEL);
		if (!Strings.isNullOrEmpty(loggerLevel)) {
			LOGGER.info(
					"Changing Logger level of 'com.payulatam.testsuites' to {}",
					loggerLevel);
			ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
					.getLogger("com.payulatam.testsuites");
			logger.setLevel(Level.valueOf(loggerLevel));
		}

		// Check for the context URL
		final String contextURL = System.getenv(Constants.ENV_CUSTOM_URL);
		if (Strings.isNullOrEmpty(contextURL)) {
			LOGGER.debug(
					"beforeClass()--> No Context URL is specified. Fallback to [{}]. ",
					this.webContextURL);
		} else {
			this.webContextURL = contextURL;
			LOGGER.debug("beforeClass()--> Context Base URL [{}]",
					this.webContextURL);
		}

		// Check for the execution type
		final String execType = System.getenv(Constants.ENV_EXECUTION_TYPE);
		if (Strings.isNullOrEmpty(execType)) {
			LOGGER.debug(
					"beforeClass()--> No Execution Type is specified. Fallback to [{}]. ",
					this.executionType);
		} else {
			this.executionType = execType;
			LOGGER.debug("beforeClass()--> Execution Type: [{}]",
					this.executionType);
		}

		// Check for the remote host
		final String remote = System.getenv(Constants.ENV_REMOTE_HOST);
		if (Strings.isNullOrEmpty(remote)) {
			LOGGER.debug("beforeClass()--> No Remote host is specified. Fallback to None. ");
		} else {
			this.remoteHost = remote;
			LOGGER.debug("beforeClass()--> Remote Host: [{}]", this.remoteHost);
		}

		// Check for the browser
		final String envBrowser = System.getenv(Constants.ENV_REMOTE_BROWSER);
		if (Strings.isNullOrEmpty(envBrowser)) {
			LOGGER.debug(
					"beforeClass()--> No Browser is specified. Fallback to [{}]. ",
					this.browser);
		} else {
			this.browser = envBrowser;
			LOGGER.debug("beforeClass()--> Browser: [{}]", this.browser);
		}

		// Check for the evidence path
		final String envEvidencePath = System
				.getenv(Constants.ENV_EVIDENCE_PATH);
		if (Strings.isNullOrEmpty(envEvidencePath)) {
			LOGGER.debug(
					"beforeClass()--> No EvidencePath is specified. Fallback to [{}]. ",
					this.evidencePath);
		} else {
			this.evidencePath = envEvidencePath;
			LOGGER.debug("beforeClass()--> Evidence Path: [{}]",
					this.evidencePath);
		}

		// Check the evidence path, create if necessary
		final File fileEvidencePath = new File(this.evidencePath);
		if (!fileEvidencePath.exists()) {
			boolean result = fileEvidencePath.mkdirs();
			if (!result) {
				throw new Error("Cannot create the evidence path");
			}
			LOGGER.debug(
					"beforeClass()--> Evidence Path created successfully: [{}]",
					this.evidencePath);
		}
		
		// Check for the headless mode
		final String headless = System.getenv(WebDriverTestEnvironmentConstants.EXECUTION_HEADLESS);
			
			if (Strings.isNullOrEmpty(headless)) {
				this.headlessMode = "OFF";
				LOGGER.debug("beforeClass()--> No Headless mode is specified. [{}]", this.headlessMode);
			} else {
				this.headlessMode = "ON";
				LOGGER.debug("beforeClass()--> Headless mode: [{}]", this.headlessMode);
			}
						
		if (!fileEvidencePath.isDirectory()) {
			throw new Error("The specified Evidence Path is not a directory. ");
		}
		
		/**
		 * Initialize the properties file depending on the environment
		 * 
		 */
		 try {
			 InputStream is = null;
			 Environment enviroment = Environment.fromString(this.getWebContextURL());
			 switch (enviroment) {
				case QA:
					is = AbstractSearchIntegrationTest.class.getClassLoader().getResourceAsStream("dataQA.properties");
					break;
				default:
					break;
				}
			 properties = new Properties();
			 properties.load(is);
			
			 /** if the project is running in jenkins doesn't load the dataShared file because 
			  * it generates a NullPointerException
			  */
			 
			 
			 //if (Strings.isNullOrEmpty(remote)){
			 //	 InputStream dataShared = AbstractAdminIntegrationTest.class.getClassLoader().getResourceAsStream("dataShared.properties");	
			 //	 propertiesDataShare = new Properties();
			 //	 propertiesDataShare.load(dataShared);
			 //	 properties.putAll(propertiesDataShare);
			 //	 dataShared.close();
			 //	}
			 
			 is.close();
			 
		 }catch (IOException e1) {
			 LOGGER.error("beforeClass()--> Error trying to load properties file. Ex: [{}]", e1);
			 throw new RuntimeException(e1);
		 }
		
		
	}

	/**
	 * Finalization
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		
		driver.quit();
	}
	
	/**
	 * @return the driver
	 */
	public WebDriver getDriver() {

		return driver;
	}

	/**
	 * @return the factory
	 */
	/**public AdminWebPageObjectFactory getFactory() {

		return factory;
	}*/

	/**
	 * @return the webContextURL
	 */
	public String getWebContextURL() {

		return webContextURL;
	}

	/**
	 * @return the remoteHost
	 */
	public String getRemoteHost() {

		return remoteHost;
	}

	/**
	 * @return the executionType
	 */
	public String getExecutionType() {

		return executionType;
	}

	/**
	 * @return the browser
	 */
	public String getBrowser() {

		return browser;
	}

	/**
	 * @return the evidencePath
	 */
	public String getEvidencePath() {

		return evidencePath;
	}

	/**
	 * @return the pageLoadTimeOut
	 */
	public int getPageLoadTimeOut() {

		return pageLoadTimeOut;
	}
	
	/**
	// * Get a property value from the properties file.
	// *
	// * @param key
	// * the key to use
	// * @return {@link String} the value
	// */
	public String getProperty(String key) {
	
	 return this.properties.getProperty(key);
	}
	
	public void setWebContextURL(String webContextURL){
		
		this.webContextURL= webContextURL;
		
	}

}