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
    
    /** Logger instance for logging driver operations */
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    
    /** ThreadLocal storage for WebDriver - ensures thread-safe parallel execution */
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * Gets the WebDriver instance for the current thread
     * Creates a new driver if one doesn't exist for this thread
     * 
     * @param browserName The browser to launch (chrome, firefox, or edge)
     * @return WebDriver instance for the current thread
     */
    public static WebDriver getDriver(String browserName) {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver(browserName));
        }
        return driverThreadLocal.get();
    }
    
    /**
     * Creates and configures a new WebDriver instance based on browser name
     * Automatically downloads and sets up the appropriate driver binary using WebDriverManager
     * Configures browser options including headless mode if specified in config
     * 
     * @param browserName The browser to create (chrome, firefox, or edge) - case insensitive
     * @return Configured WebDriver instance
     * @throws RuntimeException if browser is not supported
     */
    private static WebDriver createDriver(String browserName) {
        WebDriver driver = null;
        
        switch (browserName.toLowerCase()) {
            case "chrome":
                // Setup Chrome driver binary automatically using WebDriverManager
                WebDriverManager.chromedriver().setup();
                
                // Configure Chrome options for optimal test execution
                ChromeOptions chromeOptions = new ChromeOptions();

                // Disable browser extensions to prevent interference with tests
                chromeOptions.addArguments("--disable-extensions");
                
                // Overcome limited resource problems in Docker/containerized environments
                // /dev/shm partition is too small in Docker (64MB default), this flag prevents crashes
                chromeOptions.addArguments("--disable-dev-shm-usage");
                
                // Bypass OS security model - required for running in containers or as root
                chromeOptions.addArguments("--no-sandbox");
                
                // Disable GPU hardware acceleration - reduces crashes on Windows OS and CI servers
                chromeOptions.addArguments("--disable-gpu");

                
                // Automatically enable headless mode for CI/CD pipeline execution
                // Checks three conditions:
                // 1. JENKINS_HOME environment variable (set by Jenkins)
                // 2. CI environment variable (set by most CI/CD platforms like GitHub Actions, GitLab CI)
                // 3. headless property in config.properties file
                if (System.getenv("JENKINS_HOME") != null ||
                    System.getenv("CI") != null ||
                    Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    logger.info("Running in Jenkins/CI - enabling headless mode");
                    // Use --headless=new for modern Chrome (v109+) - more stable than old headless mode
                    chromeOptions.addArguments("--headless=new");
                }
                
                // Create and return Chrome WebDriver instance with configured options
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                // Setup Firefox driver binary automatically using WebDriverManager
                WebDriverManager.firefoxdriver().setup();
                
                // Configure Firefox options
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                // Automatically enable headless mode for CI/CD pipeline execution
                // Checks for Jenkins, CI environment variables, or config property
                if (System.getenv("JENKINS_HOME") != null ||
                    System.getenv("CI") != null ||
                    Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    // Enable headless mode for Firefox
                    firefoxOptions.addArguments("--headless");
                }

                // Create and return Firefox WebDriver instance with configured options
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                // Setup Edge driver binary automatically using WebDriverManager
                WebDriverManager.edgedriver().setup();
                
                // Configure Edge options
                EdgeOptions edgeOptions = new EdgeOptions();

                // Automatically enable headless mode for CI/CD pipeline execution
                // Checks for Jenkins, CI environment variables, or config property
                if (System.getenv("JENKINS_HOME") != null ||
                    System.getenv("CI") != null ||
                    Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    // Use --headless=new for modern Edge (Chromium-based) - more stable
                    edgeOptions.addArguments("--headless=new");
                }

                // Create and return Edge WebDriver instance with configured options
                driver = new EdgeDriver(edgeOptions);
                break;
                
            default:
                logger.error("Browser not supported: " + browserName);
                throw new RuntimeException("Browser not supported: " + browserName);
        }
        
        // Maximize browser window for consistent test execution
        driver.manage().window().maximize();
        logger.info("WebDriver created for browser: " + browserName);
        return driver;
    }
    
    /**
     * Quits the WebDriver instance for the current thread and removes it from ThreadLocal
     * Should be called in test teardown to properly clean up browser sessions
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove(); // Clean up ThreadLocal to prevent memory leaks
            logger.info("WebDriver session closed");
        }
    }
}