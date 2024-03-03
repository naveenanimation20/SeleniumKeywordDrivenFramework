package ExecutionRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;

public class TestExecutor {
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	public TestExecutor() {
		// this.driver = new ChromeDriver();
	}

	public void executeTestCasesFromCSV(String csvFile) {
		try (BufferedReader br = new BufferedReader(new FileReader("./src/test/java/csvs/" + csvFile))) {
			String line;
			String currentTestCase = null;
			List<String> testSteps = new ArrayList<>();

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				String testCaseName = data[0];
				String keyword = data[1].trim();
				String target = data[2].trim();
				String value = (data.length == 4) ? data[3].trim() : ""; // Set value to empty string if not provided

				if (!testCaseName.equals(currentTestCase)) {
					// Execute previous test case steps
					if (currentTestCase != null) {
						executeTestSteps(currentTestCase, testSteps);
						testSteps.clear();
					}
					currentTestCase = testCaseName;
				}

				// Add current test step to the list
				testSteps.add(keyword + "," + target + "," + value);
			}

			// Execute the last test case
			if (currentTestCase != null) {
				executeTestSteps(currentTestCase, testSteps);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void executeTestSteps(String testCaseName, List<String> testSteps) {
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

	private void executeKeyword(String keyword, String target, String value) {
		// Interpret and execute keywords
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
		// Add more keyword interpretations as needed
		}
	}

	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	private void openBrowser(String browserName) {
		switch (browserName.toLowerCase()) {
		case "chrome":
			// this.driver = new ChromeDriver();
			tlDriver.set(new ChromeDriver());
			break;

		default:
			break;
		}
	}

	private void navigateTo(String url) {
		getDriver().navigate().to(url);
	}

	private void click(String target) {
		By locator = getBy(target);
		getDriver().findElement(locator).click();
	}

	private void type(String target, String value) {
		By locator = getBy(target);
		getDriver().findElement(locator).sendKeys(value);
	}

	private void verifyText(String target, String expectedText) {
		By locator = getBy(target);
		String actualText = getDriver().findElement(locator).getText();
		Assert.assertEquals(actualText, expectedText, "Text verification failed!");
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

	public void quitBrowser() {
		getDriver().quit();

	}

	public static void main(String[] args) {
		TestExecutor testExecutor = new TestExecutor();
		testExecutor.executeTestCasesFromCSV("./src/test/java/csvs/login_test.csv");
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
