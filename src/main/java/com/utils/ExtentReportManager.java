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

public class ExtentReportManager {
    
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static String reportPath;
    
    public static void createInstance() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            reportPath = System.getProperty("user.dir") + File.separator + "test-output" + 
                        File.separator + "extent-reports" + File.separator + 
                        "TestReport_" + timestamp + ".html";
            
            // Create directories if they don't exist
            new File(reportPath).getParentFile().mkdirs();
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Selenium Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Browser", ConfigReader.getProperty("browser"));
            extentReports.setSystemInfo("Environment", ConfigReader.getProperty("environment"));
            
            logger.info("ExtentReports instance created. Report path: " + reportPath);
        }
    }
    
    public static ExtentTest createTest(String testName) {
        return extentReports.createTest(testName);
    }
    
    public static ExtentTest createTest(String testName, String description) {
        return extentReports.createTest(testName, description);
    }
    
    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed. Report available at: " + reportPath);
        }
    }
    
    public static String getReportPath() {
        return reportPath;
    }
}