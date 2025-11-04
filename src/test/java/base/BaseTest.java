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

public class BaseTest {
    
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ExtentTest extentTest;
    
    // Shared list for all Galen test results
    protected static final List<GalenTestInfo> galenTests = new LinkedList<>();
    
    @BeforeClass
    public void setUpClass() {
        logger.info("Starting test class: " + this.getClass().getSimpleName());
        ExtentReportManager.createInstance();
    }
    
    @BeforeMethod
    public void setUp(ITestResult result) {
        logger.info("Setting up test: " + result.getMethod().getMethodName());
        
        // Initialize WebDriver
        String browserName = ConfigReader.getProperty("browser");
        driver = DriverManager.getDriver(browserName);
        
        // Create ExtentTest instance
        extentTest = ExtentReportManager.createTest(result.getMethod().getMethodName());
        extentTest.info("Test started: " + result.getMethod().getMethodName());
        
        logger.info("WebDriver initialized for browser: " + browserName);
    }
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test failed: " + result.getMethod().getMethodName());
            extentTest.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
            
            // Capture and attach screenshot
            String base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64(driver);
            if (base64Screenshot != null) {
                extentTest.addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
                logger.info("Screenshot captured and attached to report");
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed: " + result.getMethod().getMethodName());
            extentTest.log(Status.PASS, "Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("Test skipped: " + result.getMethod().getMethodName());
            extentTest.log(Status.SKIP, "Test Skipped: " + result.getThrowable().getMessage());
        }
        
        // Quit WebDriver
        DriverManager.quitDriver();
        logger.info("WebDriver session ended");
    }
    
    @AfterClass
    public void tearDownClass() {
        logger.info("Finished test class: " + this.getClass().getSimpleName());
    }
    
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
        
        ExtentReportManager.flush();
        logger.info("Test suite execution completed");
    }
}