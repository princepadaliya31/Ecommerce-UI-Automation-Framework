package com.qa.pages;

import com.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for SauceDemo Checkout Step Two: Overview (https://www.saucedemo.com/checkout-step-two.html)
 */
public class CheckoutOverviewPage {

    private final WebDriver driver;

    // Locators
    private final By headerTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By paymentInfoValue = By.cssSelector("div[data-test='payment-info-value']");
    private final By shippingInfoValue = By.cssSelector("div[data-test='shipping-info-value']");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCheckoutOverviewPageDisplayed() {
        return WaitUtils.waitForUrlToContain(driver, "/checkout-step-two.html", 10)
                && WaitUtils.waitForVisibility(driver, headerTitle, 10).isDisplayed();
    }

    public int getItemCount() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, cartItems, 10);
        return elements.size();
    }

    public List<String> getItemNames() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, itemNames, 10);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public List<BigDecimal> getItemPricesAsBigDecimal() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, itemPrices, 10);
        List<BigDecimal> prices = new ArrayList<>();
        for (WebElement el : elements) {
            String text = el.getText().replace("$", "").trim();
            prices.add(new BigDecimal(text));
        }
        return prices;
    }

    public String getPaymentInfo() {
        return WaitUtils.waitForVisibility(driver, paymentInfoValue, 10).getText().trim();
    }

    public String getShippingInfo() {
        return WaitUtils.waitForVisibility(driver, shippingInfoValue, 10).getText().trim();
    }

    public BigDecimal getSubtotalAsBigDecimal() {
        String text = WaitUtils.waitForVisibility(driver, subtotalLabel, 10).getText();
        return parseAmount(text);
    }

    public BigDecimal getTaxAsBigDecimal() {
        String text = WaitUtils.waitForVisibility(driver, taxLabel, 10).getText();
        return parseAmount(text);
    }

    public BigDecimal getTotalAsBigDecimal() {
        String text = WaitUtils.waitForVisibility(driver, totalLabel, 10).getText();
        return parseAmount(text);
    }

    private BigDecimal parseAmount(String text) {
        int dollarIndex = text.indexOf("$");
        if (dollarIndex != -1) {
            String cleanAmount = text.substring(dollarIndex + 1).trim();
            return new BigDecimal(cleanAmount);
        }
        return BigDecimal.ZERO;
    }

    public CheckoutCompletePage finishCheckout() {
        WaitUtils.waitForClickable(driver, finishButton, 10).click();
        CheckoutCompletePage completePage = new CheckoutCompletePage(driver);
        WaitUtils.waitForUrlToContain(driver, "/checkout-complete.html", 10);
        return completePage;
    }

    public InventoryPage cancelCheckout() {
        WaitUtils.waitForClickable(driver, cancelButton, 10).click();
        InventoryPage inventoryPage = new InventoryPage(driver);
        WaitUtils.waitForUrlToContain(driver, "/inventory.html", 10);
        return inventoryPage;
    }
}
