package com.qa.pages;

import com.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;

/**
 * Page Object Model for SauceDemo Product Details Page (https://www.saucedemo.com/inventory-item.html)
 */
public class ProductDetailsPage {

    private final WebDriver driver;

    // Locators
    private final By backToProductsButton = By.id("back-to-products");
    private final By productName = By.cssSelector(".inventory_details_name.large_size");
    private final By productDesc = By.cssSelector(".inventory_details_desc.large_size");
    private final By productPrice = By.className("inventory_details_price");
    private final By addToCartButton = By.cssSelector("button[id^='add-to-cart']");
    private final By removeButton = By.cssSelector("button[id^='remove']");

    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isProductDetailsPageDisplayed() {
        return WaitUtils.waitForUrlToContain(driver, "/inventory-item.html", 10)
                && WaitUtils.waitForVisibility(driver, productName, 10).isDisplayed();
    }

    public String getProductName() {
        return WaitUtils.waitForVisibility(driver, productName, 10).getText().trim();
    }

    public String getProductDescription() {
        return WaitUtils.waitForVisibility(driver, productDesc, 10).getText().trim();
    }

    public String getProductPrice() {
        return WaitUtils.waitForVisibility(driver, productPrice, 10).getText().trim();
    }

    public BigDecimal getProductPriceAsBigDecimal() {
        String cleanPrice = getProductPrice().replace("$", "").trim();
        return new BigDecimal(cleanPrice);
    }

    public void addToCart() {
        WaitUtils.waitForClickable(driver, addToCartButton, 10).click();
    }

    public void removeFromCart() {
        WaitUtils.waitForClickable(driver, removeButton, 10).click();
    }

    public InventoryPage backToProducts() {
        WaitUtils.waitForClickable(driver, backToProductsButton, 10).click();
        return new InventoryPage(driver);
    }
}
