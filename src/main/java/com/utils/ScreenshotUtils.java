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

public class ScreenshotUtils {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    
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
    
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String screenshotPath = System.getProperty("user.dir") + File.separator + "test-output" + 
                                  File.separator + "screenshots" + File.separator + 
                                  testName + "_" + timestamp + ".png";
            
            File destinationFile = new File(screenshotPath);
            destinationFile.getParentFile().mkdirs();
            
            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot saved at: " + screenshotPath);
            return screenshotPath;
            
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
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