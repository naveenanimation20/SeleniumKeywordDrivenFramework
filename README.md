# Keyword Driven Testing Framework with Selenium

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
- `src/test/resources/csvs/`: Contains the CSV files defining the test cases.
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

## Test Reporting
- TestNG generates detailed HTML reports after test execution.
- The reports provide information about test results, including pass/fail status and error messages.

## Contributing
Contributions to improve the framework or add new features are welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request.

## License
This project is licensed under the [MIT License](LICENSE).

