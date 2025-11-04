package base;

import com.utils.ConfigReader;
import com.utils.DriverManager;
import com.utils.ExtentReportManager;
import com.utils.ScreenshotUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.galenframework.reports.GalenTestInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.LinkedList;
import java.util.List;

/**
 * BaseTest - Abstract base class for all TestNG test classes.
 * Provides common test setup, teardown, and reporting functionality.
 * Manages WebDriver lifecycle, ExtentReports integration, and Galen report generation.
 * All test classes should extend this base class to inherit these capabilities.
 */
public class BaseTest {
    
    /** Logger instance for logging test execution events */
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    
    /** WebDriver instance for browser automation - one per test method */
    protected WebDriver driver;
    
    /** ExtentTest instance for logging test steps and results to HTML report */
    protected ExtentTest extentTest;
    
    /** Shared list for all Galen test results - static to collect across all test classes */
    protected static final List<GalenTestInfo> galenTests = new LinkedList<>();
    
    /**
     * Setup method executed once before all tests in the class
     * Initializes the ExtentReports instance for HTML reporting
     */
    @BeforeClass
    public void setUpClass() {
        logger.info("Starting test class: " + this.getClass().getSimpleName());
        ExtentReportManager.createInstance();
    }
    
    /**
     * Setup method executed before each test method
     * Initializes WebDriver and creates ExtentTest entry for the test
     * 
     * @param result ITestResult object containing test method metadata
     */
    @BeforeMethod
    public void setUp(ITestResult result) {
        logger.info("Setting up test: " + result.getMethod().getMethodName());
        
        // Initialize WebDriver with browser from config file
        String browserName = ConfigReader.getProperty("browser");
        driver = DriverManager.getDriver(browserName);
        
        // Create ExtentTest instance for this test
        extentTest = ExtentReportManager.createTest(result.getMethod().getMethodName());
        extentTest.info("Test started: " + result.getMethod().getMethodName());
        
        logger.info("WebDriver initialized for browser: " + browserName);
    }
    
    /**
     * Teardown method executed after each test method
     * Handles test result logging, screenshot capture on failure, and WebDriver cleanup
     * 
     * @param result ITestResult object containing test execution result and status
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        // Handle test failure - log error and capture screenshot
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test failed: " + result.getMethod().getMethodName());
            extentTest.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
            
            // Capture and attach screenshot to report
            String base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64(driver);
            if (base64Screenshot != null) {
                extentTest.addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
                logger.info("Screenshot captured and attached to report");
            }
        } 
        // Handle test success
        else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed: " + result.getMethod().getMethodName());
            extentTest.log(Status.PASS, "Test Passed");
        } 
        // Handle test skip
        else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("Test skipped: " + result.getMethod().getMethodName());
            extentTest.log(Status.SKIP, "Test Skipped: " + result.getThrowable().getMessage());
        }
        
        // Quit WebDriver and clean up resources
        DriverManager.quitDriver();
        logger.info("WebDriver session ended");
    }
    
    /**
     * Teardown method executed once after all tests in the class
     */
    @AfterClass
    public void tearDownClass() {
        logger.info("Finished test class: " + this.getClass().getSimpleName());
    }
    
    /**
     * Teardown method executed once after all tests in the suite
     * Generates the Galen HTML report with all collected layout test results
     * Flushes ExtentReports to write the final HTML report file
     */
    @AfterSuite
    public void tearDownSuite() {
        // Generate Galen HTML report with all test results
        if (!galenTests.isEmpty()) {
            try {
                com.galenframework.reports.HtmlReportBuilder reportBuilder = new com.galenframework.reports.HtmlReportBuilder();
                reportBuilder.build(galenTests, "target/galen-reports");
                logger.info("Galen HTML report generated at: target/galen-reports");
            } catch (Exception e) {
                logger.error("Failed to generate Galen HTML report", e);
            }
        }
        
        // Flush ExtentReports to write the final report
        ExtentReportManager.flush();
        logger.info("Test suite execution completed");
    }
}