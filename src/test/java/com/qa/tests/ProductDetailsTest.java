package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.pages.InventoryPage;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

@Epic("Inventory Module")
@Feature("Product Details Navigation")
public class ProductDetailsTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToInventory() {
        LoginPage loginPage = new LoginPage(getDriver());
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page must be displayed after login.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"},
          description = "TC_INV_010: Verify clicking product title navigates to Product Details page matching title and price")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Details Navigation")
    public void testTC_INV_010_ProductDetailsNavigation() {
        List<String> names = inventoryPage.getProductNames();
        List<BigDecimal> prices = inventoryPage.getProductPricesAsBigDecimal();

        String targetName = names.get(0);
        BigDecimal targetPrice = prices.get(0);

        ProductDetailsPage detailsPage = inventoryPage.clickProductTitleByName(targetName);
        Assert.assertTrue(detailsPage.isProductDetailsPageDisplayed(), "Product details page should be displayed.");
        Assert.assertEquals(detailsPage.getProductName(), targetName, "Product Details name should match selected item.");
        Assert.assertEquals(detailsPage.getProductPriceAsBigDecimal(), targetPrice, "Product Details price should match selected item.");
    }

    @Test(priority = 2, groups = {"regression"},
          description = "TC_INV_011: Verify clicking Back to Products returns user to Inventory page")
    @Severity(SeverityLevel.NORMAL)
    @Story("Back to Products Navigation")
    public void testTC_INV_011_BackToInventory() {
        ProductDetailsPage detailsPage = inventoryPage.clickFirstProductTitle();
        Assert.assertTrue(detailsPage.isProductDetailsPageDisplayed(), "Product details page displayed.");

        InventoryPage returnedInventoryPage = detailsPage.backToProducts();
        Assert.assertTrue(returnedInventoryPage.isInventoryPageDisplayed(), "User should return to Inventory catalog page.");
    }
}
