package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object Model (POM) class representing the Login page.
 * This class uses Selenium's PageFactory pattern with @FindBy annotations
 * to locate and interact with web elements on the login page.
 * Extends BasePage to inherit common functionality like waits and logging.
 */
public class LoginPage extends BasePage {
    
    // Web element locators using @FindBy annotations
    // These elements are automatically initialized by PageFactory in the BasePage constructor
    
    /** Username input field - Located by HTML id="username" */
    @FindBy(id = "username")
    private WebElement usernameField;
    
    /** Password input field - Located by HTML id="password" */
    @FindBy(id = "password")
    private WebElement passwordField;
    
    /** Submit/Login button - Located by HTML id="submit" */
    @FindBy(id = "submit")
    private WebElement loginButton;
    
    /** Error message element - Displays validation errors - Located by HTML id="error" */
    @FindBy(id = "error")
    private WebElement errorMessage;
    
    /** Page title/heading element - Located by CSS selector "h2" */
    @FindBy(css = "h2")
    private WebElement pageTitle;
    
    /**
     * Constructor - Initializes the LoginPage with WebDriver
     * Calls super() to initialize PageFactory elements in BasePage
     * 
     * @param driver The WebDriver instance to control the browser
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Enters the username into the username field
     * Waits for element visibility and clears existing text before typing
     * 
     * @param username The username string to enter
     */
    public void enterUsername(String username) {
        sendKeys(usernameField, username);
        logger.info("Entered username: " + username);
    }
    
    /**
     * Enters the password into the password field
     * Waits for element visibility and clears existing text before typing
     * 
     * @param password The password string to enter
     */
    public void enterPassword(String password) {
        sendKeys(passwordField, password);
        logger.info("Entered password");
    }
    
    /**
     * Clicks the login/submit button
     * Waits for element to be clickable before clicking
     */
    public void clickLoginButton() {
        click(loginButton);
        logger.info("Clicked login button");
    }
    
    /**
     * Performs complete login action with username and password
     * Composite method that combines enterUsername, enterPassword, and clickLoginButton
     * 
     * @param username The username to login with
     * @param password The password to login with
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("Performed login with username: " + username);
    }
    
    /**
     * Retrieves the error message text if displayed
     * Used to validate login failures or validation errors
     * 
     * @return The error message text, or empty string if not displayed
     */
    public String getErrorMessage() {
        if (isElementDisplayed(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
    
    /**
     * Gets the page title/heading text
     * 
     * @return The text content of the page title element
     */
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    /**
     * Checks if the login button is visible on the page
     * 
     * @return true if login button is displayed, false otherwise
     */
    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginButton);
    }
    
    /**
     * Checks if the username field is visible on the page
     * 
     * @return true if username field is displayed, false otherwise
     */
    public boolean isUsernameFieldDisplayed() {
        return isElementDisplayed(usernameField);
    }
    
    /**
     * Checks if the password field is visible on the page
     * 
     * @return true if password field is displayed, false otherwise
     */
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordField);
    }
}