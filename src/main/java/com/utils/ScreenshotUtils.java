package com.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * ScreenshotUtils - Utility class for capturing and managing screenshots during test execution.
 * Provides methods to capture screenshots in different formats (Base64, file)
 * and convert between formats. Useful for test reporting and debugging failures.
 */
public class ScreenshotUtils {
    
    /** Logger instance for logging screenshot operations */
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    
    /**
     * Captures a screenshot and returns it as a Base64-encoded string
     * Base64 format is useful for embedding screenshots directly in HTML reports
     * without needing separate image files
     * 
     * @param driver The WebDriver instance of the current browser session
     * @return Base64-encoded screenshot string, or null if capture fails
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            String base64Screenshot = takesScreenshot.getScreenshotAs(OutputType.BASE64);
            logger.info("Screenshot captured as Base64 string");
            return base64Screenshot;
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as Base64: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Captures a screenshot and saves it as a PNG file with timestamp
     * File is saved in test-output/screenshots directory with a unique timestamped name
     * 
     * @param driver The WebDriver instance of the current browser session
     * @param testName The name of the test (used in the screenshot filename)
     * @return The absolute path to the saved screenshot file, or null if capture fails
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            // Generate timestamped filename to avoid overwrites
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String screenshotPath = System.getProperty("user.dir") + File.separator + "test-output" + 
                                  File.separator + "screenshots" + File.separator + 
                                  testName + "_" + timestamp + ".png";
            
            // Create directory if it doesn't exist
            File destinationFile = new File(screenshotPath);
            destinationFile.getParentFile().mkdirs();
            
            // Copy screenshot file to destination
            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot saved at: " + screenshotPath);
            return screenshotPath;
            
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Converts an existing screenshot file to Base64-encoded string
     * Useful for converting saved PNG files to Base64 for report embedding
     * 
     * @param filePath The absolute path to the screenshot file
     * @return Base64-encoded string of the file content, or null if conversion fails
     */
    public static String convertFileToBase64(String filePath) {
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            logger.error("Failed to convert file to Base64: " + e.getMessage());
            return null;
        }
    }
}