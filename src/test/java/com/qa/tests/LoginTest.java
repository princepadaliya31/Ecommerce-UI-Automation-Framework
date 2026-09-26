package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.dataproviders.LoginDataProvider;
import com.qa.pages.InventoryPage;
import com.qa.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Authentication Module")
@Feature("User Login & Session Management")
public class LoginTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke", "regression"},
          description = "TC_LOGIN_001: Verify successful login with valid standard user credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Valid Login")
    public void testTC_LOGIN_001_ValidCredentials() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed.");

        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page should be displayed after successful login.");
        Assert.assertEquals(inventoryPage.getHeaderTitle(), "Products", "Header title should be 'Products'.");
    }

    @Test(priority = 2, groups = {"regression"},
          description = "TC_LOGIN_002: Verify error message when attempting login with invalid username")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid Username")
    public void testTC_LOGIN_002_InvalidUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("invalid_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error banner should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service",
                "Error message should match expected invalid credentials error.");
    }

    @Test(priority = 3, groups = {"regression"},
          description = "TC_LOGIN_003: Verify error message when attempting login with invalid password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid Password")
    public void testTC_LOGIN_003_InvalidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("standard_user", "wrong_password");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error banner should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service",
                "Error message should match expected invalid credentials error.");
    }

    @Test(priority = 4, groups = {"regression"},
          description = "TC_LOGIN_004: Verify error message when username field is left empty")
    @Severity(SeverityLevel.NORMAL)
    @Story("Empty Username")
    public void testTC_LOGIN_004_EmptyUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error banner should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username is required",
                "Error message should state Username is required.");
    }

    @Test(priority = 5, groups = {"regression"},
          description = "TC_LOGIN_005: Verify error message when password field is left empty")
    @Severity(SeverityLevel.NORMAL)
    @Story("Empty Password")
    public void testTC_LOGIN_005_EmptyPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("standard_user", "");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error banner should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Password is required",
                "Error message should state Password is required.");
    }

    @Test(priority = 6, groups = {"regression"},
          description = "TC_LOGIN_006: Verify error message for locked-out user account")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Locked Out User")
    public void testTC_LOGIN_006_LockedOutUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error banner should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Sorry, this user has been locked out.",
                "Error message should state user is locked out.");
    }

    @Test(priority = 7, groups = {"regression"},
          description = "TC_LOGIN_007: Verify password input field masks text input")
    @Severity(SeverityLevel.MINOR)
    @Story("Security & UI")
    public void testTC_LOGIN_007_PasswordMasking() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isPasswordMasked(), "Password input field attribute 'type' must be 'password'.");
    }

    @Test(priority = 8, groups = {"smoke", "regression"},
          description = "TC_LOGIN_008: Verify user can successfully log out from the application")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Logout")
    public void testTC_LOGIN_008_SuccessfulLogout() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "User should be logged in.");

        LoginPage loggedOutPage = inventoryPage.logout();
        Assert.assertTrue(loggedOutPage.isLoginPageDisplayed(), "Login page should be displayed after logout.");
    }

    @Test(priority = 9, groups = {"regression"},
          dataProvider = "loginCsvData", dataProviderClass = LoginDataProvider.class,
          description = "TC_LOGIN_009: Data-driven CSV verification of negative login scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Story("CSV Data-Driven Login Errors")
    public void testTC_LOGIN_009_CSVDataDrivenErrors(String username, String password, String expectedError) {
        logger.info("Executing CSV login test for username: {}", username);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error banner should be displayed for negative scenario.");
        Assert.assertEquals(loginPage.getErrorMessage(), expectedError, "CSV Expected error message should match actual.");
    }

    @Test(priority = 10, groups = {"regression"},
          description = "TC_LOGIN_010: Verify protected page redirection to login after session logout")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Session Security & Protected Pages")
    public void testTC_LOGIN_010_ProtectedPageBehaviorAfterLogout() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page displayed.");

        // Logout
        LoginPage loggedOutPage = inventoryPage.logout();
        Assert.assertTrue(loggedOutPage.isLoginPageDisplayed(), "User logged out.");

        // Attempt direct access to protected page /inventory.html
        getDriver().get("https://www.saucedemo.com/inventory.html");

        // Verify user is blocked and redirected back to login page with error
        Assert.assertTrue(loggedOutPage.isLoginPageDisplayed(), "User should be redirected back to Login page.");
        Assert.assertTrue(loggedOutPage.isErrorMessageDisplayed(), "Protected access error banner should be displayed.");
        Assert.assertEquals(loggedOutPage.getErrorMessage(),
                "Epic sadface: You can only access '/inventory.html' when you are logged in.",
                "Protected page access error message should match.");
    }
}
