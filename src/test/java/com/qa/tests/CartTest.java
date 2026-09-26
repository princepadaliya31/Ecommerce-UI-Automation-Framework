package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.pages.CartPage;
import com.qa.pages.InventoryPage;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Epic("Cart Module")
@Feature("Shopping Cart Management")
public class CartTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToInventory() {
        LoginPage loginPage = new LoginPage(getDriver());
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page must be displayed after login.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"},
          description = "TC_CART_001: Verify adding a single product to cart updates cart state")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add Single Product")
    public void testTC_CART_001_AddSingleProduct() {
        String targetProduct = "Sauce Labs Backpack";
        inventoryPage.addItemToCartByName(targetProduct);
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(1, 5), 1, "Inventory cart badge count should be 1.");

        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed.");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should contain 1 item.");
        Assert.assertEquals(cartPage.getCartBadgeCount(), 1, "Cart badge count on Cart page should be 1.");
        Assert.assertEquals(cartPage.getCartItemNames().get(0), targetProduct, "Cart item name should match.");
    }

    @Test(priority = 2, groups = {"regression"},
          description = "TC_CART_002: Verify adding multiple products to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add Multiple Products")
    public void testTC_CART_002_AddMultipleProducts() {
        List<String> targetProducts = Arrays.asList("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Fleece Jacket");
        for (String product : targetProducts) {
            inventoryPage.addItemToCartByName(product);
        }
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(3, 5), 3, "Inventory cart badge count should be 3.");

        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 3, "Cart item count should be 3.");
        Assert.assertEquals(cartPage.getCartBadgeCount(), 3, "Cart badge count should be 3.");

        List<String> actualNames = cartPage.getCartItemNames();
        for (String expected : targetProducts) {
            Assert.assertTrue(actualNames.contains(expected), "Cart should contain product: " + expected);
        }
    }

    @Test(priority = 3, groups = {"regression"},
          description = "TC_CART_003: Verify cart badge count updates dynamically on adding and removing items")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Cart Badge Count Dynamic Update")
    public void testTC_CART_003_CartBadgeCountDynamicUpdate() {
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 0, "Initial cart badge count should be 0.");

        inventoryPage.addItemToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(1, 5), 1, "Badge count should be 1 after adding first item.");

        inventoryPage.addItemToCartByName("Sauce Labs Bike Light");
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(2, 5), 2, "Badge count should be 2 after adding second item.");

        inventoryPage.removeItemByName("Sauce Labs Backpack");
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(1, 5), 1, "Badge count should decrement to 1 after removing item from inventory.");

        CartPage cartPage = inventoryPage.goToCart();
        cartPage.removeItemByName("Sauce Labs Bike Light");
        Assert.assertEquals(cartPage.waitForCartBadgeCount(0, 5), 0, "Badge count should reset to 0 after removing all items from cart.");
    }

    @Test(priority = 4, groups = {"regression"},
          description = "TC_CART_004: Verify product name in cart matches catalog item name exactly")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Name Matching")
    public void testTC_CART_004_ProductNameInCart() {
        String expectedName = inventoryPage.getProductNames().get(0);
        inventoryPage.addItemToCartByName(expectedName);

        CartPage cartPage = inventoryPage.goToCart();
        String actualName = cartPage.getCartItemNames().get(0);
        Assert.assertEquals(actualName, expectedName, "Cart product name must match catalog product name exactly.");
    }

    @Test(priority = 5, groups = {"regression"},
          description = "TC_CART_005: Verify product price in cart matches catalog price using BigDecimal")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Price BigDecimal Matching")
    public void testTC_CART_005_ProductPriceInCartBigDecimal() {
        BigDecimal expectedPrice = inventoryPage.getProductPricesAsBigDecimal().get(0);
        String productName = inventoryPage.getProductNames().get(0);
        inventoryPage.addItemToCartByName(productName);

        CartPage cartPage = inventoryPage.goToCart();
        BigDecimal actualPrice = cartPage.getCartItemPricesAsBigDecimal().get(0);
        Assert.assertEquals(actualPrice, expectedPrice, "Cart BigDecimal price must match catalog BigDecimal price exactly.");
    }

    @Test(priority = 6, groups = {"regression"},
          description = "TC_CART_006: Verify removing product from inventory page resets badge count")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Remove Product from Inventory")
    public void testTC_CART_006_RemoveProductFromInventory() {
        String productName = "Sauce Labs Backpack";
        inventoryPage.addItemToCartByName(productName);
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(1, 5), 1, "Cart badge count should be 1.");

        inventoryPage.removeItemByName(productName);
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(0, 5), 0, "Cart badge count should be 0 after removing from inventory.");
    }

    @Test(priority = 7, groups = {"regression"},
          description = "TC_CART_007: Verify removing product from cart page updates item list and badge count")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Remove Product from Cart")
    public void testTC_CART_007_RemoveProductFromCart() {
        inventoryPage.addItemToCartByName("Sauce Labs Backpack");
        inventoryPage.addItemToCartByName("Sauce Labs Bike Light");

        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Initial cart item count should be 2.");

        cartPage.removeItemByName("Sauce Labs Backpack");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart item count should be 1 after removal.");
        Assert.assertEquals(cartPage.waitForCartBadgeCount(1, 5), 1, "Cart badge count should be 1 after removal.");
        Assert.assertEquals(cartPage.getCartItemNames().get(0), "Sauce Labs Bike Light", "Remaining item should be Bike Light.");
    }

    @Test(priority = 8, groups = {"regression"},
          description = "TC_CART_008: Verify empty cart after removing all items")
    @Severity(SeverityLevel.NORMAL)
    @Story("Empty Cart")
    public void testTC_CART_008_EmptyCartAfterRemovingEverything() {
        inventoryPage.addItemToCartByName("Sauce Labs Backpack");
        inventoryPage.addItemToCartByName("Sauce Labs Onesie");

        CartPage cartPage = inventoryPage.goToCart();
        cartPage.removeAllItems();

        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart item count should be 0 after removing everything.");
        Assert.assertEquals(cartPage.waitForCartBadgeCount(0, 5), 0, "Cart badge count should be 0.");
    }

    @Test(priority = 9, groups = {"smoke", "regression"},
          description = "TC_CART_009: Verify Continue Shopping button navigates back to inventory catalog")
    @Severity(SeverityLevel.NORMAL)
    @Story("Continue Shopping Navigation")
    public void testTC_CART_009_ContinueShopping() {
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page displayed.");

        InventoryPage returnedInventory = cartPage.continueShopping();
        Assert.assertTrue(returnedInventory.isInventoryPageDisplayed(), "Should navigate back to inventory catalog.");
    }

    @Test(priority = 10, groups = {"regression"},
          description = "TC_CART_010: Verify cart state persists across supported navigation flows")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Cart State Persistence Across Navigation")
    public void testTC_CART_010_CartStateAcrossSupportedNavigation() {
        inventoryPage.addItemToCartByName("Sauce Labs Backpack");
        inventoryPage.addItemToCartByName("Sauce Labs Bike Light");
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(2, 5), 2, "Initial badge count 2.");

        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart item count 2.");

        InventoryPage inventory = cartPage.continueShopping();
        ProductDetailsPage detailsPage = inventory.clickProductTitleByName("Sauce Labs Fleece Jacket");
        Assert.assertTrue(detailsPage.isProductDetailsPageDisplayed(), "Product details displayed.");

        CartPage finalCartPage = inventory.goToCart();
        Assert.assertEquals(finalCartPage.getCartItemCount(), 2, "Cart item count should remain 2 after navigating.");
        Assert.assertEquals(finalCartPage.getCartBadgeCount(), 2, "Badge count should remain 2 after navigating.");
    }

    @Test(priority = 11, groups = {"regression"},
          description = "TC_CART_011: Verify correct items and BigDecimal prices after mixed add/remove operations")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Mixed Add/Remove Operations State Verification")
    public void testTC_CART_011_CorrectItemsAfterMixedAddRemoveOperations() {
        // Step 1: Add items A, B, C
        inventoryPage.addItemToCartByName("Sauce Labs Backpack");    // A ($29.99)
        inventoryPage.addItemToCartByName("Sauce Labs Bike Light");   // B ($9.99)
        inventoryPage.addItemToCartByName("Sauce Labs Bolt T-Shirt"); // C ($15.99)
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(3, 5), 3, "Badge count should be 3.");

        // Step 2: Remove B from inventory page
        inventoryPage.removeItemByName("Sauce Labs Bike Light");
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(2, 5), 2, "Badge count should be 2.");

        // Step 3: Add D from inventory page
        inventoryPage.addItemToCartByName("Sauce Labs Onesie");        // D ($7.99)
        Assert.assertEquals(inventoryPage.waitForCartBadgeCount(3, 5), 3, "Badge count should be 3.");

        // Step 4: Go to cart and remove A
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 3, "Cart item count 3 before removal.");
        cartPage.removeItemByName("Sauce Labs Backpack");             // Remove A

        // Step 5: Verify remaining items C ("Sauce Labs Bolt T-Shirt") and D ("Sauce Labs Onesie")
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Final cart item count should be 2.");
        Assert.assertEquals(cartPage.waitForCartBadgeCount(2, 5), 2, "Final badge count should be 2.");

        List<String> actualNames = cartPage.getCartItemNames();
        List<BigDecimal> actualPrices = cartPage.getCartItemPricesAsBigDecimal();

        Assert.assertTrue(actualNames.contains("Sauce Labs Bolt T-Shirt"), "Cart must contain Bolt T-Shirt.");
        Assert.assertTrue(actualNames.contains("Sauce Labs Onesie"), "Cart must contain Onesie.");

        Assert.assertTrue(actualPrices.contains(new BigDecimal("15.99")), "Cart must contain BigDecimal price 15.99.");
        Assert.assertTrue(actualPrices.contains(new BigDecimal("7.99")), "Cart must contain BigDecimal price 7.99.");
    }
}
