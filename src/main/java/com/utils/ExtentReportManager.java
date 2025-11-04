package com.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager - Singleton manager class for ExtentReports HTML test reporting.
 * Handles creation, configuration, and lifecycle management of ExtentReports instance.
 * Generates detailed HTML reports with test results, screenshots, and system information.
 */
public class ExtentReportManager {
    
    /** Logger instance for logging report operations */
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    
    /** Singleton instance of ExtentReports - only one instance exists throughout test execution */
    private static ExtentReports extentReports;
    
    /** Path where the HTML report will be saved */
    private static String reportPath;
    
    /**
     * Creates and configures the ExtentReports instance (Singleton pattern)
     * Only creates a new instance if one doesn't already exist
     * Sets up HTML reporter with custom configuration and system information
     */
    public static void createInstance() {
        if (extentReports == null) {
            // Generate timestamped filename for unique report identification
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            reportPath = System.getProperty("user.dir") + File.separator + "test-output" + 
                        File.separator + "extent-reports" + File.separator + 
                        "TestReport_" + timestamp + ".html";
            
            // Create directories if they don't exist
            new File(reportPath).getParentFile().mkdirs();
            
            // Configure ExtentSparkReporter with HTML theme and settings
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Selenium Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            
            // Initialize ExtentReports and attach reporter
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            
            // Add system/environment information to the report
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Browser", ConfigReader.getProperty("browser"));
            extentReports.setSystemInfo("Environment", ConfigReader.getProperty("environment"));
            
            logger.info("ExtentReports instance created. Report path: " + reportPath);
        }
    }
    
    /**
     * Creates a new test entry in the report with the given name
     * 
     * @param testName The name of the test to appear in the report
     * @return ExtentTest object to log test steps and results
     */
    public static ExtentTest createTest(String testName) {
        return extentReports.createTest(testName);
    }
    
    /**
     * Creates a new test entry in the report with name and description
     * 
     * @param testName The name of the test
     * @param description Detailed description of what the test does
     * @return ExtentTest object to log test steps and results
     */
    public static ExtentTest createTest(String testName, String description) {
        return extentReports.createTest(testName, description);
    }
    
    /**
     * Writes all logged information to the HTML report file
     * Should be called at the end of test execution (typically in @AfterSuite)
     * Without calling flush(), no report file will be generated
     */
    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed. Report available at: " + reportPath);
        }
    }
    
    /**
     * Gets the file path where the report is saved
     * 
     * @return The absolute path to the generated HTML report
     */
    public static String getReportPath() {
        return reportPath;
    }
}