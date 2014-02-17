package com.wikia.webdriver.Common.Templates;

import java.io.File;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.wikia.webdriver.Common.ContentPatterns.URLsContent;
import com.wikia.webdriver.Common.Core.Annotations.NetworkTrafficDump;
import com.wikia.webdriver.Common.Core.CommonUtils;
import com.wikia.webdriver.Common.Core.Configuration.AbstractConfiguration;
import com.wikia.webdriver.Common.Core.Configuration.ConfigurationFactory;
import com.wikia.webdriver.Common.Core.GeoEdge.GeoEdgeProxy;
import com.wikia.webdriver.Common.Core.GeoEdge.GeoEdgeUtils;
import com.wikia.webdriver.Common.Core.NetworkTrafficInterceptor.NetworkTrafficInterceptor;
import com.wikia.webdriver.Common.Core.URLBuilder.UrlBuilder;
import com.wikia.webdriver.Common.DriverProvider.NewDriverProvider;
import com.wikia.webdriver.Common.Logging.PageObjectLogging;
import com.wikia.webdriver.Common.Properties.Properties;
import java.lang.reflect.Method;
import org.browsermob.proxy.ProxyServer;

/**
 * @author Karol 'kkarolk' Kujawiak
 *
 */
@Listeners({ com.wikia.webdriver.Common.Logging.PageObjectLogging.class })
public class NewTestTemplateCore {

	protected WebDriver driver;
	protected WebDriver driverFF;
	protected UrlBuilder urlBuilder;
	protected AbstractConfiguration config;
	protected String wikiURL;
	protected String wikiCorporateURL;
	protected String wikiCorpSetupURL;
	private DesiredCapabilities capabilities;
	protected NetworkTrafficInterceptor networkTrafficIntereceptor;
	protected boolean isProxyServerRunning = false;

	public NewTestTemplateCore() {
		config = ConfigurationFactory.getConfig();
	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		prepareDirectories();
	}

	protected void prepareDirectories() {
		Properties.setProperties();
		CommonUtils.deleteDirectory("." + File.separator + "logs");
		CommonUtils.createDirectory("." + File.separator + "logs");
	}

	private void printProperties() {
		System.out.println("Wiki url: " + wikiURL);
		System.out.println("Wiki corporate url: " + wikiCorporateURL);
	}

	protected void prepareURLs() {
		urlBuilder = new UrlBuilder(config.getEnv());
		wikiURL = urlBuilder.getUrlForWiki(config.getWikiName());
		wikiCorporateURL = urlBuilder.getUrlForWiki("wikia");
		wikiCorpSetupURL = urlBuilder.getUrlForWiki("corp");
		printProperties();
	}

	protected void startBrowser() {
		driver = registerDriverListener(
			NewDriverProvider.getDriverInstanceForBrowser (config.getBrowser())
		);
	}

	protected WebDriver registerDriverListener(EventFiringWebDriver driver) {
		driver.register(new PageObjectLogging());
		return driver;
	}

	protected void startBrowserFirefox() {
		EventFiringWebDriver eventDriver = NewDriverProvider.getDriverInstanceForBrowser (
			"FF"
		);
		eventDriver.register(new PageObjectLogging());
		driverFF = eventDriver;
	}

	protected void logOut() {
		driver.get(wikiURL + URLsContent.logout);
	}

	protected void logOutFirefox() {
		driverFF.get(wikiURL + URLsContent.logout);
	}

	protected void stopBrowser() {
		if (driver != null) {
			driver.quit();
		}
	}

	protected void stopBrowserFirefox() {
		if (driverFF != null) {
			driverFF.quit();
		}
	}

	protected DesiredCapabilities getCapsWithProxyServerSet(ProxyServer server) {
		capabilities = new DesiredCapabilities();
		try {
			capabilities.setCapability(
				CapabilityType.PROXY, server.seleniumProxy()
			);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return capabilities;
	}

	protected void setDriverCapabilities(DesiredCapabilities caps) {
		NewDriverProvider.setDriverCapabilities(caps);
	}

	protected void setWindowSize(int width, int height, WebDriver desiredDriver) {
		Dimension dimension = new Dimension(width, height);
		desiredDriver.manage().window().setSize(dimension);
	}

	protected void setBrowserUserAgent(String userAgent) {
		NewDriverProvider.setBrowserUserAgent(config.getBrowser(), userAgent);
	}

	protected void runProxyServerIfNeeded(Method method) {
		boolean isGeoEdgeSet = false;
		boolean isNetworkTrafficDumpSet = false;
		String countryCode = null;

		if (method.getAnnotation(GeoEdgeProxy.class) != null) {
			isGeoEdgeSet = true;
			countryCode = method.getAnnotation(GeoEdgeProxy.class).country();
		}

		if (method.getAnnotation(NetworkTrafficDump.class) != null) {
			isNetworkTrafficDumpSet = true;
		}

		if (isGeoEdgeSet || isNetworkTrafficDumpSet) {
			isProxyServerRunning = true;
			networkTrafficIntereceptor = new NetworkTrafficInterceptor();
			networkTrafficIntereceptor.startSeleniumProxyServer();
		} else {
			return;
		}

		if (isGeoEdgeSet) {
			GeoEdgeUtils geoEdgeUtils = new GeoEdgeUtils(config.getCredentialsFilePath());
			String credentialsBase64 = "Basic " + geoEdgeUtils.createBaseFromCredentials();
			String IP = geoEdgeUtils.getIPForCountry(countryCode);
			networkTrafficIntereceptor.setProxyServer(IP);
			networkTrafficIntereceptor.changeHeader("Proxy-Authorization", credentialsBase64);
		}

		capabilities = getCapsWithProxyServerSet(networkTrafficIntereceptor);
		setDriverCapabilities(capabilities);
	}
}
