package com.qa.pages;

import com.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for SauceDemo Order Complete Confirmation Page (https://www.saucedemo.com/checkout-complete.html)
 */
public class CheckoutCompletePage {

    private final WebDriver driver;

    // Locators
    private final By headerTitle = By.className("title");
    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCheckoutCompletePageDisplayed() {
        return WaitUtils.waitForUrlToContain(driver, "/checkout-complete.html", 10)
                && WaitUtils.waitForVisibility(driver, headerTitle, 10).isDisplayed();
    }

    public String getCompleteHeader() {
        return WaitUtils.waitForVisibility(driver, completeHeader, 10).getText().trim();
    }

    public String getCompleteText() {
        return WaitUtils.waitForVisibility(driver, completeText, 10).getText().trim();
    }

    public InventoryPage backHome() {
        WaitUtils.waitForClickable(driver, backHomeButton, 10).click();
        InventoryPage inventoryPage = new InventoryPage(driver);
        WaitUtils.waitForUrlToContain(driver, "/inventory.html", 10);
        return inventoryPage;
    }
}
