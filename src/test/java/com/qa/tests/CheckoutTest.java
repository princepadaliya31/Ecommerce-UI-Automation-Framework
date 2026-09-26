package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.pages.*;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

@Epic("Checkout Module")
@Feature("Checkout & Order Validation")
public class CheckoutTest extends BaseTest {

    private CartPage cartPage;

    @BeforeMethod(alwaysRun = true)
    public void prepareCartWithItem() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        inventoryPage.addItemToCartByName("Sauce Labs Backpack");
        cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"},
          description = "TC_CHECKOUT_001: Verify successful navigation to Overview page with valid customer information")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Valid Customer Information")
    public void testTC_CHECKOUT_001_ValidCustomerInformation() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        Assert.assertTrue(infoPage.isCheckoutInformationPageDisplayed(), "Information page displayed.");

        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");
        Assert.assertTrue(overviewPage.isCheckoutOverviewPageDisplayed(), "Overview page displayed after valid information submission.");
    }

    @Test(priority = 2, groups = {"regression"},
          description = "TC_CHECKOUT_002: Verify error message when First Name is omitted")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Missing First Name Validation")
    public void testTC_CHECKOUT_002_EmptyFirstName() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        infoPage.fillInformation("", "Doe", "90210");
        infoPage.clickContinue();

        Assert.assertEquals(infoPage.getErrorMessage(), "Error: First Name is required",
                "Error message should state First Name is required.");
    }

    @Test(priority = 3, groups = {"regression"},
          description = "TC_CHECKOUT_003: Verify error message when Last Name is omitted")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Missing Last Name Validation")
    public void testTC_CHECKOUT_003_EmptyLastName() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        infoPage.fillInformation("John", "", "90210");
        infoPage.clickContinue();

        Assert.assertEquals(infoPage.getErrorMessage(), "Error: Last Name is required",
                "Error message should state Last Name is required.");
    }

    @Test(priority = 4, groups = {"regression"},
          description = "TC_CHECKOUT_004: Verify error message when Postal Code is omitted")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Missing Postal Code Validation")
    public void testTC_CHECKOUT_004_EmptyPostalCode() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        infoPage.fillInformation("John", "Doe", "");
        infoPage.clickContinue();

        Assert.assertEquals(infoPage.getErrorMessage(), "Error: Postal Code is required",
                "Error message should state Postal Code is required.");
    }

    @Test(priority = 5, groups = {"regression"},
          description = "TC_CHECKOUT_005: Verify Checkout Overview renders payment and shipping summary information")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overview Information Summary")
    public void testTC_CHECKOUT_005_CheckoutOverviewSummaryInfo() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");

        Assert.assertTrue(overviewPage.getPaymentInfo().contains("SauceCard"), "Payment Info should contain SauceCard.");
        Assert.assertTrue(overviewPage.getShippingInfo().contains("Free Pony Express Delivery!"), "Shipping Info should contain Free Pony Express Delivery!.");
    }

    @Test(priority = 6, groups = {"regression"},
          description = "TC_CHECKOUT_006: Verify Checkout Overview displays selected product names")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Overview Product Names")
    public void testTC_CHECKOUT_006_SelectedProductNames() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");

        List<String> names = overviewPage.getItemNames();
        Assert.assertTrue(names.contains("Sauce Labs Backpack"), "Overview should display Sauce Labs Backpack.");
    }

    @Test(priority = 7, groups = {"regression"},
          description = "TC_CHECKOUT_007: Verify Checkout Overview displays item BigDecimal prices")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Overview Item Prices")
    public void testTC_CHECKOUT_007_ItemPricesBigDecimal() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");

        List<BigDecimal> prices = overviewPage.getItemPricesAsBigDecimal();
        Assert.assertTrue(prices.contains(new BigDecimal("29.99")), "Overview item price should match $29.99.");
    }

    @Test(priority = 8, groups = {"regression"},
          description = "TC_CHECKOUT_008: Verify item subtotal equals calculated sum of BigDecimal item prices")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Item Subtotal Calculation")
    public void testTC_CHECKOUT_008_ItemSubtotalBigDecimal() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");

        List<BigDecimal> itemPrices = overviewPage.getItemPricesAsBigDecimal();
        BigDecimal calculatedSubtotal = BigDecimal.ZERO;
        for (BigDecimal price : itemPrices) {
            calculatedSubtotal = calculatedSubtotal.add(price);
        }

        BigDecimal displayedSubtotal = overviewPage.getSubtotalAsBigDecimal();
        Assert.assertEquals(displayedSubtotal, calculatedSubtotal, "Displayed subtotal must equal sum of item prices.");
    }

    @Test(priority = 9, groups = {"regression"},
          description = "TC_CHECKOUT_009: Verify tax is calculated and greater than zero BigDecimal")
    @Severity(SeverityLevel.NORMAL)
    @Story("Tax Calculation")
    public void testTC_CHECKOUT_009_TaxBigDecimal() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");

        BigDecimal tax = overviewPage.getTaxAsBigDecimal();
        Assert.assertTrue(tax.compareTo(BigDecimal.ZERO) > 0, "Displayed tax must be greater than $0.00.");
    }

    @Test(priority = 10, groups = {"smoke", "regression"},
          description = "TC_CHECKOUT_010: Verify mathematical validation: Displayed Total = Displayed Subtotal + Displayed Tax")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Mathematical Total Validation")
    public void testTC_CHECKOUT_010_TotalMathematicalValidationBigDecimal() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");

        BigDecimal subtotal = overviewPage.getSubtotalAsBigDecimal();
        BigDecimal tax = overviewPage.getTaxAsBigDecimal();
        BigDecimal displayedTotal = overviewPage.getTotalAsBigDecimal();

        BigDecimal calculatedTotal = subtotal.add(tax);
        Assert.assertEquals(displayedTotal, calculatedTotal, "Displayed total must equal displayed subtotal + displayed tax.");
    }

    @Test(priority = 11, groups = {"regression"},
          description = "TC_CHECKOUT_011: Verify clicking Cancel on Information and Overview pages returns user to appropriate screens")
    @Severity(SeverityLevel.NORMAL)
    @Story("Cancel Checkout Flow")
    public void testTC_CHECKOUT_011_CancelCheckout() {
        // Cancel from Information page returns to CartPage
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CartPage returnedCart = infoPage.cancelCheckout();
        Assert.assertTrue(returnedCart.isCartPageDisplayed(), "Canceling from Information page should return to Cart page.");

        // Cancel from Overview page returns to InventoryPage
        CheckoutInformationPage infoPage2 = returnedCart.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage2.continueToOverview("John", "Doe", "90210");
        InventoryPage returnedInventory = overviewPage.cancelCheckout();
        Assert.assertTrue(returnedInventory.isInventoryPageDisplayed(), "Canceling from Overview page should return to Inventory catalog page.");
    }

    @Test(priority = 12, groups = {"smoke", "regression"},
          description = "TC_CHECKOUT_012: Verify clicking Finish completes checkout and navigates to Order Complete page")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Complete Checkout")
    public void testTC_CHECKOUT_012_CompleteCheckout() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");

        CheckoutCompletePage completePage = overviewPage.finishCheckout();
        Assert.assertTrue(completePage.isCheckoutCompletePageDisplayed(), "Complete page should be displayed after clicking Finish.");
    }

    @Test(priority = 13, groups = {"smoke", "regression"},
          description = "TC_CHECKOUT_013: Verify Order Confirmation headers and text on Order Complete page")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Order Confirmation Content")
    public void testTC_CHECKOUT_013_OrderConfirmationContent() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");
        CheckoutCompletePage completePage = overviewPage.finishCheckout();

        Assert.assertEquals(completePage.getCompleteHeader(), "Thank you for your order!", "Completion header should match expected.");
        Assert.assertTrue(completePage.getCompleteText().contains("Your order has been dispatched"), "Completion description text should contain dispatch notice.");
    }

    @Test(priority = 14, groups = {"regression"},
          description = "TC_CHECKOUT_014: Verify clicking Back Home on Order Complete page returns user to Inventory page")
    @Severity(SeverityLevel.NORMAL)
    @Story("Back Home Navigation")
    public void testTC_CHECKOUT_014_ReturnToProducts() {
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        CheckoutOverviewPage overviewPage = infoPage.continueToOverview("John", "Doe", "90210");
        CheckoutCompletePage completePage = overviewPage.finishCheckout();

        InventoryPage inventoryPage = completePage.backHome();
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Clicking Back Home should return user to Inventory page.");
    }
}
