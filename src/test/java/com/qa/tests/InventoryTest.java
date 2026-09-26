package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.pages.InventoryPage;
import com.qa.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Epic("Inventory Module")
@Feature("Product Catalog & Sorting")
public class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToInventory() {
        LoginPage loginPage = new LoginPage(getDriver());
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page must be displayed after login.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"},
          description = "TC_INV_001: Verify product catalog renders inventory items container with available products")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Product Listing")
    public void testTC_INV_001_ProductListing() {
        int itemTotal = inventoryPage.getItemCount();
        Assert.assertTrue(itemTotal > 0, "Catalog should render at least 1 product item.");
        logger.info("Validated inventory item listing container. Current displayed item count: {}", itemTotal);
    }

    @Test(priority = 2, groups = {"regression"},
          description = "TC_INV_002: Verify all catalog items display non-empty product names")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Names")
    public void testTC_INV_002_ProductNames() {
        List<String> productNames = inventoryPage.getProductNames();
        Assert.assertFalse(productNames.isEmpty(), "Product names list should not be empty.");
        for (String name : productNames) {
            Assert.assertNotNull(name, "Product name should not be null.");
            Assert.assertFalse(name.trim().isEmpty(), "Product name should not be empty string.");
        }
    }

    @Test(priority = 3, groups = {"regression"},
          description = "TC_INV_003: Verify all catalog items display valid BigDecimal prices greater than zero")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Prices")
    public void testTC_INV_003_ProductPrices() {
        List<BigDecimal> prices = inventoryPage.getProductPricesAsBigDecimal();
        Assert.assertFalse(prices.isEmpty(), "Prices list should not be empty.");
        for (BigDecimal price : prices) {
            Assert.assertNotNull(price, "Parsed BigDecimal price should not be null.");
            Assert.assertTrue(price.compareTo(BigDecimal.ZERO) > 0, "Product price must be greater than $0.00.");
        }
    }

    @Test(priority = 4, groups = {"regression"},
          description = "TC_INV_004: Verify all product items render valid images with src attributes")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Images")
    public void testTC_INV_004_ProductImages() {
        Assert.assertTrue(inventoryPage.areAllProductImagesDisplayed(), "All product images should be displayed and have src attributes.");
        List<String> imageSources = inventoryPage.getProductImageSources();
        for (String src : imageSources) {
            Assert.assertNotNull(src, "Image src attribute should not be null.");
            Assert.assertFalse(src.trim().isEmpty(), "Image src attribute should not be empty.");
        }
    }

    @Test(priority = 5, groups = {"regression"},
          description = "TC_INV_005: Verify product cards render title, description, and price content")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Details Content")
    public void testTC_INV_005_ProductDetailsContent() {
        List<String> names = inventoryPage.getProductNames();
        List<String> descriptions = inventoryPage.getProductDescriptions();
        List<BigDecimal> prices = inventoryPage.getProductPricesAsBigDecimal();

        Assert.assertEquals(descriptions.size(), names.size(), "Each product must have a corresponding description.");
        Assert.assertEquals(prices.size(), names.size(), "Each product must have a corresponding price.");

        for (int i = 0; i < names.size(); i++) {
            Assert.assertFalse(names.get(i).isEmpty(), "Item title should not be empty.");
            Assert.assertFalse(descriptions.get(i).isEmpty(), "Item description should not be empty.");
            Assert.assertTrue(prices.get(i).compareTo(BigDecimal.ZERO) > 0, "Item price should be > 0.");
        }
    }

    @Test(priority = 6, groups = {"regression"},
          description = "TC_INV_006: Verify product sorting by Name (A to Z) using Java Collections")
    @Severity(SeverityLevel.NORMAL)
    @Story("Sorting A-Z")
    public void testTC_INV_006_SortingNameAZ() {
        inventoryPage.selectSortOption("az");
        List<String> actualNames = inventoryPage.getProductNames();

        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames);

        Assert.assertEquals(actualNames, expectedNames, "Products should be sorted alphabetically from A to Z.");
    }

    @Test(priority = 7, groups = {"regression"},
          description = "TC_INV_007: Verify product sorting by Name (Z to A) using Java Collections")
    @Severity(SeverityLevel.NORMAL)
    @Story("Sorting Z-A")
    public void testTC_INV_007_SortingNameZA() {
        inventoryPage.selectSortOption("za");
        List<String> actualNames = inventoryPage.getProductNames();

        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames, Collections.reverseOrder());

        Assert.assertEquals(actualNames, expectedNames, "Products should be sorted in reverse alphabetical order Z to A.");
    }

    @Test(priority = 8, groups = {"regression"},
          description = "TC_INV_008: Verify product sorting by Price (low to high) using BigDecimal Java Collections")
    @Severity(SeverityLevel.NORMAL)
    @Story("Sorting Price Low to High")
    public void testTC_INV_008_SortingPriceLowToHigh() {
        inventoryPage.selectSortOption("lohi");
        List<BigDecimal> actualPrices = inventoryPage.getProductPricesAsBigDecimal();

        List<BigDecimal> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices, "Products should be sorted by price in ascending order.");
    }

    @Test(priority = 9, groups = {"regression"},
          description = "TC_INV_009: Verify product sorting by Price (high to low) using BigDecimal Java Collections")
    @Severity(SeverityLevel.NORMAL)
    @Story("Sorting Price High to Low")
    public void testTC_INV_009_SortingPriceHighToLow() {
        inventoryPage.selectSortOption("hilo");
        List<BigDecimal> actualPrices = inventoryPage.getProductPricesAsBigDecimal();

        List<BigDecimal> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices, Collections.reverseOrder());

        Assert.assertEquals(actualPrices, expectedPrices, "Products should be sorted by price in descending order.");
    }
}
