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
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;

import com.nal.utils.CSVReader;

import io.qameta.allure.Step;

public class KeywordExecutor {
	
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();


	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	@Step("{keyword} | {target} | {value} | ")
	public void executeKeyword(String keyword, String target, String value) {
		switch (keyword) {
		case "OpenBrowser":
			openBrowser(target);
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
	
	public void executeTestCasesFromCSV(String csvFile) {
        CSVReader csvReader = new CSVReader();
        csvReader.executeTestCasesFromCSV(csvFile);
    }

	private void assertElementPresent(String target) {
		By locator = getBy(target);
		boolean isElementPresent = isElementPresent(locator);
		Assert.assertTrue(isElementPresent, "Element is not present: " + target);
	}

	private boolean isElementPresent(By locator) {
		try {
			getDriver().findElement(locator);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	@Step("Open browser: {browserName}")
	private void openBrowser(String browserName) {
		switch (browserName.toLowerCase()) {
		case "chrome":
			tlDriver.set(new ChromeDriver());
			break;
		// Add more cases for other browsers if needed
		default:
			break;
		}
	}

	@Step("Navigate to URL: {url}")
	private void navigateTo(String url) {
		getDriver().navigate().to(url);
	}

	@Step("Click on element: {target}")
	private void click(String target) {
		By locator = getBy(target);
		getDriver().findElement(locator).click();
	}

	@Step("Type '{value}' into element: {target}")
	private void type(String target, String value) {
		By locator = getBy(target);
		getDriver().findElement(locator).sendKeys(value);
	}

	@Step("Verify text '{expectedText}' in element: {target}")
	private void verifyText(String target, String expectedText) {
		By locator = getBy(target);
		String actualText = getDriver().findElement(locator).getText();
		Assert.assertEquals(actualText, expectedText, "Text verification failed!");
	}

	@Step("Switch to frame: {frameName}")
	private void switchToFrame(String frameName) {
		getDriver().switchTo().frame(frameName);
	}

	@Step("Switch to default content")
	private void switchToDefaultContent() {
		getDriver().switchTo().defaultContent();
	}

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
	 * take screenshot
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
