package ExecutionRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;

import io.qameta.allure.Step;

public class TestExecutor {
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();
	private static final Object lock = new Object();

	public static String csvPath = null;

	public TestExecutor() {
		// this.driver = new ChromeDriver();

	}

	private void printTestCasesFromCSV(String csvFile) {
		synchronized (lock) {
			try (BufferedReader br = new BufferedReader(new FileReader("./src/test/resources/csvs/" + csvFile))) {
				String line;
				System.out.println("Test cases from CSV file: " + csvFile);
				System.out.println(
						"+-------------------------------------------------------------------------------------------+");
				System.out.printf("| %-15s | %-15s | %-50s | %-20s |\n", "Test Case", "Keyword", "Target", "Value");
				System.out.println(
						"+-------------------------------------------------------------------------------------------+");
				while ((line = br.readLine()) != null) {
					String[] data = line.split(",");
					String testCase = data[0];
					String keyword = data[1];
					String target = data[2];
					String value = "";
					if (data.length > 3) {
						value = data[3];
					}
					System.out.printf("| %-15s | %-15s | %-50s | %-20s |\n", testCase, keyword, target, value);
				}
				System.out.println("+----------------------------------------------------------------+");
				System.out.println("Total test cases: " + getTestCaseCount(csvFile));
				System.out.println("+----------------------------------------------------------------+");
				System.out.println();
				System.out.println();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private int getTestCaseCount(String csvFile) throws IOException {
		int count = 0;
		try (BufferedReader br = new BufferedReader(new FileReader("./src/test/resources/csvs/" + csvFile))) {
			while (br.readLine() != null) {
				count++;
			}
		}
		return count;
	}

	// @Step("csv: {0}" )
	public void executeTestCasesFromCSV(String csvFile) {
		csvPath = "./src/test/resources/csvs/" + csvFile;
		printTestCasesFromCSV(csvFile);
		try (BufferedReader br = new BufferedReader(new FileReader("./src/test/resources/csvs/" + csvFile))) {
			String line;
			String currentTestCase = null;
			List<String> testSteps = new ArrayList<>();

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				String testCaseName = data[0];
				String keyword = data[1].trim();
				String target = data[2].trim();
				String value = (data.length == 4) ? data[3].trim() : "";

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

	@Step("{keyword} | {target} | {value} | ")
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

	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	@Step("Open browser: {browserName}")
	private void openBrowser(String browserName) {
		switch (browserName.toLowerCase()) {
		case "chrome":
			tlDriver.set(new ChromeDriver());
			break;

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

	@Step("Maximize the browser window")
	private void maximizeBrowserWindow() {
		getDriver().manage().window().maximize();
	}

	@Step("Refresh the page")
	private void refreshPage() {
		getDriver().navigate().refresh();
	}

	@Step("Navigate back")
	private void navigateBack() {
		getDriver().navigate().back();
	}

	@Step("Navigate forward")
	private void navigateForward() {
		getDriver().navigate().forward();
	}

	@Step("Clear text from element: {target}")
	private void clearText(String target) {
		By locator = getBy(target);
		getDriver().findElement(locator).clear();
	}

	@Step("Switch to frame: {frameName}")
	private void switchToFrame(String frameName) {
		getDriver().switchTo().frame(frameName);
	}

	@Step("Switch to default content")
	private void switchToDefaultContent() {
		getDriver().switchTo().defaultContent();
	}

	@Step("Accept alert")
	private void acceptAlert() {
		getDriver().switchTo().alert().accept();
	}

	@Step("Dismiss alert")
	private void dismissAlert() {
		getDriver().switchTo().alert().dismiss();
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
