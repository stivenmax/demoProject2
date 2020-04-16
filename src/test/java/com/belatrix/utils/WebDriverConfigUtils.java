package com.belatrix.utils;

import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM;
import static org.openqa.selenium.remote.CapabilityType.SUPPORTS_JAVASCRIPT;
import static org.openqa.selenium.remote.CapabilityType.VERSION;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.AutomationTalks.demoProject2.CustomRemoteWebDriver;
import com.belatrix.enums.BrowserType;

/**
 * Utility class used to create WebDrivers.<br/>
 * Configuration is required for Chrome and IE
 *
 * @author <a href="mailto:ing_stiven@hotmail.com">Stiven Jose Mosquera Puello</a><br/>
 *         13/12/2019
 *
 */
public class WebDriverConfigUtils implements Serializable {

	private static final long serialVersionUID = -8301166631138136830L;

	/** Class logger */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WebDriverConfigUtils.class);

	/** Path for the chrome driver */
	private String chromeDriverPath;

	/** Path for the IE Driver */
	private String ieDriverPath = "src/test/resources/IEDriverServer.exe";
	
	/** Script run time */
	private int scriptRunTime = 120;

	/** Encoding to use */
	private String encoding = "ISO-8859-1";
	
	/**Headless Mode*/
	private String headless;

	/**
	 * Default constructor
	 */
	public WebDriverConfigUtils() {

	}

	/**
	 * Creates a Driver configurated for local execution
	 *
	 * @param browser
	 *            {@link BrowserType} the browser to use
	 * @return {@link WebDriver}
	 */
	public WebDriver createDriverForLocalExecution(BrowserType browser) {

		WebDriver driver = null;
		

			switch (browser) {
			case IE:
				driver = new InternetExplorerDriver(this.createIEConfig());
				break;
			case CHROME:
				driver = new ChromeDriver(this.createChromeConfig());
				// robot.keyPress(KeyEvent.VK_F11);
				break;
			case CHROME_PROXY:
				driver = new ChromeDriver(this.createChromeProxyConfig());
				// robot.keyPress(KeyEvent.VK_F11);
				break;
			case CHROME_LINUX:	
				driver = new ChromeDriver(this.createChromeConfig());
				// robot.keyPress(KeyEvent.VK_F11);
				break;
			default:
				throw new RuntimeException("Browser " + browser
						+ " not supported. ");
			}
			
		return driver;
	}

	/**
	 * Creates a Driver configurated for remote execution
	 *
	 * @param host
	 *            {@link String} the host:port to use
	 * @param browser
	 *            {@link BrowserType} the browser to use
	 * @return {@link WebDriver}
	 */
	public WebDriver createDriverForRemoteExecution(String host,
			BrowserType browser) {

		String[] hosts = host.split(":");
		String port = hosts.length == 1 ? "4444" : hosts[1];

		WebDriver driver = null;
		DesiredCapabilities capabilities = null;

		switch (browser) {
		case IE:
			capabilities = createIEConfig();
			capabilities.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			capabilities.setBrowserName("iexplore");
			break;
		case CHROME:
			System.setProperty("webdriver.chrome.driver", new File(
					this.chromeDriverPath).getAbsolutePath());

			capabilities = DesiredCapabilities.chrome();
			capabilities.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			capabilities.setBrowserName("chrome");
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized", "--disable-popup-blocking"));
			break;
		case CHROME_PROXY:
			System.setProperty("webdriver.chrome.driver", new File(
					this.chromeDriverPath).getAbsolutePath());

			capabilities = DesiredCapabilities.chrome();
			capabilities.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			capabilities.setBrowserName("chrome");
			
			//System.setProperty("http.proxyHost", "127.0.0.1");
			//System.setProperty("http.proxyPort", "8080");
			
			String burp_proxy = "127.0.0.1:8080";
			Proxy proxy = new Proxy();
			proxy.setHttpProxy(burp_proxy)
				.setFtpProxy(burp_proxy)
				.setSslProxy(burp_proxy);
			capabilities.setCapability(CapabilityType.PROXY, proxy);
						
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized", "--disable-popup-blocking"));
			break;
		default:
			throw new RuntimeException("Browser " + browser
					+ " not supported. ");
		}

		try {
			String txtUrl = "http://" + hosts[0] + ":" + port + "/wd/hub";
			URL url = new URL(txtUrl);
			driver = new CustomRemoteWebDriver(url, capabilities);
		} catch (MalformedURLException me) {
			LOGGER.error("createDriverForRemoteExecution()--> Error: [{}]",
					me.getMessage());
			driver = null;
		}

		return driver;
	}

	
	private DesiredCapabilities createIEConfig() {
		
		System.setProperty("webdriver.ie.driver", this.ieDriverPath);
		DesiredCapabilities capabilities = new DesiredCapabilities(
				"Internet Explorer", "9", Platform.WINDOWS);
		capabilities.setJavascriptEnabled(true);

		// Desabilitar modo protegido
		capabilities.setCapability("ignoreProtectedModeSettings", true);
		capabilities.setCapability("ensureCleanSession", true);
		capabilities
				.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);

		return capabilities;
	}


	/**
	 *
	 * @return
	 * @throws IOException
	 */
	private DesiredCapabilities createChromeConfig() {

		ChromeOptions options = new ChromeOptions();
		
		if(headless.equals("ON")) {
			options.addArguments("--headless");
			options.addArguments("--no-sandbox");
			//options.addArguments("--disable-gpu");	
		}
		System.setProperty("webdriver.chrome.driver", this.chromeDriverPath);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability("start-maximized", 1);
        cap.setCapability("chrome.switches", Arrays.asList("--start-maximized", "--disable-popup-blocking"));
	
		//cap.setCapability("chrome.switches", Arrays.asList("--disable-popup-blocking"));

		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		cap.setCapability(ChromeOptions.CAPABILITY, options);

		return cap;
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	private DesiredCapabilities createChromeProxyConfig() {

		ChromeOptions options = new ChromeOptions();

		System.setProperty("webdriver.chrome.driver", this.chromeDriverPath);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability("start-maximized", 1);

		cap.setCapability("chrome.switches", Arrays.asList("--start-maximized", "--disable-popup-blocking"));

		//cap.setCapability("chrome.switches", Arrays.asList("--disable-popup-blocking"));
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8080");
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
				UnexpectedAlertBehaviour.ACCEPT);
		
		cap.setCapability(ChromeOptions.CAPABILITY, options);

		return cap;
	}
	
	
	/**
	 * @return the chromeDriverPath
	 */
	public String getChromeDriverPath() {

		return chromeDriverPath;
	}

	/**
	 * @param chromeDriverPath
	 *            the chromeDriverPath to set
	 */
	public void setChromeDriverPath(String chromeDriverPath) {

		this.chromeDriverPath = chromeDriverPath;
	}

	
	/**
	 * @return the ieDriverPath
	 */
	public String getIeDriverPath() {

		return ieDriverPath;
	}

	/**
	 * @param ieDriverPath
	 *            the ieDriverPath to set
	 */
	public void setIeDriverPath(String ieDriverPath) {

		this.ieDriverPath = ieDriverPath;
	}

	/**
	 * @return the scriptRunTime
	 */
	public int getScriptRunTime() {

		return scriptRunTime;
	}

	/**
	 * @param scriptRunTime
	 *            the scriptRunTime to set
	 */
	public void setScriptRunTime(int scriptRunTime) {

		this.scriptRunTime = scriptRunTime;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {

		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {

		this.encoding = encoding;
	}

	
	/**
	 * @param headless
	 *            the headless to set
	 */
	public void setHeadless(String headless) {
		this.headless = headless;
	}
	
}
