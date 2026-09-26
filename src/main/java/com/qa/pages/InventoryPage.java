package com.qa.pages;

import com.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for SauceDemo Inventory / Products Page (https://www.saucedemo.com/inventory.html)
 */
public class InventoryPage {

    private final WebDriver driver;

    // Locators
    private final By headerTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemDescs = By.className("inventory_item_desc");
    private final By itemPrices = By.className("inventory_item_price");
    private final By itemImages = By.cssSelector(".inventory_item_img img");
    private final By sortSelect = By.className("product_sort_container");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By shoppingCartLink = By.className("shopping_cart_link");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutSidebarLink = By.id("logout_sidebar_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isInventoryPageDisplayed() {
        return WaitUtils.waitForUrlToContain(driver, "/inventory.html", 10)
                && WaitUtils.waitForVisibility(driver, headerTitle, 10).isDisplayed();
    }

    public String getHeaderTitle() {
        return WaitUtils.waitForVisibility(driver, headerTitle, 10).getText().trim();
    }

    public int getItemCount() {
        List<WebElement> items = driver.findElements(inventoryItems);
        return items.size();
    }

    public List<String> getProductNames() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, itemNames, 10);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public List<String> getProductDescriptions() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, itemDescs, 10);
        List<String> descs = new ArrayList<>();
        for (WebElement el : elements) {
            descs.add(el.getText().trim());
        }
        return descs;
    }

    public List<BigDecimal> getProductPricesAsBigDecimal() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, itemPrices, 10);
        List<BigDecimal> prices = new ArrayList<>();
        for (WebElement el : elements) {
            String cleanPrice = el.getText().replace("$", "").trim();
            prices.add(new BigDecimal(cleanPrice));
        }
        return prices;
    }

    public List<String> getProductImageSources() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, itemImages, 10);
        List<String> sources = new ArrayList<>();
        for (WebElement el : elements) {
            sources.add(el.getAttribute("src"));
        }
        return sources;
    }

    public boolean areAllProductImagesDisplayed() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, itemImages, 10);
        if (elements.isEmpty()) return false;
        for (WebElement el : elements) {
            if (!el.isDisplayed() || el.getAttribute("src") == null || el.getAttribute("src").isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public BigDecimal getProductPriceByName(String productName) {
        By priceLocator = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item_description']//div[@class='inventory_item_price']");
        String text = WaitUtils.waitForVisibility(driver, priceLocator, 10).getText().replace("$", "").trim();
        return new BigDecimal(text);
    }

    public void addItemToCartByName(String productName) {
        String formattedName = productName.toLowerCase().replace(" ", "-");
        By addToCartButton = By.id("add-to-cart-" + formattedName);
        WaitUtils.waitForClickable(driver, addToCartButton, 10).click();
    }

    public void removeItemByName(String productName) {
        String formattedName = productName.toLowerCase().replace(" ", "-");
        By removeButton = By.id("remove-" + formattedName);
        WaitUtils.waitForClickable(driver, removeButton, 10).click();
    }

    public ProductDetailsPage clickProductTitleByName(String productName) {
        By productTitleLocator = By.xpath("//div[@class='inventory_item_name ' and text()='" + productName + "']");
        WaitUtils.waitForClickable(driver, productTitleLocator, 10).click();
        return new ProductDetailsPage(driver);
    }

    public ProductDetailsPage clickFirstProductTitle() {
        List<WebElement> elements = WaitUtils.waitForAllVisible(driver, itemNames, 10);
        elements.get(0).click();
        return new ProductDetailsPage(driver);
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

    public CartPage goToCart() {
        WaitUtils.waitForClickable(driver, shoppingCartLink, 10).click();
        CartPage cartPage = new CartPage(driver);
        WaitUtils.waitForUrlToContain(driver, "/cart.html", 10);
        return cartPage;
    }

    public void selectSortOption(String optionValue) {
        WebElement dropdown = WaitUtils.waitForVisibility(driver, sortSelect, 10);
        Select select = new Select(dropdown);
        select.selectByValue(optionValue);
    }

    public LoginPage logout() {
        WaitUtils.waitForClickable(driver, burgerMenuButton, 10).click();
        WaitUtils.waitForClickable(driver, logoutSidebarLink, 10).click();
        return new LoginPage(driver);
    }
}
