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

public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
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
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-gpu");

                // ✅ Automatically enable headless mode inside Jenkins or CI
                if (System.getenv("JENKINS_HOME") != null ||
                    System.getenv("CI") != null ||
                    Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    logger.info("Running in Jenkins/CI - enabling headless mode");
                    chromeOptions.addArguments("--headless=new"); // safer for modern Chrome
                }

                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (System.getenv("JENKINS_HOME") != null ||
                    System.getenv("CI") != null ||
                    Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    firefoxOptions.addArguments("--headless");
                }

                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();

                if (System.getenv("JENKINS_HOME") != null ||
                    System.getenv("CI") != null ||
                    Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                    edgeOptions.addArguments("--headless=new");
                }

                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                logger.error("Browser not supported: " + browserName);
                throw new RuntimeException("Browser not supported: " + browserName);
        }

        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.warn("Window maximize skipped (headless mode may not support it)");
        }

        logger.info("WebDriver created for browser: " + browserName);
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            logger.info("WebDriver session closed");
        }
    }
}
