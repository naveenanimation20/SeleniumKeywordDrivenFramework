package com.nal.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nal.keywords.KeywordExecutor;

/**
 * @author naveenautomationlabs
 * This class reads test cases from CSV files and executes them using a KeywordExecutor.
 */
public class CSVReader {

    private static final Object lock = new Object();

    /**
     * Path to the CSV file being processed.
     */
    public static String csvPath = null;

    /**
     * Reads and executes test cases from a CSV file.
     * 
     * @param csvFile The path to the CSV file containing test cases.
     */
    public void executeTestCasesFromCSV(String csvFile) {
        csvPath = "./src/test/resources/csvs/" + csvFile;
        printTestCasesFromCSV(csvFile);
        try (BufferedReader br = new BufferedReader(
                new FileReader("./src/test/resources/csvs/" + csvFile))) {
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
                        KeywordExecutor keywordExecutor = new KeywordExecutor();
                        keywordExecutor.executeTestSteps(currentTestCase, testSteps);
                        testSteps.clear();
                    }
                    currentTestCase = testCaseName;
                }

                // Add current test step to the list
                testSteps.add(keyword + "," + target + "," + value);
            }

            // Execute the last test case
            if (currentTestCase != null) {
                KeywordExecutor keywordExecutor = new KeywordExecutor();
                keywordExecutor.executeTestSteps(currentTestCase, testSteps);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the test cases from the given CSV file.
     * 
     * @param csvFile The path to the CSV file containing test cases.
     */
    private void printTestCasesFromCSV(String csvFile) {
        synchronized (lock) {
            try (BufferedReader br = new BufferedReader(
                    new FileReader("./src/test/resources/csvs/" + csvFile))) {
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

    /**
     * Counts the number of test cases in the given CSV file.
     * 
     * @param csvFile The path to the CSV file containing test cases.
     * @return The number of test cases in the CSV file.
     * @throws IOException If an I/O error occurs.
     */
    private int getTestCaseCount(String csvFile) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(
                new FileReader("./src/test/resources/csvs/" + csvFile))) {
            while (br.readLine() != null) {
                count++;
            }
        }
        return count;
    }
}
