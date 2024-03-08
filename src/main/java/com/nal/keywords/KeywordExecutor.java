package com.nal.keywords;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;

import com.nal.utils.CSVReader;

import io.qameta.allure.Step;

/**
 * @author naveenautomationlabs This class contains methods to execute keywords
 *         defined in test steps and perform corresponding actions.
 */
public class KeywordExecutor {

	/**
	 * ThreadLocal instance to hold WebDriver instance for each thread.
	 */
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	/**
	 * Executes the given keyword with the provided target and value.
	 * 
	 * @param keyword The keyword to execute.
	 * @param target  The target element or location.
	 * @param value   The value to input or verify.
	 */
	@Step("{keyword} | {target} | {value} | ")
	public void executeKeyword(String keyword, String target, String value) {
		switch (keyword) {
		case "OpenBrowser":
			try {
				Browser browser = Browser.valueOf(target.toLowerCase());
				openBrowser(browser);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Unsupported browser: " + value);
			}
			break;
		case "NavigateTo":
			navigateTo(target);
			break;
		case "Click":
			click(target);
			break;
		case "Type":
			type(target, value);
			break;
		case "VerifyText":
			verifyText(target, value);
			break;
		case "AssertElementPresent":
			assertElementPresent(target);
			break;
		case "SwitchToFrame":
			switchToFrame(target);
			break;
		case "SwitchToDefaultContent":
			switchToDefaultContent();
			break;
		// Add more cases for other keyword interpretations
		default:
			throw new IllegalArgumentException("Unsupported keyword: " + keyword);
		}
	}

	/**
	 * Executes the test steps for the given test case name.
	 * 
	 * @param testCaseName The name of the test case.
	 * @param testSteps    The list of test steps to execute.
	 */
	public void executeTestSteps(String testCaseName, List<String> testSteps) {
		System.out.println("Executing test case: " + testCaseName);
		for (String step : testSteps) {
			String[] parts = step.split(",");
			String keyword = parts[0].trim();
			String target = parts[1].trim();
			String value = "";
			if (parts.length == 3) {
				value = parts[2].trim();
			}
			executeKeyword(keyword, target, value);
		}
	}

	/**
	 * Executes test cases from the given CSV file.
	 * 
	 * @param csvFile The path to the CSV file containing test cases.
	 */
	public void executeTestCasesFromCSV(String csvFile) {
		CSVReader csvReader = new CSVReader();
		csvReader.executeTestCasesFromCSV(csvFile);
	}

	/**
	 * Asserts that the element specified by the target is present.
	 * 
	 * @param target The target element.
	 */
	private void assertElementPresent(String target) {
		By locator = getBy(target);
		boolean isElementPresent = isElementPresent(locator);
		Assert.assertTrue(isElementPresent, "Element is not present: " + target);
	}

	/**
	 * Checks if the element specified by the locator is present.
	 * 
	 * @param locator The locator of the element.
	 * @return True if the element is present, otherwise false.
	 */
	private boolean isElementPresent(By locator) {
		try {
			getDriver().findElement(locator);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Opens the browser specified by the browserName.
	 * 
	 * @param browser The browser to open.
	 */
	@Step("Open browser: {browser}")
	private void openBrowser(Browser browser) {
		WebDriver driver;
		System.out.println("==========browser : " + browser);
		switch (browser) {
		case chrome:
			ChromeOptions co = new ChromeOptions();
			co.addArguments("--window-size=1920,1080");
            co.addArguments("--no-sandbox");
            co.addArguments("--headless");
            co.addArguments("--disable-gpu");
            co.addArguments("--disable-crash-reporter");
            co.addArguments("--disable-extensions");
            co.addArguments("--disable-in-process-stack-traces");
            co.addArguments("--disable-logging");
            co.addArguments("--disable-dev-shm-usage");
            co.addArguments("--log-level=3");
            co.addArguments("--output=/dev/null");
            co.addArguments("ignore-certificate-errors");
			driver = new ChromeDriver(co);
			break;
		case firefox:
			driver = new FirefoxDriver();
			break;
		case edge:
			driver = new EdgeDriver();
			break;
		case ie:
			driver = new InternetExplorerDriver();
			break;
		case safari:
			driver = new SafariDriver();
			break;
		default:
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}
		tlDriver.set(driver);
	}

	/**
	 * Navigates to the specified URL.
	 * 
	 * @param url The URL to navigate to.
	 */
	@Step("Navigate to URL: {url}")
	private void navigateTo(String url) {
		getDriver().navigate().to(url);
	}

	/**
	 * Clicks on the element specified by the target.
	 * 
	 * @param target The target element to click.
	 */
	@Step("Click on element: {target}")
	private void click(String target) {
		By locator = getBy(target);
		getDriver().findElement(locator).click();
	}

	/**
	 * Types the specified value into the element specified by the target.
	 * 
	 * @param target The target element to type into.
	 * @param value  The value to type.
	 */
	@Step("Type '{value}' into element: {target}")
	private void type(String target, String value) {
		By locator = getBy(target);
		getDriver().findElement(locator).sendKeys(value);
	}

	/**
	 * Verifies that the text in the element specified by the target matches the
	 * expectedText.
	 * 
	 * @param target       The target element containing the text to verify.
	 * @param expectedText The expected text.
	 */
	@Step("Verify text '{expectedText}' in element: {target}")
	private void verifyText(String target, String expectedText) {
		By locator = getBy(target);
		String actualText = getDriver().findElement(locator).getText();
		Assert.assertEquals(actualText, expectedText, "Text verification failed!");
	}

	/**
	 * Switches to the frame specified by the frameName.
	 * 
	 * @param frameName The name or ID of the frame to switch to.
	 */
	@Step("Switch to frame: {frameName}")
	private void switchToFrame(String frameName) {
		getDriver().switchTo().frame(frameName);
	}

	/**
	 * Switches to the default content.
	 */
	@Step("Switch to default content")
	private void switchToDefaultContent() {
		getDriver().switchTo().defaultContent();
	}

	/**
	 * Retrieves the By locator for the specified target.
	 * 
	 * @param target The target element.
	 * @return The By locator for the target.
	 */
	private By getBy(String target) {
		By locator;
		if (target.startsWith("id=")) {
			locator = By.id(target.substring(3));
		} else if (target.startsWith("name=")) {
			locator = By.name(target.substring(5));
		} else if (target.startsWith("class=")) {
			locator = By.className(target.substring(6));
		} else if (target.startsWith("xpath=")) {
			locator = By.xpath(target.substring(6));
		} else if (target.startsWith("css=")) {
			locator = By.cssSelector(target.substring(4));
		} else if (target.startsWith("linktext=")) {
			locator = By.linkText(target.substring(9));
		} else {
			throw new IllegalArgumentException("Unsupported locator format: " + target);
		}
		return locator;
	}

	/**
	 * Retrieves the WebDriver instance.
	 * 
	 * @return The WebDriver instance.
	 */
	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	/**
	 * Takes a screenshot of the current WebDriver instance and saves it to the
	 * specified file.
	 * 
	 * @param methodName The name of the method or test.
	 * @return The path to the saved screenshot file.
	 */
	public static String getScreenshot(String methodName) {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/screenshot/" + methodName + "_" + System.currentTimeMillis()
				+ ".png";
		File destination = new File(path);
		try {
			FileHandler.copy(srcFile, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
}
