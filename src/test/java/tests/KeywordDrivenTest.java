
package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.nal.keywords.KeywordExecutor;
import com.nal.listeners.TestAllureListener;

@Listeners(TestAllureListener.class)
public class KeywordDrivenTest {

	@AfterMethod
	public void tearDown() {
		if (KeywordExecutor.getDriver() != null) {
			KeywordExecutor.getDriver().close();
		}
	}

	@Test
	public void executeLoginTest() {
		KeywordExecutor keywordExecutor = new KeywordExecutor();
		keywordExecutor.executeTestCasesFromCSV("login_test.csv");
	}

	@Test
	public void executeSearchTest() {
		KeywordExecutor keywordExecutor = new KeywordExecutor();
		keywordExecutor.executeTestCasesFromCSV("search_test.csv");
	}

	@Test
	public void executeHomeTest() {
		KeywordExecutor keywordExecutor = new KeywordExecutor();
		keywordExecutor.executeTestCasesFromCSV("home_test.csv");
	}
}