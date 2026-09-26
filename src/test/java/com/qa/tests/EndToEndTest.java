package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.pages.*;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

@Epic("End-To-End Workflows")
@Feature("Complete Customer Purchasing Journey")
public class EndToEndTest extends BaseTest {

    @Test(priority = 1, groups = {"e2e", "regression"},
          description = "TC_E2E_001: Execute complete 25-step customer purchasing flow from login to logout")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Customer E2E Journey")
    public void testTC_E2E_001_CompleteCustomerJourney() {
        // Step 1: Open SauceDemo & Step 2: Verify login page
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Step 1 & 2: Login page should be rendered.");

        // Step 3: Login with valid credentials & Step 4: Verify inventory
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Step 3 & 4: Inventory page should be displayed after login.");

        // Step 5: Select two products & Step 6: Capture names and prices
        String product1Name = "Sauce Labs Backpack";
        String product2Name = "Sauce Labs Bike Light";
        BigDecimal product1Price = inventoryPage.getProductPriceByName(product1Name);
        BigDecimal product2Price = inventoryPage.getProductPriceByName(product2Name);

        Assert.assertNotNull(product1Price, "Product 1 price captured.");
        Assert.assertNotNull(product2Price, "Product 2 price captured.");

        // Step 7: Add products to cart
        inventoryPage.addItemToCartByName(product1Name);
        inventoryPage.addItemToCartByName(product2Name);

        // Step 8: Verify cart badge count equals 2
        int badgeCount = inventoryPage.waitForCartBadgeCount(2, 5);
        Assert.assertEquals(badgeCount, 2, "Step 8: Cart badge count should equal 2.");

        // Step 9: Open cart
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Step 9: Cart page should be displayed.");

        // Step 10: Verify correct product names in cart
        List<String> cartNames = cartPage.getCartItemNames();
        Assert.assertTrue(cartNames.contains(product1Name), "Step 10: Cart contains Product 1.");
        Assert.assertTrue(cartNames.contains(product2Name), "Step 10: Cart contains Product 2.");

        // Step 11: Verify correct prices in cart
        List<BigDecimal> cartPrices = cartPage.getCartItemPricesAsBigDecimal();
        Assert.assertTrue(cartPrices.contains(product1Price), "Step 11: Cart contains Product 1 price.");
        Assert.assertTrue(cartPrices.contains(product2Price), "Step 11: Cart contains Product 2 price.");

        // Step 12: Checkout & Step 13: Enter customer information & Step 14: Continue
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        Assert.assertTrue(infoPage.isCheckoutInformationPageDisplayed(), "Step 12: Checkout Information page displayed.");

        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("Alex", "Smith", "90210");

        // Step 15: Verify checkout overview page
        Assert.assertTrue(overviewPage.isCheckoutOverviewPageDisplayed(), "Step 15: Checkout Overview page displayed.");

        // Step 16: Verify selected items on overview
        List<String> overviewNames = overviewPage.getItemNames();
        Assert.assertTrue(overviewNames.contains(product1Name), "Step 16: Overview displays Product 1.");
        Assert.assertTrue(overviewNames.contains(product2Name), "Step 16: Overview displays Product 2.");

        // Step 17: Calculate expected subtotal & Step 18: Verify displayed subtotal
        BigDecimal expectedSubtotal = product1Price.add(product2Price);
        BigDecimal displayedSubtotal = overviewPage.getSubtotalAsBigDecimal();
        Assert.assertEquals(displayedSubtotal, expectedSubtotal, "Step 18: Displayed subtotal matches sum of selected items.");

        // Step 19: Verify tax calculation
        BigDecimal tax = overviewPage.getTaxAsBigDecimal();
        Assert.assertTrue(tax.compareTo(BigDecimal.ZERO) > 0, "Step 19: Displayed tax is greater than zero.");

        // Step 20: Verify total = subtotal + tax
        BigDecimal expectedTotal = expectedSubtotal.add(tax);
        BigDecimal displayedTotal = overviewPage.getTotalAsBigDecimal();
        Assert.assertEquals(displayedTotal, expectedTotal, "Step 20: Displayed total equals subtotal + tax.");

        // Step 21: Finish order & Step 22: Verify order confirmation
        CheckoutCompletePage completePage = overviewPage.finishCheckout();
        Assert.assertTrue(completePage.isCheckoutCompletePageDisplayed(), "Step 21 & 22: Order Complete page displayed.");
        Assert.assertEquals(completePage.getCompleteHeader(), "Thank you for your order!", "Step 22: Completion header matches.");

        // Step 23: Back to products
        InventoryPage returnedInventory = completePage.backHome();
        Assert.assertTrue(returnedInventory.isInventoryPageDisplayed(), "Step 23: Inventory page displayed after clicking Back Home.");

        // Step 24: Logout & Step 25: Verify login page
        LoginPage returnedLogin = returnedInventory.logout();
        Assert.assertTrue(returnedLogin.isLoginPageDisplayed(), "Step 24 & 25: Login page displayed after logging out.");
    }
}
