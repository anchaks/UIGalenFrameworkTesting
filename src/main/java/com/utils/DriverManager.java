package com.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * DriverManager - Manages WebDriver instances using ThreadLocal for parallel test execution.
 * Handles WebDriver creation, configuration, and lifecycle for Chrome, Firefox, and Edge browsers.
 * Uses WebDriverManager library for automatic driver binary management.
 * ThreadLocal ensures each thread gets its own WebDriver instance for safe parallel execution.
 */
public class DriverManager {
    
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    
    /** ThreadLocal storage for WebDriver - ensures thread-safe parallel execution */
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    public static WebDriver getDriver(String browserName) {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver(browserName));
        }
        return driverThreadLocal.get();
    }
    
    private static WebDriver createDriver(String browserName) {
        WebDriver driver = null;

        switch (browserName.toLowerCase()) {
            case "chrome":
                // Setup Chrome driver binary automatically using WebDriverManager
                WebDriverManager.chromedriver().setup();
                
                // Configure Chrome options for optimal test execution
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-extensions");
                
                // Overcome limited resource problems in Docker/containerized environments
                // /dev/shm partition is too small in Docker (64MB default), this flag prevents crashes
                chromeOptions.addArguments("--disable-dev-shm-usage");
                
                // Bypass OS security model - required for running in containers or as root
                chromeOptions.addArguments("--no-sandbox");
                if (Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    chromeOptions.addArguments("--headless");
                }
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                // Setup Firefox driver binary automatically using WebDriverManager
                WebDriverManager.firefoxdriver().setup();
                
                // Configure Firefox options
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                // Setup Edge driver binary automatically using WebDriverManager
                WebDriverManager.edgedriver().setup();
                
                // Configure Edge options
                EdgeOptions edgeOptions = new EdgeOptions();
                if (Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                logger.error("Browser not supported: " + browserName);
                throw new RuntimeException("Browser not supported: " + browserName);
        }
        
        driver.manage().window().maximize();
        logger.info("WebDriver created for browser: " + browserName);
        return driver;
    }
    
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove(); // Clean up ThreadLocal to prevent memory leaks
            logger.info("WebDriver session closed");
        }
    }
}
