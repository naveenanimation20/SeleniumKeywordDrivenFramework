<img width="1241" alt="Screenshot 2024-03-04 at 8 02 29 PM" src="https://github.com/naveenanimation20/SeleniumKeywordDrivenFramework/assets/6771652/ac54588d-c63d-4af8-8466-35401410bf02"># Keyword Driven Testing Framework with Selenium

## Overview
This project demonstrates a Keyword Driven Testing (KDT) framework implemented using Selenium WebDriver in Java. The framework allows for easy creation and execution of automated test cases using a set of keywords defined in CSV files.

## Features
- Keyword-driven approach for writing test cases.
- Test cases defined in CSV files for easy maintenance and readability.
- Support for parallel execution of test cases.
- Integration with TestNG for test execution and reporting.
- Flexible and extensible architecture.

## Project Structure
- `src/main/java/`: Contains the source code for the Keyword Driven Testing framework.
- `src/test/java/`: Contains the test scripts written using the framework.
- `src/test/resources/csvs/`: Contains the CSV files defining the test cases in the form of keywords.
- `testng.xml`: TestNG configuration file for executing the tests.

## Setup Instructions
1. Clone the repository to your local machine.
2. Import the project into your preferred Java IDE (e.g., Eclipse, IntelliJ IDEA).
3. Ensure that you have the necessary dependencies configured (e.g., Selenium WebDriver, TestNG).
4. Define your test cases in CSV files located in the `src/test/resources/csvs/` directory.

## Running Tests
- You can run the tests using the TestNG XML configuration file (`testng.xml`).
- Execute the `testng.xml` file using your IDE or the TestNG command-line interface.
- Ensure that the WebDriver instance is properly initialized and managed during test execution.
- Parallel run through thread-count and parallel tags in testng.xml

## Console Output
- You can see the full csv file formatted data in the console output.
<img width="1241" alt="Screenshot 2024-03-04 at 8 02 29 PM" src="https://github.com/naveenanimation20/SeleniumKeywordDrivenFramework/assets/6771652/a621fb1a-5114-41d5-84cb-31de4c18c2f8">


## Test Reporting
- TestNG generates detailed HTML reports after test execution.
- The reports provide information about test results, including pass/fail status and error messages.
- Integrated Extent and Allure reports as well, including pass/fail status, error messages and screenshot for failure tests.

## Information about reporting tools used in the project, such as Allure, ExtentReport.

- Allure Report
  Allure is a flexible lightweight test report tool that not only shows a very concise representation of what have been tested in a neat web report form, but allows everyone participating in the development process to extract maximum of useful information from everyday execution of tests.

- To generate Allure reports, follow these steps:

- Install Allure command-line tool using the instructions provided in the Allure documentation.

Execute your tests with Allure listeners attached.

After the test execution is complete, generate the Allure report using the command:
**allure generate --clean
**
- View the generated report by running:
**allure open
**
<img width="1792" alt="Screenshot 2024-03-04 at 7 36 36 PM" src="https://github.com/naveenanimation20/SeleniumKeywordDrivenFramework/assets/6771652/da6085b3-6cd2-4ee2-8fdc-d9deb29834ca">

<img width="1787" alt="Screenshot 2024-03-04 at 7 36 59 PM" src="https://github.com/naveenanimation20/SeleniumKeywordDrivenFramework/assets/6771652/4004cccc-2ce3-4c3e-b3a2-bbeac55b7d92">

<img width="1766" alt="Screenshot 2024-03-04 at 7 53 04 PM" src="https://github.com/naveenanimation20/SeleniumKeywordDrivenFramework/assets/6771652/5e3dfd3f-bc28-4510-829e-22fd4e0f22a3">

- Extent Report
  ExtentReports is an open-source reporting library for Java designed for the creation of beautiful, interactive, and detailed HTML reports.
  - After the test execution is complete, the Extent report will be generated automatically.
  - View the generated Extent report in the /reports directory.

  <img width="1461" alt="Screenshot 2024-03-04 at 7 38 08 PM" src="https://github.com/naveenanimation20/SeleniumKeywordDrivenFramework/assets/6771652/c77caa57-dd74-4220-8006-d2b06bee17c8">

<img width="1455" alt="Screenshot 2024-03-04 at 7 38 29 PM" src="https://github.com/naveenanimation20/SeleniumKeywordDrivenFramework/assets/6771652/cabde873-0151-4ad5-8828-7b6e1325aca8">

## Contributing
Contributions to improve the framework or add new features are welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request.

## License
NA
