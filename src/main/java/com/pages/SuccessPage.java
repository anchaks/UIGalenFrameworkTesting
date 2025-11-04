package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * SuccessPage - Page Object Model class representing the successful login/success page.
 * This page appears after a user successfully logs in.
 * Extends BasePage to inherit common functionality.
 */
public class SuccessPage extends BasePage {
    
    /** Main heading/title of the success page - Located by CSS selector "h1" */
    @FindBy(css = "h1")
    private WebElement successTitle;
    
    /** Success message content - Located by CSS class "post-title" */
    @FindBy(css = ".post-title")
    private WebElement successMessage;
    
    /** Logout link/button - Located by link text "Log out" */
    @FindBy(linkText = "Log out")
    private WebElement logoutLink;
    
    /**
     * Constructor - Initializes the SuccessPage with WebDriver
     * 
     * @param driver The WebDriver instance to control the browser
     */
    public SuccessPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Gets the main title text of the success page
     * Typically displays "Logged In Successfully" or similar
     * 
     * @return The text content of the success title
     */
    public String getSuccessTitle() {
        return getText(successTitle);
    }
    
    /**
     * Gets the success message displayed on the page
     * 
     * @return The text content of the success message
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }
    
    /**
     * Clicks the logout link to log out of the application
     */
    public void clickLogout() {
        click(logoutLink);
        logger.info("Clicked logout link");
    }
    
    /**
     * Checks if the logout link is visible on the page
     * 
     * @return true if logout link is displayed, false otherwise
     */
    public boolean isLogoutLinkDisplayed() {
        return isElementDisplayed(logoutLink);
    }
    
    /**
     * Verifies if the success page is properly loaded
     * Checks for presence of both success title and logout link
     * 
     * @return true if both key elements are displayed, false otherwise
     */
    public boolean isSuccessPageDisplayed() {
        return isElementDisplayed(successTitle) && isElementDisplayed(logoutLink);
    }
}