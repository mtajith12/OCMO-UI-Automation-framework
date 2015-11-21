package pageobjects;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class BaseClass {
	//public static WebDriver driver;
	public static boolean bResult;
	public static WebDriver driver;
	public static Logger Add_Log = null;
	public boolean BrowseralreadyLoaded=false;
	public static Properties PropertyUtil = null;
	public static Properties ObjectRep = null;
	// public static WebDriver driver=null;
	public static WebDriver ExistingchromeBrowser;
	public static WebDriver ExistingmozillaBrowser;
	public static WebDriver ExistingIEBrowser;
//	ATUTestRecorder recorder;

	public  BaseClass(WebDriver driver){
		BaseClass.driver = driver;
		BaseClass.bResult = true;

	}
	public static void init() throws IOException {
		//To Initialize logger service.
		//PropertiesConfigurator is used to configure logger from properties file
		PropertyConfigurator.configure("log4j.properties");
		Add_Log = Logger.getLogger("rootLogger");
		Add_Log.info("*** Test Execution is started ***");

		//Bellow given syntax will Insert log In applog.log file.

		//Initialize Config.properties file.
		PropertyUtil = new Properties();
		FileInputStream fip = new FileInputStream(System.getProperty("user.dir") + "//property//Config.properties");
		PropertyUtil.load(fip);
		Add_Log.info("Config.properties file loaded successfully.");

		//Initialize Objects.properties file.
		ObjectRep = new Properties();
		fip = new FileInputStream(System.getProperty("user.dir") + "//property//Objects.properties");
		ObjectRep.load(fip);
		Add_Log.info("Objects.properties file loaded successfully.");

		//Check If any previous webdriver browser Instance Is exist then run new test In that existing webdriver browser Instance.
		if (PropertyUtil.getProperty("testBrowser").equalsIgnoreCase("Mozilla") && ExistingmozillaBrowser != null) {
			driver = ExistingmozillaBrowser;
			return;
		} else if (PropertyUtil.getProperty("testBrowser").equalsIgnoreCase("chrome") && ExistingchromeBrowser != null) {
			driver = ExistingchromeBrowser;
			return;
		} else if (PropertyUtil.getProperty("testBrowser").equalsIgnoreCase("IE") && ExistingIEBrowser != null) {
			driver = ExistingIEBrowser;
			return;
		}

		if (PropertyUtil.getProperty("testBrowser").equalsIgnoreCase("Mozilla")) {
			//To Load Firefox driver Instance.
			driver = new FirefoxDriver();
			ExistingmozillaBrowser = driver;
			Add_Log.info("Firefox Driver Instance loaded successfully.");

		} else if (PropertyUtil.getProperty("testBrowser").equalsIgnoreCase("Chrome")) {
			//To Load Chrome driver Instance.
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//BrowserDrivers//chromedriver.exe");
			driver = new ChromeDriver();
			ExistingchromeBrowser = driver;
			Add_Log.info("Chrome Driver Instance loaded successfully.");

		} else if (PropertyUtil.getProperty("testBrowser").equalsIgnoreCase("IE")) {
			//To Load IE driver Instance.
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "//BrowserDrivers//IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			ExistingIEBrowser = driver;
			Add_Log.info("IE Driver Instance loaded successfully.");
			/**
			 * Delete all cookies at the start of each scenario to avoid
			 * shared state between tests
			 */
			//  driver.manage().deleteAllCookies();
			//  driver.manage().window().maximize();
		}
	}




	@After
	/**
	 * Embed a screenshot in test report if test is marked as failed
	 */
	public void embedScreenshot(Scenario scenario) {

		if(scenario.isFailed()) {
			try {
				scenario.write("Current Page URL is " + driver.getCurrentUrl());
//            byte[] screenshot = getScreenshotAs(OutputType.BYTES);
				byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshot, "image/png");
			} catch (WebDriverException somePlatformsDontSupportScreenshots) {
				System.err.println(somePlatformsDontSupportScreenshots.getMessage());
			}

		}
		//driver.quit();

	}

}


