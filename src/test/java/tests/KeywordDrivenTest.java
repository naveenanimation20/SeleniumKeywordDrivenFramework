package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.qa.opencart.listeners.TestAllureListener;

import ExecutionRunner.TestExecutor;


@Listeners(TestAllureListener.class)
public class KeywordDrivenTest {


	

	@AfterMethod
	public void tearDown() {
		// Quit TestExecutor
		if(TestExecutor.getDriver()!=null) {
			TestExecutor.getDriver().close();
		}
	}
	

	
	@Test
	public void executeLoginTest() {
		TestExecutor testExecutor = new TestExecutor();
		testExecutor.executeTestCasesFromCSV("login_test.csv");
	}

    @Test
	public void executeSearchTest() {
		TestExecutor testExecutor = new TestExecutor();
		testExecutor.executeTestCasesFromCSV("search_test.csv");
	}

    @Test
	public void executeHomeTest() {
		TestExecutor testExecutor = new TestExecutor();
		testExecutor.executeTestCasesFromCSV("home_test.csv");
	}

	
	
	
	
	
	
}
