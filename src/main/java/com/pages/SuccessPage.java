package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SuccessPage extends BasePage {
    
    @FindBy(css = "h1")
    private WebElement successTitle;
    
    @FindBy(css = ".post-title")
    private WebElement successMessage;
    
    @FindBy(linkText = "Log out")
    private WebElement logoutLink;
    
    public SuccessPage(WebDriver driver) {
        super(driver);
    }
    
    public String getSuccessTitle() {
        return getText(successTitle);
    }
    
    public String getSuccessMessage() {
        return getText(successMessage);
    }
    
    public void clickLogout() {
        click(logoutLink);
        logger.info("Clicked logout link");
    }
    
    public boolean isLogoutLinkDisplayed() {
        return isElementDisplayed(logoutLink);
    }
    
    public boolean isSuccessPageDisplayed() {
        return isElementDisplayed(successTitle) && isElementDisplayed(logoutLink);
    }
}