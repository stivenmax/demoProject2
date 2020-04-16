package com.AutomationTalks.demoProject2;

import java.net.URL;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;

	/**
	 *
	 * @author
	 *
	 */
	public class CustomRemoteWebDriver extends RemoteWebDriver implements TakesScreenshot {

		/**
		 * @param remoteAddress
		 * @param desiredCapabilities
		 */
		public CustomRemoteWebDriver(URL remoteAddress, MutableCapabilities desiredCapabilities) {

			super(remoteAddress, desiredCapabilities);
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * org.openqa.selenium.remote.RemoteWebDriver#getScreenshotAs(org.openqa.selenium.OutputType)
		 */
		@Override
		public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {

			if ((Boolean) getCapabilities().getCapability(CapabilityType.TAKES_SCREENSHOT)) {
				String base64Str = execute(DriverCommand.SCREENSHOT).getValue().toString();
				return target.convertFromBase64Png(base64Str);
			}
			return null;
		}
	}