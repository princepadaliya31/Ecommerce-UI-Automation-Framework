package com.qa.pages;

import com.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object Model for SauceDemo Login Page (https://www.saucedemo.com/)
 * Encapsulates element locators and page actions without test assertions.
 */
public class LoginPage {

    private final WebDriver driver;

    // Private Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessageContainer = By.cssSelector("h3[data-test='error']");
    private final By loginLogo = By.className("login_logo");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLoginPageDisplayed() {
        return WaitUtils.waitForVisibility(driver, loginLogo, 10).isDisplayed();
    }

    public LoginPage enterUsername(String username) {
        WebElement element = WaitUtils.waitForVisibility(driver, usernameInput, 10);
        element.clear();
        if (username != null && !username.isEmpty()) {
            element.sendKeys(username);
        }
        return this;
    }

    public LoginPage enterPassword(String password) {
        WebElement element = WaitUtils.waitForVisibility(driver, passwordInput, 10);
        element.clear();
        if (password != null && !password.isEmpty()) {
            element.sendKeys(password);
        }
        return this;
    }

    public void clickLogin() {
        WaitUtils.waitForClickable(driver, loginButton, 10).click();
    }

    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new InventoryPage(driver);
    }

    public String getErrorMessage() {
        WebElement element = WaitUtils.waitForVisibility(driver, errorMessageContainer, 5);
        return element.getText().trim();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return WaitUtils.waitForVisibility(driver, errorMessageContainer, 3).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordMasked() {
        WebElement element = WaitUtils.waitForVisibility(driver, passwordInput, 10);
        String typeAttribute = element.getAttribute("type");
        return "password".equalsIgnoreCase(typeAttribute);
    }
}
