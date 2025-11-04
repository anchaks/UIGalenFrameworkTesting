package com.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

/**
 * BasePage - Abstract base class for all Page Object Model (POM) classes.
 * Provides common functionality like WebDriver management, explicit waits,
 * logging, and reusable methods for interacting with web elements.
 * All page classes should extend this base class to inherit these utilities.
 */
public class BasePage {
    
    /** Logger instance for logging page actions and events */
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    
    /** WebDriver instance to control the browser */
    protected WebDriver driver;
    
    /** WebDriverWait instance for explicit waits with 10-second timeout */
    protected WebDriverWait wait;
    
    /**
     * Constructor - Initializes the page with WebDriver and sets up waits
     * Automatically initializes all @FindBy annotated elements using PageFactory
     * 
     * @param driver The WebDriver instance to use for this page
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this); // Initialize all @FindBy elements
        logger.info("Initialized page: " + this.getClass().getSimpleName());
    }
    
    /**
     * Waits for a web element to be visible in the DOM
     * Uses explicit wait with timeout defined in the wait object
     * 
     * @param element The WebElement to wait for
     */
    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Waits for a web element to be clickable (visible and enabled)
     * 
     * @param element The WebElement to wait for
     */
    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Safely sends text to an input field
     * Waits for visibility, clears existing content, then enters new text
     * 
     * @param element The input WebElement to type into
     * @param text The text string to enter
     */
    protected void sendKeys(WebElement element, String text) {
        waitForElementToBeVisible(element);
        element.clear(); // Clear existing text first
        element.sendKeys(text);
        logger.info("Entered text: " + text + " in element: " + element.toString());
    }
    
    /**
     * Safely clicks a web element
     * Waits for element to be clickable before performing the click action
     * 
     * @param element The WebElement to click
     */
    protected void click(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
        logger.info("Clicked on element: " + element.toString());
    }
    
    /**
     * Retrieves the visible text content of an element
     * Waits for element visibility before retrieving text
     * 
     * @param element The WebElement to get text from
     * @return The text content of the element
     */
    protected String getText(WebElement element) {
        waitForElementToBeVisible(element);
        String text = element.getText();
        logger.info("Retrieved text: " + text + " from element: " + element.toString());
        return text;
    }
    
    /**
     * Checks if an element is currently displayed on the page
     * Catches exceptions if element is not found or not visible
     * 
     * @param element The WebElement to check
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            logger.warn("Element not displayed: " + element.toString());
            return false;
        }
    }
}