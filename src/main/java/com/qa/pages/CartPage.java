package com.qa.pages;

import com.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for SauceDemo Cart Page (https://www.saucedemo.com/cart.html)
 */
public class CartPage {

    private final WebDriver driver;

    // Locators
    private final By headerTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCartPageDisplayed() {
        return WaitUtils.waitForUrlToContain(driver, "/cart.html", 10)
                && WaitUtils.waitForVisibility(driver, headerTitle, 10).isDisplayed();
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public int getCartBadgeCount() {
        try {
            List<WebElement> badges = driver.findElements(shoppingCartBadge);
            if (badges.isEmpty() || !badges.get(0).isDisplayed()) {
                return 0;
            }
            return Integer.parseInt(badges.get(0).getText().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public int waitForCartBadgeCount(int expectedCount, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        try {
            wait.until(d -> getCartBadgeCount() == expectedCount);
        } catch (Exception ignored) {
        }
        return getCartBadgeCount();
    }

    public List<String> getCartItemNames() {
        List<WebElement> elements = driver.findElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public List<BigDecimal> getCartItemPricesAsBigDecimal() {
        List<WebElement> elements = driver.findElements(itemPrices);
        List<BigDecimal> prices = new ArrayList<>();
        for (WebElement el : elements) {
            String text = el.getText().replace("$", "").trim();
            prices.add(new BigDecimal(text));
        }
        return prices;
    }

    public void removeItemByName(String productName) {
        String formattedName = productName.toLowerCase().replace(" ", "-");
        By removeButton = By.id("remove-" + formattedName);
        WaitUtils.waitForClickable(driver, removeButton, 10).click();
    }

    public void removeAllItems() {
        List<String> currentItems = getCartItemNames();
        for (String itemName : currentItems) {
            removeItemByName(itemName);
        }
    }

    public InventoryPage continueShopping() {
        WaitUtils.waitForClickable(driver, continueShoppingButton, 10).click();
        return new InventoryPage(driver);
    }

    public CheckoutInformationPage proceedToCheckout() {
        WaitUtils.waitForClickable(driver, checkoutButton, 10).click();
        return new CheckoutInformationPage(driver);
    }
}
