package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "submit")
    private WebElement loginButton;
    
    @FindBy(id = "error")
    private WebElement errorMessage;
    
    @FindBy(css = "h2")
    private WebElement pageTitle;
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void enterUsername(String username) {
        sendKeys(usernameField, username);
        logger.info("Entered username: " + username);
    }
    
    public void enterPassword(String password) {
        sendKeys(passwordField, password);
        logger.info("Entered password");
    }
    
    public void clickLoginButton() {
        click(loginButton);
        logger.info("Clicked login button");
    }
    
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("Performed login with username: " + username);
    }
    
    public String getErrorMessage() {
        if (isElementDisplayed(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
    
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginButton);
    }
    
    public boolean isUsernameFieldDisplayed() {
        return isElementDisplayed(usernameField);
    }
    
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordField);
    }
}