package com.qa.pages;

import com.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object Model for SauceDemo Checkout Step One: Information (https://www.saucedemo.com/checkout-step-one.html)
 */
public class CheckoutInformationPage {

    private final WebDriver driver;

    // Locators
    private final By headerTitle = By.className("title");
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessageContainer = By.cssSelector("h3[data-test='error']");

    public CheckoutInformationPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCheckoutInformationPageDisplayed() {
        return WaitUtils.waitForUrlToContain(driver, "/checkout-step-one.html", 10)
                && WaitUtils.waitForVisibility(driver, headerTitle, 10).isDisplayed();
    }

    public CheckoutInformationPage enterFirstName(String firstName) {
        WebElement el = WaitUtils.waitForVisibility(driver, firstNameInput, 10);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        if (firstName != null && !firstName.isEmpty()) {
            el.sendKeys(firstName);
        }
        return this;
    }

    public CheckoutInformationPage enterLastName(String lastName) {
        WebElement el = WaitUtils.waitForVisibility(driver, lastNameInput, 10);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        if (lastName != null && !lastName.isEmpty()) {
            el.sendKeys(lastName);
        }
        return this;
    }

    public CheckoutInformationPage enterPostalCode(String postalCode) {
        WebElement el = WaitUtils.waitForVisibility(driver, postalCodeInput, 10);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        if (postalCode != null && !postalCode.isEmpty()) {
            el.sendKeys(postalCode);
        }
        return this;
    }

    public void fillInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public void clickContinue() {
        WaitUtils.waitForClickable(driver, continueButton, 10).click();
    }

    public CheckoutOverviewPage continueToOverview(String firstName, String lastName, String postalCode) {
        fillInformation(firstName, lastName, postalCode);
        clickContinue();
        CheckoutOverviewPage overviewPage = new CheckoutOverviewPage(driver);
        WaitUtils.waitForUrlToContain(driver, "/checkout-step-two.html", 10);
        return overviewPage;
    }

    public CartPage cancelCheckout() {
        WaitUtils.waitForClickable(driver, cancelButton, 10).click();
        CartPage cartPage = new CartPage(driver);
        WaitUtils.waitForUrlToContain(driver, "/cart.html", 10);
        return cartPage;
    }

    public String getErrorMessage() {
        return WaitUtils.waitForVisibility(driver, errorMessageContainer, 10).getText().trim();
    }
}
